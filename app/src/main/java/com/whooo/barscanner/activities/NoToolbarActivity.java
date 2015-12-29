package com.whooo.barscanner.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.whooo.barscanner.BarApplication;
import com.whooo.barscanner.injectors.components.ApplicationComponent;

import butterknife.ButterKnife;

/**
 * Created by thuongle on 12/28/15.
 */
public abstract class NoToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        initializeInjectors();
        initializeVariables(savedInstanceState);
        setupViews();
    }

    protected abstract int getLayoutId();

    protected void initializeInjectors() {
    }

    protected void initializeVariables(Bundle savedInstanceState) {
    }

    protected void setupViews() {
    }

    public ApplicationComponent getApplicationComponent() {
        return ((BarApplication) getApplication()).getApplicationComponent();
    }
}
