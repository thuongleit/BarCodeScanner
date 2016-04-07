package com.thuongleit.babr.view.history;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thuongleit.babr.R;
import com.thuongleit.babr.config.Config;
import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.data.DataManager;
import com.thuongleit.babr.data.local.ProductModel;
import com.thuongleit.babr.util.dialog.DialogFactory;
import com.thuongleit.babr.view.base.BaseActivity;
import com.thuongleit.babr.view.product.ProductRecyclerAdapter;
import com.thuongleit.babr.view.widget.DividerItemDecoration;
import com.thuongleit.babr.vo.Product;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailActivity extends BaseActivity {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    Config mConfig;
    @Inject
    ProductModel mProductModel;
    @Inject
    DataManager mDataManager;

    private ArrayList<Product> productHistories = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String listId;

    private Subscription subscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        getComponent().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Detail List");

        listId = getIntent().getStringExtra(Constant.KEY_LIST_ID);

        progressDialog = DialogFactory.createProgressDialog(this, "Loading...");
        progressDialog.show();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        subscription = mDataManager.getProductsCheckout(listId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    progressDialog.dismiss();
                    productHistories.addAll(list);
                    if ((mRecyclerView.getAdapter()) == null) {
                        RecyclerView.Adapter adapter = new ProductRecyclerAdapter(this, productHistories);
                        mRecyclerView.setAdapter(adapter);
                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
