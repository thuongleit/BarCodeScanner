package com.whooo.babr.view.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivityMainBinding;
import com.whooo.babr.util.AppUtils;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.util.dialog.DialogQrcodeHistory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.cart.HistoryActivity;
import com.whooo.babr.view.product.ProductRecyclerAdapter;
import com.whooo.babr.view.scan.camera.CameraActivity;
import com.whooo.babr.view.session.signin.SignInActivity;
import com.whooo.babr.view.widget.DividerItemDecoration;
import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private Subscription mSubscription;
    private FloatingActionButton mFabScan;
    private Toolbar mToolbar;

    @Inject
    MainContract.Presenter mPresenter;

    private Context mContext;

    private boolean mDoubleBackToExitPressedOnce = false;
    private FrameLayout mLayoutContent;
    private View mEmptyView;
    private MainViewModel mViewModel;
    private AlertDialog mProgressDialog;
    private MaterialSearchView mSearchView;

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

        DataBindingUtil.setDefaultComponent(getApp().getAppComponent());
        injectViews(binding);

        binding.setPresenter(mPresenter);
        mViewModel = mPresenter.getViewModel();
        binding.setViewmodel(mViewModel);
    }

    private void setupFab() {
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
        mToolbar = binding.appBarMainView.viewToolbar.toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationView = binding.navView;
        mDrawerLayout = binding.drawerLayout;
        mRecyclerView = binding.appBarMainView.recyclerView;
        mFabScan = binding.appBarMainView.fabScan;
        mLayoutContent = binding.appBarMainView.layoutContent;

        mSearchView = binding.appBarMainView.viewToolbar.searchView;

        //set up views
        setupNavigationView();
        setupRecyclerView();
        setupFab();
        setupSearchView();
    }

    private void setupSearchView() {
        openSearch();
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
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
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_checkout:
                if (mViewModel.data.isEmpty()) {
                    DialogFactory.createGenericErrorDialog(mContext, "You don't have any items!").show();
                } else {
                    Cart cart = new Cart();
                    cart.name = AppUtils.gerenateDateFormat();
                    cart.timestamp = cart.name;

                    AlertDialog dialogCheckout = new AlertDialog.Builder(this)
                            .setMessage("Checkout items as name " + cart.name + "?")
                            .setTitle("Checkout")
                            .setNegativeButton(R.string.dialog_action_ok, ((dialog, which) -> {
                                dialog.dismiss();
                                mPresenter.checkout(cart);
                            })).setPositiveButton(R.string.dialog_action_cancel, ((dialog1, which1) -> {
                                dialog1.dismiss();
                            })).create();
                    dialogCheckout.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSearch() {
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchListenerObserver(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchListenerObserver(newText);

                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    private void searchListenerObserver(String query) {
        mSubscription = Observable.just(query)
                .debounce(400, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    if (mRecyclerView.getAdapter() != null) {
//                        mAdapter.filter(s);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            ArrayList<Product> products = data.getParcelableArrayListExtra(CameraActivity.EXTRA_DATA);
            if (products != null && !products.isEmpty()) {
                mViewModel.setData(products);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
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

                break;
            case R.id.nav_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
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
//                mProducts.remove(currPos);
                ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).deleteItem(currPos);
            }

            return true;

        }
        return false;

    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).clearSelections();
    }

    @Override
    public void onCheckoutSuccess(String keyOfCart) {
        showToast("Checkout success!");

        DialogQrcodeHistory qrcodeHistory = new DialogQrcodeHistory(mContext, keyOfCart);
        qrcodeHistory.show();
    }

    @Override
    public void onRemoveProductsSuccess() {
        showToast("onRemoveProductsSuccess");
    }

    @Override
    public void requestFailed(String message) {
        DialogFactory.createGenericErrorDialog(mContext, message).show();
    }

    @Override
    public void onEmptyResponse() {
        if (mEmptyView == null) {
            mEmptyView = getLayoutInflater().inflate(R.layout.view_empty_product, null);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutContent.addView(mEmptyView, layoutParams);
    }

    @Override
    public void removeEmptyViewIfNeeded() {
        if (mEmptyView != null && !mPresenter.getViewModel().empty.get()) {
            mLayoutContent.removeView(mEmptyView);
        }
    }

    @Override
    public void addPendingRemove(int position, Product product) {
        final Snackbar snackbar = Snackbar.make(mLayoutContent, "Item deleted", Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(mContext, R.color.white))
                .setAction("Undo", view -> {
                    mPresenter.undoRemovedProduct(position, product);
                });
        snackbar.show();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            snackbar.dismiss();
            mPresenter.removeProducts(product);
        }, 2000);
    }

    @Override
    public void showStandaloneProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(mContext);
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void showNetworkError() {
        // TODO: 7/13/16 remove this method at the end. it is already handle in presenter
    }

    @Override
    public void showInAppError() {
        // TODO: 7/13/16 remove this method at the end. it is already handle in presenter
    }
}
