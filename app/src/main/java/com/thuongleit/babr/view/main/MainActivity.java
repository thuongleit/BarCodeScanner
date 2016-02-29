package com.thuongleit.babr.view.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.thuongleit.babr.R;
import com.thuongleit.babr.config.Config;
import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.data.remote.amazon.model.AmazonProductResponse;
import com.thuongleit.babr.di.ActivityScope;
import com.thuongleit.babr.util.AppUtils;
import com.thuongleit.babr.util.DialogFactory;
import com.thuongleit.babr.view.AppIntroActivity;
import com.thuongleit.babr.view.base.ToolbarActivity;
import com.thuongleit.babr.view.product.ProductRecyclerAdapter;
import com.thuongleit.babr.view.scan.CameraActivity;
import com.thuongleit.babr.view.signin.SignInActivity;
import com.thuongleit.babr.view.widget.DancingScriptTextView;
import com.thuongleit.babr.view.widget.DividerItemDecoration;
import com.thuongleit.babr.vo.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends ToolbarActivity implements NavigationView.OnNavigationItemSelectedListener, MainView {

    private static final int REQUEST_CAMERA = 1;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;
    @Bind(R.id.layout_fab_container)
    FrameLayout mLayoutFabContainer;
    @Bind(R.id.fab_menu)
    FloatingActionsMenu mFabMenu;
    @Bind(R.id.fab_amazon_select)
    FloatingActionButton mFabAmazon;
    @Bind(R.id.fab_upcitemdb_select)
    FloatingActionButton mFabUpcItemDb;

    @Inject
    Config mConfig;
    @Inject
    MainPresenter mMainPresenter;
    @Inject
    @ActivityScope
    Context mContext;

    private boolean mDoubleBackToExitPressedOnce = false;
    private View mViewNetworkError;
    private View mViewEmpty;

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
        setupFabMenu();

        //try restore login session
        tryRestoreLoginSession();
        mMainPresenter.getProducts();
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
        //implement search view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem menuItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String query) {
//                    new GetBarCodeAsyncTask(new GetBarCodeAsyncTask.OnUpdateUICallback() {
//                        @Override
//                        public void onUpdateUI(Observable<Product> product) {
//                            if (product != null) {
//                                Intent intent = new Intent(MainActivity.this, BarViewActivity.class);
//                                intent.putExtra("data", Parcels.wrap(product));
//                                startActivityForResult(intent, REQUEST_CAMERA);
//                            } else {
//                                buildFailedDialog(String.format("Number %s was incorrect or invalid, either the length or the the check digit may have been incorrect.", query)).show();
//                            }
//                        }
//                    }).execute("http://www.upcitemdb.com/upc/" + query);
//                    searchView.onActionViewCollapsed();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            ArrayList<Product> products = data.getParcelableArrayListExtra(CameraActivity.EXTRA_DATA);
            removeAdditionalViews();
            if (mRecyclerView.getAdapter() == null) {
                RecyclerView.Adapter adapter = new ProductRecyclerAdapter(mContext, products);
                mRecyclerView.setAdapter(adapter);
            } else {
                ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).addItems(products);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mFabMenu.isExpanded()) {
            mFabMenu.collapse();
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
                mMainPresenter.getProducts();
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
        removeAdditionalViews();
        if (mRecyclerView.getAdapter() == null) {
            RecyclerView.Adapter adapter = new ProductRecyclerAdapter(MainActivity.this, products);
            mRecyclerView.setAdapter(adapter);
        } else {
            ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).addItems(products);
        }
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
    }

    private void setupNavigationView() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (drawerView.getWidth() * slideOffset);
                mRecyclerView.setTranslationX(moveFactor);
                mLayoutFabContainer.setTranslationX(moveFactor);
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

        headerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AppUtils.getStatusBarHeight(this) + AppUtils.getToolbarHeight(this)));
        DancingScriptTextView textUsername = (DancingScriptTextView) headerView.findViewById(R.id.text_nav_username);
        if (currentUser == null) {
            textUsername.setText("guest");
        } else {
            textUsername.setText(currentUser.getUsername());
        }
    }

    private void setupFabMenu() {
        mLayoutFabContainer.getBackground().setAlpha(0);
        mFabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mLayoutFabContainer.getBackground().setAlpha(240);
                mLayoutFabContainer.setOnTouchListener((v, event) -> {
                    mFabMenu.collapse();
                    return true;
                });
            }

            @Override
            public void onMenuCollapsed() {
                mLayoutFabContainer.getBackground().setAlpha(0);
                mLayoutFabContainer.setOnTouchListener(null);
            }
        });
        mFabAmazon.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CameraActivity.class);
            intent.putExtra(CameraActivity.EXTRA_SERVICE, Constant.KEY_AMAZON_SERVICE);
            startActivityForResult(intent, REQUEST_CAMERA);
            mFabMenu.collapse();

        });
        mFabUpcItemDb.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CameraActivity.class);
            intent.putExtra(CameraActivity.EXTRA_SERVICE, Constant.KEY_UPC_SERVICE);
            startActivityForResult(intent, REQUEST_CAMERA);
            mFabMenu.collapse();
        });
    }

    private void removeAdditionalViews() {
        if (mViewEmpty != null) {
            ((ViewGroup) getWindow().getDecorView().getRootView()).removeView(mViewEmpty);
        }
        if (mViewNetworkError != null) {
            ((ViewGroup) getWindow().getDecorView().getRootView()).removeView(mViewNetworkError);
        }
    }
}
