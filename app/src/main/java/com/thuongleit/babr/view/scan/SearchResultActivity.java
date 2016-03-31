package com.thuongleit.babr.view.scan;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.thuongleit.babr.R;
import com.thuongleit.babr.config.Config;
import com.thuongleit.babr.data.local.ProductModel;
import com.thuongleit.babr.data.remote.amazon.model.AmazonProductResponse;
import com.thuongleit.babr.util.DialogSaveImage;
import com.thuongleit.babr.view.base.BaseActivity;
import com.thuongleit.babr.view.main.MainActivity;
import com.thuongleit.babr.view.product.ProductRecyclerAdapter;
import com.thuongleit.babr.view.widget.DividerItemDecoration;
import com.thuongleit.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SearchResultActivity extends BaseActivity implements ParsingView,
        ActionMode.Callback, DialogSaveImage.DialogSaveImageListener {

    public static final String EXTRA_DATA = "SearchResultActivity.EXTRA_DATA";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;


    @Inject
    ParsingPresenter mParsingPresenter;
    @Inject
    Config mConfig;
    @Inject
    ProductModel mProductModel;

    private ProductRecyclerAdapter mAdapter;
    private int mCurrentParsingIndex = 0;
    private Parcelable mData;
    private ArrayList<Product> mProducts = new ArrayList<>();
    private List<Product> productListUserId = new ArrayList<>();
    private ActionMode actionMode;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        getComponent().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Timber.d("onCreateSearchResultActivity");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mData = getIntent().getParcelableExtra(EXTRA_DATA);
        if (getIntent().getBooleanExtra(CameraActivity.EXTRA_LOAD_USER_ID, false)) {
            productListUserId = getIntent().getParcelableArrayListExtra(EXTRA_DATA);
            bindListView(productListUserId);
        } else {
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

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View iew, int position) {

                if (actionMode != null) {
                    myToggleSelection(position);
                    return;
                }

            }

            @Override
            public void onLongClick(View view, int position) {
                actionMode = startSupportActionMode(SearchResultActivity.this);
                myToggleSelection(position);

            }
        }));

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

    @OnClick(R.id.button_toolbar_cancel)
    public void cancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.button_toolbar_save)
    public void save() {
        if (!mConfig.isIsDontShow()) {
            DialogSaveImage dialogSaveImage = new DialogSaveImage(SearchResultActivity.this, this);
            dialogSaveImage.show();
        } else {
            startResult();
        }
    }



    private void startResult() {
        if (!getIntent().getBooleanExtra(CameraActivity.EXTRA_LOAD_USER_ID, false)) {
            Intent intent = getIntent();
            intent.putParcelableArrayListExtra(EXTRA_DATA, mProducts);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putParcelableArrayListExtra(EXTRA_DATA, mProducts);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }


    private void bindListView(List<Product> product) {
        mProgressWheel.stopSpinning();
        mProgressWheel.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        productList.addAll(product);
        if (mAdapter == null) {
            mProducts.addAll(product);

            mAdapter = new ProductRecyclerAdapter(this, mProducts);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.addItems(product);
        }
    }

    private void bindView(Product product) {
        mProgressWheel.stopSpinning();
        mProgressWheel.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        productList.add(product);
        if (mAdapter == null) {
            mProducts.add(product);

            mAdapter = new ProductRecyclerAdapter(this, mProducts);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.addItem(product);
        }
    }



    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.crime_list_item_context, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.menu_item_delete_crime) {


            List<Integer> selectedItemPositions = ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).getSelectedItems();
            int currPos;
            for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                currPos = selectedItemPositions.get(i);
                productList.remove(currPos);
                ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).deleteItem(currPos);
            }
            actionMode.finish();

            return true;

        }
        return false;

    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).clearSelections();
    }


    private void myToggleSelection(int idx) {
        ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).toggleSelection(idx);
        String title = getString(R.string.selected_count, ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).getSelectedItemCount());
        actionMode.setTitle(title);
    }

    @Override
    public void onCancel() {
        startResult();

    }

    @Override
    public void onChoose() {

    }

    @Override
    public void onDontShow() {
        mConfig.putIsDontShow(true);
        startResult();
    }


    public interface ClickListener {
        void onClick(View iew, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private SearchResultActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (view != null && clickListener != null) {
                        clickListener.onLongClick(view, recyclerView.getChildAdapterPosition(view));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View view = rv.findChildViewUnder(e.getX(), e.getY());
            if (view != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(view, rv.getChildAdapterPosition(view));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
