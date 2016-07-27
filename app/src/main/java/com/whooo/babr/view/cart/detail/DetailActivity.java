package com.whooo.babr.view.cart.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivityDetailBinding;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.widget.DividerItemDecoration;

import javax.inject.Inject;

public class DetailActivity extends BaseActivity implements DetailContract.View {

    public static final String EXTRA_CART_ID = "EXTRA_CART_ID";
    public static final String EXTRA_CART_NAME = "EXTRA_CART_NAME";

    private String mId;
    private String mProductName;

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

        if (getIntent() != null) {
            mId = getIntent().getStringExtra(EXTRA_CART_ID);
            mProductName = getIntent().getStringExtra(EXTRA_CART_NAME);
        }
        initInjector();
        initViews(binding);
        binding.setPresenter((DetailPresenter) mPresenter);
        binding.setViewModel(mPresenter.getViewModel());
    }

    private void initViews(ActivityDetailBinding binding) {
        Toolbar mToolbar = binding.toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(mProductName);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initInjector() {
        DaggerDetailComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .detailModule(new DetailModule(this, mId))
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