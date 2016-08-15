package com.whooo.babr.view.cart.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivityDetailBinding;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.scan.camera.CameraActivity;
import com.whooo.babr.view.widget.DividerItemDecoration;

import javax.inject.Inject;

public class DetailActivity extends BaseActivity implements DetailContract.View {

    private static final int REQUEST_ADD_MORE = 1;

    public static final String EXTRA_CART_ID = "EXTRA_CART_ID";
    public static final String EXTRA_CART_NAME = "EXTRA_CART_NAME";
    public static final String EXTRA_IS_PENDING = "EXTRA_IS_PENDING";

    private String mCardId;
    private String mProductName;
    private boolean mIsPending;

    @Inject
    DetailContract.Presenter mPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        if (savedInstanceState == null) {
            mCardId = getIntent().getStringExtra(EXTRA_CART_ID);
            mProductName = getIntent().getStringExtra(EXTRA_CART_NAME);
            mIsPending = getIntent().getBooleanExtra(EXTRA_IS_PENDING, false);
        } else {
            mCardId = savedInstanceState.getString(EXTRA_CART_ID);
            mProductName = savedInstanceState.getString(EXTRA_CART_NAME);
            mIsPending = savedInstanceState.getBoolean(EXTRA_IS_PENDING);
        }
        initInjector();
        initViews(binding);
        binding.setPresenter((DetailPresenter) mPresenter);
        binding.setViewModel(mPresenter.getViewModel());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_MORE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(DetailActivity.this, "New products has been added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_CART_ID, mCardId);
        outState.putString(EXTRA_CART_NAME, mProductName);
        outState.putBoolean(EXTRA_IS_PENDING, mIsPending);
    }

    private void initViews(ActivityDetailBinding binding) {
        Toolbar mToolbar = binding.toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(mProductName);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        if (mIsPending) {
            binding.fabScan.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.EXTRA_CARD_ID, mCardId);
                startActivityForResult(intent, REQUEST_ADD_MORE);
            });
        } else {
            binding.fabScan.setVisibility(View.GONE);
        }
    }

    private void initInjector() {
        DaggerDetailComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .detailModule(new DetailModule(this, mCardId))
                .build()
                .inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showNetworkError() {
        DialogFactory
                .createSimpleOkErrorDialog(this, R.string.dialog_error_title, R.string.dialog_internet_disconnnect_error)
                .show();
    }

    @Override
    public void showInAppError() {
        DialogFactory
                .createSimpleOkErrorDialog(this, R.string.dialog_error_title, R.string.dialog_error_general_message)
                .show();
    }
}