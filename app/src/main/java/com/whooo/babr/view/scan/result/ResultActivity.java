package com.whooo.babr.view.scan.result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivitySearchResultBinding;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.widget.DividerItemDecoration;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ResultActivity extends BaseActivity implements ResultContract.View {
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_CART_ID = "EXTRA_CART_ID";

    private RecyclerView mRecyclerView;

    private ArrayList<Product> mProducts = new ArrayList<>();
    private String mCardId;

    @Inject
    ResultContract.Presenter mPresenter;
    @NonNull
    private Context mContext;
    @Nullable
    private AlertDialog mProgressDialog;

    @NonNull
    private CoordinatorLayout mLayoutCoordinator;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchResultBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result);
        mContext = this;

        if (savedInstanceState == null) {
            mProducts = getIntent().getParcelableArrayListExtra(EXTRA_DATA);
            mCardId = getIntent().getStringExtra(EXTRA_CART_ID);
        } else {
            mProducts = savedInstanceState.getParcelableArrayList(EXTRA_DATA);
            mCardId = savedInstanceState.getString(EXTRA_CART_ID);
        }
        if (mProducts == null) {
            showInAppError();
            return;
        }
        initializeInjector();
        injectViews(binding);
        setUpRecyclerView();
        binding.setViewmodel(mPresenter.getViewModel());
        binding.setPresenter(mPresenter);
    }

    private void initializeInjector() {
        DaggerResultComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .resultModule(new ResultModule(this, mProducts))
                .build()
                .inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_DATA, mProducts);
        outState.putString(EXTRA_CART_ID, mCardId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_save:
                mPresenter.saveProducts(mCardId);
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void injectViews(ActivitySearchResultBinding binding) {
        Toolbar mToolbar = binding.toolbar;
        mRecyclerView = binding.recyclerView;
        mLayoutCoordinator = binding.layoutCoordinator;

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void showNetworkError() {
        DialogFactory.
                createSimpleOkErrorDialog(mContext, R.string.dialog_error_title, R.string.dialog_internet_disconnnect_error).show();
    }

    @Override
    public void showInAppError() {
        DialogFactory
                .createSimpleOkErrorDialog(mContext, R.string.dialog_error_title, R.string.dialog_error_general_message).show();
    }

    @Override
    public void requestFailed(String message) {
        DialogFactory
                .createGenericErrorDialog(mContext, message).show();
    }

    @Override
    public void onSaveSuccess(List<Product> products) {
        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(EXTRA_DATA, (ArrayList<? extends Parcelable>) products);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(mContext);
            mProgressDialog.setOnCancelListener(dialog -> mPresenter.unsubscribe());
        }

        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void addPendingRemove(int position, Product product) {
        final Snackbar snackbar = Snackbar.make(mLayoutCoordinator, "Item deleted", Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(mContext, R.color.white))
                .setAction("Undo", view -> {
                    mPresenter.undoRemovedProduct(position, product);
                });

        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();

        Handler handler = new Handler();
        handler.postDelayed(() -> snackbar.dismiss(), 2000);
    }
}
