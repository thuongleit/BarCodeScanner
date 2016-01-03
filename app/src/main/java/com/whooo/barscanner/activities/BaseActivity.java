package com.whooo.barscanner.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.whooo.barscanner.R;


/**
 * Created by thuongle on 12/23/15.
 */
public abstract class BaseActivity extends NoToolbarActivity {
    protected ActionBar mSupportActionBar;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mSupportActionBar = getSupportActionBar();
        mSupportActionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        setupToolbar();
    }
}
