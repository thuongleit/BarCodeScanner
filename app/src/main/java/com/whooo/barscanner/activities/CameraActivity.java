package com.whooo.barscanner.activities;

import com.whooo.barscanner.R;

public class CameraActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
    }
}
