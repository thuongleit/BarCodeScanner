package com.jokotech.babr.view.history;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jokotech.babr.R;
import com.jokotech.babr.config.Config;
import com.jokotech.babr.data.DataManager;
import com.jokotech.babr.data.local.ProductModel;
import com.jokotech.babr.util.dialog.DialogFactory;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.widget.DividerItemDecoration;
import com.jokotech.babr.vo.ProductHistory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HistoryActivity extends BaseActivity {

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

    private ArrayList<ProductHistory> productHistories = new ArrayList<>();
    private AdapterHistory adapterHistory;
    private ProgressDialog progressDialog;

    private Subscription subscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ButterKnife.bind(this);

        getComponent().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("History");


        progressDialog = DialogFactory.createProgressDialog(this, "Loading...");
        progressDialog.show();

        adapterHistory = new AdapterHistory(this, productHistories);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setAdapter(adapterHistory);

        subscription=  mDataManager.getProductsHistory()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    progressDialog.dismiss();
                    productHistories.addAll(list);
                    adapterHistory.addItems(productHistories);
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
