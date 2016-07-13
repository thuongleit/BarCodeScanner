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

import com.google.firebase.database.DatabaseReference;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivityMainBinding;
import com.whooo.babr.util.AppUtils;
import com.whooo.babr.util.FirebaseUtils;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.util.dialog.DialogQrcodeHistory;
import com.whooo.babr.util.swipe.ItemTouchHelperAdapter;
import com.whooo.babr.util.swipe.ItemTouchHelperCallback;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.history.HistoryActivity;
import com.whooo.babr.view.product.ProductRecyclerAdapter;
import com.whooo.babr.view.scan.CameraActivity;
import com.whooo.babr.view.session.signin.SignInActivity;
import com.whooo.babr.view.widget.DividerItemDecoration;
import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View,
        ActionMode.Callback, ProductRecyclerAdapter.SwipeProductListener {

    private static final int REQUEST_CAMERA = 1;
    public static final String USER_ID_EXTRA = "user_id_extra";

    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SearchBox searchBox;
    private FloatingActionButton mFabScan;
    private Toolbar mToolbar;

    @Inject
    MainContract.Presenter mPresenter;

    private Context mContext;

    private boolean mDoubleBackToExitPressedOnce = false;
    private List<Product> mProducts = new ArrayList<>();
    private String mGenerateListId;
    private ItemTouchHelper mItemTouchHelper;

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
        binding.setPresenter(mPresenter);
        binding.setViewmodel(mPresenter.getViewModel());

        injectViews(binding);
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
        mToolbar = binding.appBarMainView.toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationView = binding.navView;
        mDrawerLayout = binding.drawerLayout;
        mRecyclerView = binding.appBarMainView.recyclerView;
        mFabScan = binding.appBarMainView.fabScan;

        //set up views
        setupNavigationView();
        setupRecyclerView();
        setupFab();
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
        mGenerateListId = AppUtils.generateString(new Random(), "1254789dhfoendlf89ssofnd8965412312sda2wdsds212asdsa21dad", 20);

//        parseServiceOK.saveListProductNoCheckout(mProducts, mGenerateListId).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(a -> {
//            mProducts.clear();
//            showToast("Generator qr-code has saved to server!");
//        });
        CheckoutHistory checkoutHistory = new CheckoutHistory();
        checkoutHistory.listId = mGenerateListId;
        checkoutHistory.name = AppUtils.gerenateDateFormat();
        checkoutHistory.size = mProducts.size();

        mPresenter.saveProductsHistory(checkoutHistory, mProducts);
        mProducts.clear();
        showToast("Generator qr-code has saved to server!");

        DialogQrcodeHistory qrcodeHistory = new DialogQrcodeHistory(mContext, mGenerateListId);
        qrcodeHistory.show();

        ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).deleteAll();
    }


    public void openSearch() {
        mToolbar.setTitle("");
        mToolbar.setVisibility(View.GONE);
        searchBox.revealFromMenuItem(R.id.action_search, this);
        searchBox.setMenuListener(() -> {

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

//                subscription = Observable.just(term)
//                        .debounce(400, TimeUnit.SECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(s -> {
//                            if (mRecyclerView.getAdapter() != null) {
//                                ((ProductRecyclerAdapter) mRecyclerView.getAdapter()).filter(s);
//                            }
//                        });

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

                DatabaseReference productRef = FirebaseUtils.getProductsRef();
                for (Product p : products) {
                    String newProductKey = productRef.push().getKey();
                    p.objectId = newProductKey;
                }
                mPresenter.saveProducts(products);
                setupWithItemTouch();
            }
        }
    }

    private void setupWithItemTouch() {
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback((ItemTouchHelperAdapter) mRecyclerView.getAdapter(), this);
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

//                if (userId != null) {
//
//                    Intent intentGenerate = new Intent(MainActivity.this, GenerateQR.class);
//                    intentGenerate.putExtra(USER_ID_EXTRA, userId);
//                    startActivity(intentGenerate);
//
//                } else {
//                    showToast("You must SignIn!");
//                }
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
                mProducts.remove(currPos);
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
    public void onSaveProductsSuccess() {
        showToast("onSaveProductsSuccess");
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
    public void onSwipeProduct(int position, Product product) {
        mPresenter.removeProducts(product);

//        DatabaseReference userRef = FirebaseUtils.getUserProductsRef();
//        DatabaseReference productRef = FirebaseUtils.getProductsRef();
//        userRef.child(product.objectId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    userRef.child(product.objectId).removeValue();
//                    productRef.child(product.objectId).removeValue();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
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
