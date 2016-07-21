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
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivityMainBinding;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.scan.camera.CameraActivity;
import com.whooo.babr.view.session.signin.SignInActivity;
import com.whooo.babr.vo.Cart;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.subjects.PublishSubject;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FloatingActionButton mFabScan;
    private Toolbar mToolbar;
    private PublishSubject<Integer> mNavSubject = PublishSubject.create();

    @Inject
    ShopContract.Presenter mPresenter;

    private boolean mDoubleBackToExitPressedOnce = false;
    private MaterialSearchView mSearchView;
    private Context mContext;
    private FrameLayout mLayContent;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = this;


        mToolbar = binding.appBarMainView.viewToolbar.toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationView = binding.navView;
        mDrawerLayout = binding.drawerLayout;
        mLayContent = binding.appBarMainView.layoutContent;
        mFabScan = binding.appBarMainView.fabScan;
        mSearchView = binding.appBarMainView.viewToolbar.searchView;

        setupFab();
        setupNavigationView();
        mNavSubject.onNext(R.id.nav_action_shop);
        mNavSubject
                .distinctUntilChanged()
                .delay(350, TimeUnit.MILLISECONDS)
                .subscribe(id -> {
                    Fragment fragment = null;
                    switch (id) {
                        case R.id.nav_action_shop:
                            fragment = ShopFragment.createInstance();
                            break;
                        case R.id.nav_action_pending_carts:
                            break;
                        case R.id.nav_action_history:
                            break;
                        default:
                            break;
                    }

                    if (fragment != null) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.layout_content, fragment).commitAllowingStateLoss();
                    }
                });
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

                            startActivity(new Intent(mContext, CameraActivity.class));
                        } else {
                            Toast.makeText(mContext, "You must allow to use camera to access this function", Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }

    private void setupSearchView() {
//        openSearch();
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
    }


    private void performCheckout(Cart cart) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_checkout, null);
        TextInputEditText inputCartName = (TextInputEditText) dialogView.findViewById(R.id.input_cart_name);
        inputCartName.setText(cart.timestamp);
        AlertDialog dialogCheckout = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton(R.string.dialog_action_ok, ((dialog, which) -> {
                    String cartName = inputCartName.getText().toString();
                    if (TextUtils.isEmpty(cartName)) {
                        DialogFactory.createGenericErrorDialog(mContext, "You have to input cart name.").show();
                    } else {
                        inputCartName.setError(null);
                        dialog.dismiss();
                        cart.name = cartName;
                        mPresenter.checkout(cart);
                    }
                })).setNegativeButton(R.string.dialog_action_cancel, ((dialog, which1) -> {
                    dialog.dismiss();
                }))
                .setNeutralButton("Save for later", (dialog, which) -> {

                }).create();
        dialogCheckout.show();
    }

//    public void openSearch() {
//        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchListenerObserver(query);
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchListenerObserver(newText);
//
//                return false;
//            }
//        });
//    }

//    private void searchListenerObserver(String query) {
//        Subscription mSubscription = Observable.just(query)
//                .debounce(400, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(s -> {
//                    if (mRecyclerView.getAdapter() != null) {
////                        mAdapter.filter(s);
//                    }
//                });
//    }


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
            case R.id.nav_action_shop:
            case R.id.nav_action_pending_carts:
            case R.id.nav_action_history:
                mNavSubject.onNext(item.getItemId());
                break;
            case R.id.nav_action_login:
                Intent i = new Intent(this, SignInActivity.class);
                startActivity(i);
                this.finish();
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupNavigationView() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (drawerView.getWidth() * slideOffset);
                mLayContent.setTranslationX(moveFactor);
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
    }
}
