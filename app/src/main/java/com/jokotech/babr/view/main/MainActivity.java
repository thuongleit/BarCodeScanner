package com.jokotech.babr.view.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.jokotech.babr.R;
import com.jokotech.babr.config.Config;
import com.jokotech.babr.config.Constant;
import com.jokotech.babr.data.DataManager;
import com.jokotech.babr.data.local.ProductModel;
import com.jokotech.babr.data.remote.ParseService;
import com.jokotech.babr.di.ActivityScope;
import com.jokotech.babr.util.AppUtils;
import com.jokotech.babr.util.dialog.DialogFactory;
import com.jokotech.babr.util.dialog.DialogHistory;
import com.jokotech.babr.util.dialog.DialogQrcodeHistory;
import com.jokotech.babr.view.AppIntroActivity;
import com.jokotech.babr.view.base.ToolbarActivity;
import com.jokotech.babr.view.history.HistoryActivity;
import com.jokotech.babr.view.product.ProductRecyclerAdapter;
import com.jokotech.babr.view.qrgenerate.GenerateQR;
import com.jokotech.babr.view.scan.CameraActivity;
import com.jokotech.babr.view.signin.SignInActivity;
import com.jokotech.babr.view.widget.DancingScriptTextView;
import com.jokotech.babr.view.widget.DividerItemDecoration;
import com.jokotech.babr.vo.Product;
import com.jokotech.babr.vo.ProductHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends ToolbarActivity implements NavigationView.OnNavigationItemSelectedListener, MainView,
        ActionMode.Callback, DialogHistory.DialogSaveHistoryListener {

    private static final int REQUEST_CAMERA = 1;
    private String userId;
    public static final String USER_ID_EXTRA = "user_id_extra";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;

    @Bind(R.id.searchbox)
    SearchBox searchBox;
    @Bind(R.id.fab_scan)
    FloatingActionButton fabScan;

    @Inject
    Config mConfig;
    @Inject
    MainPresenter mMainPresenter;
    @Inject
    @ActivityScope
    Context mContext;
    @Inject
    ProductModel mProductModel;
    @Inject
    DataManager mDataManager;
    private boolean mDoubleBackToExitPressedOnce = false;
    private View mViewNetworkError;
    private View mViewEmpty;
    private ParseService parseService;
    private List<Product> productList = new ArrayList<>();
    private ActionMode actionMode;
    private ProgressDialog progressDialog;

    private String generateListId;
    private Subscription subscription;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        mMainPresenter.attachView(this);

        if (mConfig.isFirstRun()) {
            startActivity(new Intent(this, AppIntroActivity.class));
            mConfig.putIsFirstRun(false);
        }

        setupReCyclerView();
        setupNavigationView();


        parseService = new ParseService();

        //try restore login session
        tryRestoreLoginSession();
        mMainPresenter.getProductsNotCheckout("a");


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
                actionMode = startSupportActionMode(MainActivity.this);
                myToggleSelection(position);
            }
        }));


        fabScan.setOnClickListener(v -> {
            // Must be done during an initialization phase like onCreate
            RxPermissions.getInstance(this)
                    .request(Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) { // Always true pre-M
                            int[] startingLocation = new int[2];
                            v.getLocationOnScreen(startingLocation);
                            startingLocation[0] += v.getWidth() / 2;

                            Intent intent = new Intent(mContext, CameraActivity.class);
                            intent.putExtra(CameraActivity.EXTRA_SERVICE, Constant.KEY_UPC_SERVICE);
                         //   intent.putExtra(CameraActivity.ARG_REVEAL_START_LOCATION, startingLocation);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else {
                            showToast("You must allow to use camera to access this function");
                        }
                    });

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            openSearch();
            return true;
        }

        if (id == R.id.action_camera) {

            if (productList.size() >= 1) {
                generateListId = AppUtils.generateString(new Random(), "1254789dhfoendlf89ssofnd896541", 20);

                DialogHistory dialogHistory = new DialogHistory(this, this, generateListId);
                dialogHistory.show();
            } else {
                showToast("You don't have any item!");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void openSearch() {
        getToolbar().setTitle("");
        getToolbarTitle().setVisibility(View.GONE);
        searchBox.revealFromMenuItem(R.id.action_search, this);
        searchBox.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {

            }

        });
        searchBox.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {

            }

            @Override
            public void onSearchClosed() {
                closeSearch();
                getToolbarTitle().setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchTermChanged(String term) {

                subscription = Observable.just(term)
                        .debounce(400, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(s -> {
                            if (mRecyclerView.getAdapter() != null) {
                                ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).filter(s);
                            }
                        });

            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(MainActivity.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResultClick(SearchResult result) {
            }

            @Override
            public void onSearchCleared() {

            }

        });

    }

    protected void closeSearch() {
        searchBox.hideCircularly(this);
        if (searchBox.getSearchText().isEmpty()) getToolbar().setTitle("");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            ArrayList<Product> products = data.getParcelableArrayListExtra(CameraActivity.EXTRA_DATA);
            removeAdditionalViews();
            if (products.size() >= 1) {
                productList.addAll(products);
                progressDialog = DialogFactory.createProgressDialog(this, "Loading...");
                progressDialog.show();
                if (mRecyclerView.getAdapter() == null) {
                    if (mConfig.isUserLogin()) {
                        parseService.saveListProduct(products).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(a -> {
                                    showToast("products has been saved!");

                                });
                        subscription = Observable.timer(3, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(a -> {
                                    progressDialog.dismiss();
                                    reloadActivity();

                                });

                    } else {
                        for (Product product : products) {
                            mProductModel.saveProduct(product);
                        }
                    }
                } else {


                    //   ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).addItems(products);
                    if (mConfig.isUserLogin()) {
                        parseService.saveListProduct(products).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(a -> {
                                    showToast("products has been saved!");
                                });
                        subscription = Observable.timer(3, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(a -> {
                                    progressDialog.dismiss();
                                    reloadActivity();

                                });
                    } else {
                        for (Product product : products) {
                            mProductModel.saveProduct(product);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!mDoubleBackToExitPressedOnce) {
                this.mDoubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> mDoubleBackToExitPressedOnce = false, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_log_in:
                if (mConfig.isUserLogin()) {
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                    ParseUser.logOutInBackground();
                }
                Intent i = new Intent(this, SignInActivity.class);
                startActivity(i);
                this.finish();
                break;
            case R.id.nav_generate:

                if (userId != null) {

                    Intent intentGenerate = new Intent(MainActivity.this, GenerateQR.class);
                    intentGenerate.putExtra(USER_ID_EXTRA, userId);
                    startActivity(intentGenerate);

                } else {
                    showToast("You must SignIn!");
                }
                break;
            case R.id.nav_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void showNetworkError() {
        if (mViewNetworkError == null) {
            mViewNetworkError = LayoutInflater.from(mContext).inflate(R.layout.view_network_error, null);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
            rootView.addView(mViewNetworkError, layoutParams);

            mViewNetworkError.setOnClickListener(v -> {
                mMainPresenter.getProductsNotCheckout("a");
                rootView.removeView(mViewNetworkError);
            });
        }
    }

    @Override
    public void showGeneralError(String message) {
        DialogFactory.createGenericErrorDialog(mContext, R.string.dialog_error_general_message).show();
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mProgressWheel.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressWheel.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProducts(List<Product> products) {

        productList.addAll(products);

        //  showToast(String.valueOf(products.size()));
        removeAdditionalViews();

        RecyclerView.Adapter adapter = new ProductRecyclerAdapter(MainActivity.this, new ArrayList<>());
        mRecyclerView.setAdapter(adapter);
       // mRecyclerView.setItemAnimator(new FeedItemAnimator());
        ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).addItems(products);


//        if (mRecyclerView.getAdapter() == null) {
//            RecyclerView.Adapter adapter = new ProductRecyclerAdapter(MainActivity.this, products);
//            mRecyclerView.setAdapter(adapter);
//        } else {
//            ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).addItems(products);
//        }
    }

    @Override
    public void showEmptyView() {
        if (mViewEmpty == null) {
            mViewEmpty = LayoutInflater.from(mContext).inflate(R.layout.view_empty_product, null);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
            rootView.addView(mViewEmpty, layoutParams);
        }
    }

    private void setupReCyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
       // mRecyclerView.setItemAnimator(new FeedItemAnimator());

    }

    private void setupNavigationView() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (drawerView.getWidth() * slideOffset);
                mRecyclerView.setTranslationX(moveFactor);
//                mLayoutFabContainer.setTranslationX(moveFactor);
                if (mViewEmpty != null) {
                    mViewEmpty.setTranslationX(moveFactor);
                }
                if (mViewNetworkError != null) {
                    mViewNetworkError.setTranslationX(moveFactor);
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        MenuItem navSignIn = mNavigationView.getMenu().findItem(R.id.nav_log_in);
        if (mConfig.isUserLogin()) {
            navSignIn.setTitle(R.string.menu_log_out);
        } else {
            navSignIn.setTitle(R.string.menu_login);
        }
    }

    private void tryRestoreLoginSession() {
        final ParseUser currentUser = mConfig.getCurrentUser();
        View headerView = mNavigationView.getHeaderView(0);

        if (mConfig.isUserLogin())
            userId = currentUser.getObjectId();

        headerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AppUtils.getStatusBarHeight(this) + AppUtils.getToolbarHeight(this)));
        DancingScriptTextView textUsername = (DancingScriptTextView) headerView.findViewById(R.id.text_nav_username);
        if (currentUser == null) {
            textUsername.setText("guest");
        } else {
            textUsername.setText(currentUser.getUsername());
        }
    }


    private void removeAdditionalViews() {
        if (mViewEmpty != null) {
            ((ViewGroup) getWindow().getDecorView().getRootView()).removeView(mViewEmpty);
        }
        if (mViewNetworkError != null) {
            ((ViewGroup) getWindow().getDecorView().getRootView()).removeView(mViewNetworkError);
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
                if (mConfig.isUserLogin()) {
                    parseService.deleteProduct(productList.get(currPos).getObjectId());
                    productList.remove(currPos);
                } else {
                    mProductModel.deleteProduct(productList.get(currPos));
                    productList.remove(currPos);
                }
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

    }

    @Override
    public void onSave(String name) {
        if (mConfig.isUserLogin()) {
            parseService.saveListProductNoCheckout(productList, generateListId).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(a -> {
                productList.clear();
                showToast("Generator qr-code has saved to server!");
            });
            ProductHistory productHistory = new ProductHistory();
            productHistory.setListId(generateListId);
            productHistory.setName(name);
            parseService.saveProductHistory(productHistory);

            DialogQrcodeHistory qrcodeHistory = new DialogQrcodeHistory(mContext, generateListId);
            qrcodeHistory.show();

        } else {
            mProductModel.saveListProductNoCheckout(generateListId).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(a -> {
                productList.clear();
                showToast("Generator qr-code has saved to database!");
            });
            ProductHistory productHistory = new ProductHistory();
            productHistory.setListId(generateListId);
            productHistory.setName(name);
            mProductModel.saveProductHistory(productHistory);

//            DialogQrcodeHistory qrcodeHistory=new DialogQrcodeHistory(mContext,generateListId);
//            qrcodeHistory.show();
        }
        ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).deleteAll();
    }


    public interface ClickListener {
        void onClick(View iew, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

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
