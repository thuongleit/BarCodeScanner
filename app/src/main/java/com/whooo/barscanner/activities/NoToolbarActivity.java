package com.whooo.barscanner.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.whooo.barscanner.BarApplication;
import com.whooo.barscanner.injectors.components.ApplicationComponent;
import com.whooo.barscanner.injectors.modules.ActivityModule;

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

    public void startNewActivity(Class<? extends Activity> clazz) {
        startNewActivity(clazz, null);
    }

    private void startNewActivity(Class<? extends Activity> clazz, Bundle arg) {
        Intent intent = new Intent(this, clazz);
        if (arg != null) {
            intent.putExtras(arg);
        }
        startActivity(intent);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((BarApplication) getApplication()).getApplicationComponent();
    }

    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
