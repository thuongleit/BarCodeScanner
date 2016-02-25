package com.thuongleit.babr.view.scan;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.thuongleit.babr.R;
import com.thuongleit.babr.data.remote.amazon.model.AmazonProductResponse;
import com.thuongleit.babr.view.base.BaseActivity;
import com.thuongleit.babr.view.product.ProductRecyclerAdapter;
import com.thuongleit.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchResultActivity extends BaseActivity implements ParsingView {

    public static final String EXTRA_DATA = "SearchResultActivity.EXTRA_DATA";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    ParsingPresenter mParsingPresenter;

    private ProductRecyclerAdapter mAdapter;
    private int mCurrentParsingIndex = 0;
    private Parcelable mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        getComponent().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mData = getIntent().getParcelableExtra(EXTRA_DATA);

        //check if the array contains product object
        if (mData != null) {
            if (mData instanceof Product) {
                bindView((Product) mData);
            } else if (mData instanceof AmazonProductResponse) {
                mParsingPresenter.attachView(this);
                AmazonProductResponse amazonProductResponse = (AmazonProductResponse) mData;
                mParsingPresenter.parse(amazonProductResponse.getProducts().get(mCurrentParsingIndex).getDetailPageURL());
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mParsingPresenter.detachView();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(SearchResultActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGeneralError(String message) {
        Toast.makeText(SearchResultActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onParseSuccess(Product product) {
        if (product != null) {
            bindView(product);
            AmazonProductResponse productResponse = (AmazonProductResponse) this.mData;
            if (mCurrentParsingIndex < productResponse.getProducts().size()) {
                mCurrentParsingIndex++;
                mParsingPresenter.parse(productResponse.getProducts().get(mCurrentParsingIndex).getDetailPageURL());
            }
        }
    }

    private void bindView(Product product) {
        mProgressWheel.stopSpinning();
        mProgressWheel.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            List<Product> products = new ArrayList<>();
            products.add(product);
            mAdapter = new ProductRecyclerAdapter(this, products);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.addItem(product);
        }
    }
}
