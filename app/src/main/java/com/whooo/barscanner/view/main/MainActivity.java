package com.whooo.barscanner.view.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.whooo.barscanner.R;
import com.whooo.barscanner.config.Config;
import com.whooo.barscanner.util.AppUtils;
import com.whooo.barscanner.view.AppIntroActivity;
import com.whooo.barscanner.view.base.ToolbarActivity;
import com.whooo.barscanner.view.product.BarViewRecyclerAdapter;
import com.whooo.barscanner.view.scan.CameraActivity;
import com.whooo.barscanner.view.scan.CameraFragment;
import com.whooo.barscanner.view.signin.SignInActivity;
import com.whooo.barscanner.view.widget.DancingScriptTextView;
import com.whooo.barscanner.vo.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends ToolbarActivity implements NavigationView.OnNavigationItemSelectedListener, MainView {

    private static final int REQUEST_QR_CODE = 1;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;

    @Inject
    Config mConfig;
    @Inject
    MainPresenter mMainPresenter;

    private static List<Product> products = new ArrayList<>();

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
//                                startActivityForResult(intent, REQUEST_QR_CODE);
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
        if (requestCode == REQUEST_QR_CODE && resultCode == RESULT_OK) {
            Product product = data.getParcelableExtra(CameraFragment.EXTRA_DATA);
            products.add(product);
            ((BarViewRecyclerAdapter) mRecyclerView.getAdapter()).addItem(product);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    }

    @Override
    public void showGeneralError(String message) {

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
        RecyclerView.Adapter adapter = new BarViewRecyclerAdapter(MainActivity.this, products);
        mRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.fab)
    public void onFabButtonClick() {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivityForResult(intent, REQUEST_QR_CODE);
    }

    private void setupReCyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupNavigationView() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (drawerView.getWidth() * slideOffset);
                mRecyclerView.setTranslationX(moveFactor);
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
}
