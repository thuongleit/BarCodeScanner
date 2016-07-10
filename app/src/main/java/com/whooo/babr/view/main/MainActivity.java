package com.whooo.babr.view.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.whooo.babr.R;
import com.whooo.babr.data.remote.ParseServiceOK;
import com.whooo.babr.databinding.ActivityMainBinding;
import com.whooo.babr.util.AppUtils;
import com.whooo.babr.util.dialog.DialogQrcodeHistory;
import com.whooo.babr.util.swipe.ItemTouchHelperCallback;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.history.HistoryActivity;
import com.whooo.babr.view.product.ProductRecyclerAdapter;
import com.whooo.babr.view.qrgenerate.GenerateQR;
import com.whooo.babr.view.scan.CameraActivity;
import com.whooo.babr.view.session.signin.SignInActivity;
import com.whooo.babr.view.widget.DividerItemDecoration;
import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.zip.DeflaterOutputStream;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View,
        ActionMode.Callback {

    private static final int REQUEST_CAMERA = 1;
    public static final String USER_ID_EXTRA = "user_id_extra";

    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ProgressWheel mProgressWheel;
    private SearchBox searchBox;
    private FloatingActionButton mFabScan;
    private Toolbar mToolbar;

    @Inject
    MainContract.Presenter mPresenter;
    private Context mContext;

    private boolean mDoubleBackToExitPressedOnce = false;
    private List<Product> mProducts = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String mGenerateListId;
    private ParseServiceOK parseServiceOK;
    private Subscription subscription;
    private Integer userId;
    private DeflaterOutputStream actionMode;
    private ItemTouchHelper mItemTouchHelper;
    private ProductRecyclerAdapter mAdapter;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = this;

        initializeInjector();
        injectViews(binding);

        setupNavigationView();
        setupReCyclerView();

        mFabScan.setOnClickListener(v -> {
            // Must be done during an initialization phase like onCreate
            RxPermissions.getInstance(this)
                    .request(Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) { // Always true pre-M
                            int[] startingLocation = new int[2];
                            v.getLocationOnScreen(startingLocation);
                            startingLocation[0] += v.getWidth() / 2;

                            Intent intent = new Intent(mContext, CameraActivity.class);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else {
                            showToast("You must allow to use camera to access this function");
                        }
                    });

        });
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private void injectViews(ActivityMainBinding binding) {
        mToolbar = binding.appBarMainView.toolbar;
        mNavigationView = binding.navView;
        mDrawerLayout = binding.drawerLayout;
        mRecyclerView = binding.appBarMainView.recyclerView;
        mProgressWheel = binding.appBarMainView.progressWheel;
        mFabScan = binding.appBarMainView.fabScan;

        binding.setPresenter(mPresenter);
    }

    private void initializeInjector() {
        DaggerMainComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            openSearch();
            return true;
        }

        if (id == R.id.action_camera) {


            if (mProducts.size() >= 1) {

                new AlertDialog.Builder(this, R.style.MyAlertDialogAppCompatStyle)
                        .setMessage(getResources().getString(R.string.save_to_history))
                        .setTitle("Option")
                        .setNegativeButton("OK", ((dialog, which) -> {
                            saveProductToHistory();
                        })).setPositiveButton("Cancle", ((dialog1, which1) -> {
                    dialog1.dismiss();
                })).show();


            } else {
                showToast("You don't have any item!");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveProductToHistory() {
        mGenerateListId = AppUtils.generateString(new Random(), "1254789dhfoendlf89ssofnd896541", 20);

        parseServiceOK.saveListProductNoCheckout(mProducts, mGenerateListId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(a -> {
            mProducts.clear();
            showToast("Generator qr-code has saved to server!");
        });
        CheckoutHistory checkoutHistory = new CheckoutHistory();
        checkoutHistory.listId = mGenerateListId;
        checkoutHistory.name = AppUtils.gerenateDateFormat();
        checkoutHistory.size = mProducts.size();
        parseServiceOK.saveProductHistory(checkoutHistory);

        DialogQrcodeHistory qrcodeHistory = new DialogQrcodeHistory(mContext, mGenerateListId);
        qrcodeHistory.show();

        ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).deleteAll();
    }


    public void openSearch() {
        mToolbar.setTitle("");
        mToolbar.setVisibility(View.GONE);
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
        if (searchBox.getSearchText().isEmpty()) mToolbar.setTitle("");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            ArrayList<Product> products = data.getParcelableArrayListExtra(CameraActivity.EXTRA_DATA);
            if (products != null && !products.isEmpty()) {
             //   mProducts.addAll(products);
                mAdapter.addItems(products);
                setupWithItemTouch();

            }
        }
    }

    private void setupWithItemTouch() {
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter, this);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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


    public void showProgress(boolean show) {
        if (show) {
            mProgressWheel.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressWheel.setVisibility(View.GONE);
        }
    }

    public void showProducts(List<Product> products) {
        mProducts.addAll(products);
        mAdapter.addItems(products);
    }

    private void setupReCyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ProductRecyclerAdapter(this, mProducts);
        mRecyclerView.setAdapter(mAdapter);
        setupWithItemTouch();
    }


    private void setupNavigationView() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (drawerView.getWidth() * slideOffset);
                mRecyclerView.setTranslationX(moveFactor);
//                mLayoutFabContainer.setTranslationX(moveFactor);
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
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
                parseServiceOK.deleteProduct(mProducts.get(currPos).objectId);
                mProducts.remove(currPos);
                ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).deleteItem(currPos);
            }

            return true;

        }
        return false;

    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).clearSelections();
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showInAppError() {

    }
}
