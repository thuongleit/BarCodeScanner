package com.example.barscanner;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.example.barscanner.adapter.BarViewRecyclerAdapter;
import com.example.barscanner.database.SQLHelper;
import com.example.barscanner.model.BarCode;
import com.example.barscanner.net.GetBarCodeAsyncTask;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int QR_CODE = 1001;
    private static List<BarCode> barCodes = new ArrayList<>();

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    private SQLHelper sqlHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        setupReCyclerView();
        setupNavigationView();

        //try restore login session
        tryRestoreLoginSession();
    }

    private void tryRestoreLoginSession() {
        ParseUser currentUser = ParseUser.getCurrentUser();
//        MenuItem accountMenu = mNavigationView.getMenu().findItem(R.id.nav_account);
//        if (currentUser == null) {
//            accountMenu.setTitle("Login");
//        } else {
//            accountMenu.setTitle("Logout");
//            //username textview
//            TextView usernameView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_text_username);
//            usernameView.setText(currentUser.getUsername());
//
//            TextView emailView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_text_email);
//            emailView.setText(currentUser.getEmail());
//        }
    }

    private void setupNavigationView() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
            try {
                sqlHelper = new SQLHelper(this);
                barCodes = sqlHelper.getBarCodeDao().queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            RecyclerView.Adapter adapter = new BarViewRecyclerAdapter(MainActivity.this, barCodes);
            mRecyclerView.setAdapter(adapter);

        } else {

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Products").whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects != null)
                        for (ParseObject object : objects) {
                            String image = object.getString("image");
                            String upcA = object.getString("upcA");
                            String ean = object.getString("ean");
                            String country = object.getString("country");
                            String manufacture = object.getString("manufacture");
                            String model = object.getString("model");
                            Number quantity = object.getNumber("quantity");

                            BarCode barCode = new BarCode();
                            barCode.image = image;
                            barCode.upcA = upcA;
                            barCode.ean = ean;
                            barCode.country = country;
                            barCode.manufacture = manufacture;
                            barCode.model = model;

                            barCodes.add(barCode);
                        }
                    RecyclerView.Adapter adapter = new BarViewRecyclerAdapter(MainActivity.this, barCodes);
                    mRecyclerView.setAdapter(adapter);
                }
            });
        }
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
                    new GetBarCodeAsyncTask(new GetBarCodeAsyncTask.OnUpdateUICallback() {
                        @Override
                        public void onUpdateUI(BarCode barCode) {
                            if (barCode != null) {
                                Intent intent = new Intent(MainActivity.this, BarViewActivity.class);
                                intent.putExtra("data", Parcels.wrap(barCode));
                                startActivityForResult(intent, QR_CODE);
                            } else {
                                buildFailedDialog(String.format("Number %s was incorrect or invalid, either the length or the the check digit may have been incorrect.", query)).show();
                            }
                        }
                    }).execute("http://www.upcitemdb.com/upc/" + query);
                    searchView.onActionViewCollapsed();
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
            Parcelable parcelableExtra = data.getParcelableExtra("data");
            BarCode barCode = Parcels.unwrap(parcelableExtra);
            barCodes.add(barCode);
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
//                    Intent i = new Intent(this, LoginActivity.class);
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
