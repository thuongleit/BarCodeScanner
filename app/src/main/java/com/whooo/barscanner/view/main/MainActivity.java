package com.whooo.barscanner.view.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseUser;
import com.whooo.barscanner.R;
import com.whooo.barscanner.util.AppUtils;
import com.whooo.barscanner.view.AppIntroActivity;
import com.whooo.barscanner.view.product.BarViewRecyclerAdapter;
import com.whooo.barscanner.view.scan.CameraActivity;
import com.whooo.barscanner.view.scan.CameraFragment;
import com.whooo.barscanner.view.signin.SignInActivity;
import com.whooo.barscanner.vo.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int QR_CODE = 1001;
    private static List<Product> products = new ArrayList<>();

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean is_first_run = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("is_first_run", true);
        if (is_first_run) {
            startActivity(new Intent(this, AppIntroActivity.class));
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("is_first_run", true).apply();
        }
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        setupReCyclerView();
        setupNavigationView();

        //try restore login session
        tryRestoreLoginSession();
    }

    private void tryRestoreLoginSession() {
        final ParseUser currentUser = ParseUser.getCurrentUser();
        View headerView = mNavigationView.getHeaderView(0);
        headerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AppUtils.getStatusBarHeight(this) + AppUtils.getToolbarHeight(this)));
        ImageButton imageLogin = (ImageButton) mNavigationView.getHeaderView(0).findViewById(R.id.image_button_login);
        TextView textUsername = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_text_username);
        textUsername.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/KaushanScript-Regular.ttf"));
        if (currentUser == null) {
            imageLogin.setBackgroundResource(R.drawable.ic_login);
            imageLogin.setContentDescription("Login");
            textUsername.setText("guest");
        } else {
            imageLogin.setBackgroundResource(R.drawable.ic_logout);
            imageLogin.setContentDescription("Logout");
            //username textview
            textUsername.setText(currentUser.getUsername());
        }

        imageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    ParseUser.logOutInBackground();
                }
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                MainActivity.this.finish();
            }
        });
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
    }

    @OnClick(R.id.fab)
    public void onFabButtonClick() {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivityForResult(intent, QR_CODE);
    }

    private void setupReCyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (ParseUser.getCurrentUser() == null) {
//            try {
//                sqlHelper = new SQLHelper(this);
//                products = sqlHelper.getBarCodeDao().queryForAll();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            RecyclerView.Adapter adapter = new BarViewRecyclerAdapter(MainActivity.this, products);
            mRecyclerView.setAdapter(adapter);

        } else {
//            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Products").whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> objects, ParseException e) {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upcA = object.getString("upcA");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.image = image;
//                            product.upcA = upcA;
//                            product.ean = ean;
//                            product.country = country;
//                            product.manufacture = manufacture;
//                            product.model = model;
//
//                            products.add(product);
//                        }
//                    RecyclerView.Adapter adapter = new BarViewRecyclerAdapter(MainActivity.this, products);
//                    mRecyclerView.setAdapter(adapter);
                }
//            });
//        }
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
//                                startActivityForResult(intent, QR_CODE);
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
        if (requestCode == QR_CODE && resultCode == RESULT_OK) {
            Product product = data.getParcelableExtra(CameraFragment.EXTRA_DATA);
            products.add(product);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @NonNull
    private AlertDialog.Builder buildFailedDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error").setMessage(message);
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        return builder;
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
//            case R.id.nav_account:
//                if (ParseUser.getCurrentUser() == null) {
//                    Intent i = new Intent(this, SignInActivity.class);
//                    startActivity(i);
//                    this.finish();
//                } else {
//                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
//                    ParseUser.logOutInBackground();
//                    this.finish();
//                    startActivity(getIntent());
//                }
//                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
