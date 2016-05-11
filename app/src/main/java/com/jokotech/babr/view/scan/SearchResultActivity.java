package com.jokotech.babr.view.scan;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.jokotech.babr.R;
import com.jokotech.babr.config.Config;
import com.jokotech.babr.data.local.ProductModel;
import com.jokotech.babr.data.remote.amazon.model.AmazonProductResponse;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.main.MainActivity;
import com.jokotech.babr.view.product.ProductRecyclerAdapter;
import com.jokotech.babr.view.widget.DividerItemDecoration;
import com.jokotech.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

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
    @Inject
    Config mConfig;
    @Inject
    ProductModel mProductModel;

    private ProductRecyclerAdapter mAdapter;
    private int mCurrentParsingIndex = 0;
    private Parcelable mData;
    private ArrayList<Product> mProducts = new ArrayList<>();
    private List<Product> productListUserId = new ArrayList<>();
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

        setUpRecyclerView();

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
          // Toast.makeText(SearchResultActivity.this, message, Toast.LENGTH_SHORT).show();
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
            new AlertDialog.Builder(this, R.style.MyAlertDialogAppCompatStyle)
                    .setTitle("Option")
                    .setMessage(getString(R.string.choose_image))
                    .setPositiveButton("Always select all", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mConfig.putIsDontShow(true);
                            startResult();
                        }
                    }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startResult();
                        }
                    }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

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

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(SearchResultActivity.this, R.drawable.ic_delete_sweep_white_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) SearchResultActivity.this.getResources().getDimension(R.dimen.card_corner_radius);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ProductRecyclerAdapter adapter = (ProductRecyclerAdapter) mRecyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.deleteItem(swipedPosition);
                }
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                ProductRecyclerAdapter testAdapter = (ProductRecyclerAdapter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }

                if (!initiated) {
                    init();
                }

                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        };

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                if (parent.getItemAnimator().isRunning()) {

                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    int left = 0;
                    int right = parent.getWidth();

                    int top = 0;
                    int bottom = 0;

                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
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
            // mRecyclerView.setItemAnimator(new FeedItemAnimator());
        } else {
            mAdapter.addItem(product);
        }
    }


}
