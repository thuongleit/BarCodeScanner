package com.whooo.babr.view.scan.camera;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivityCameraBinding;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;

public class CameraActivity extends BaseActivity {

    public static final String EXTRA_CARD_ID = "EXTRA_CARD_ID";
    private String mCardId;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCameraBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mCardId = getIntent().getStringExtra(EXTRA_CARD_ID);
        } else {
            mCardId = savedInstanceState.getString(EXTRA_CARD_ID);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_content);
        if (fragment == null) {
            fragment = FullScannerFragment.newInstance(mCardId);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_content, fragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_CARD_ID, mCardId);
    }
}
