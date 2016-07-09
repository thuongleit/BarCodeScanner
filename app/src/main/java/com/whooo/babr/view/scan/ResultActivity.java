package com.whooo.babr.view.scan;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivitySearchResultBinding;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.product.ProductRecyclerAdapter;
import com.whooo.babr.view.widget.DividerItemDecoration;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;

public class ResultActivity extends BaseActivity {
    public static final String EXTRA_DATA = "EXTRA_DATA";

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    private ArrayList<Product> mProducts = new ArrayList<>();

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchResultBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result);

        injectViews(binding);
        mProducts = getIntent().getParcelableArrayListExtra(EXTRA_DATA);

        if (mProducts == null) {
            // TODO: 7/9/16 handle empty state
            // FIXME: 7/9/16 no empty list?
        }

        setUpRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void injectViews(ActivitySearchResultBinding binding) {
        mToolbar = binding.toolbar;
        mRecyclerView = binding.recyclerView;

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setAdapter(new ProductRecyclerAdapter(this, mProducts));
    }

    private void save() {
        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(EXTRA_DATA, mProducts);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
