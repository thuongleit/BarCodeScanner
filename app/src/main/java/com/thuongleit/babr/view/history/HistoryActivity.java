package com.thuongleit.babr.view.history;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.thuongleit.babr.R;
import com.thuongleit.babr.config.Config;
import com.thuongleit.babr.data.DataManager;
import com.thuongleit.babr.data.local.ProductModel;
import com.thuongleit.babr.util.DialogFactory;
import com.thuongleit.babr.view.base.BaseActivity;
import com.thuongleit.babr.view.base.ToolbarActivity;
import com.thuongleit.babr.view.product.ProductRecyclerAdapter;
import com.thuongleit.babr.view.widget.DividerItemDecoration;
import com.thuongleit.babr.vo.ProductHistory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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

        Timber.d("onCreateSearchResultActivity");

        progressDialog = DialogFactory.createProgressDialog(this, "Loading...");
        progressDialog.show();

        adapterHistory = new AdapterHistory(this, productHistories);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setAdapter(adapterHistory);

        mDataManager.getProductsHistory()
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

}
