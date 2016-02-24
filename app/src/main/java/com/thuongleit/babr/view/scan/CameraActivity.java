package com.thuongleit.babr.view.scan;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.thuongleit.babr.R;
import com.thuongleit.babr.view.base.ToolbarActivity;

public class CameraActivity extends ToolbarActivity {

    public static final String EXTRA_SERVICE = "CameraActivity.EXTRA_SERVICE";
    private String mService;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);

        mService = getIntent().getStringExtra(EXTRA_SERVICE);
    }

    public String getService() {
        return mService;
    }
}
