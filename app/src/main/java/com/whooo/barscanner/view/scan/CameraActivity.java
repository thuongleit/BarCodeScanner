package com.whooo.barscanner.view.scan;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whooo.barscanner.R;
import com.whooo.barscanner.view.base.ToolbarActivity;

public class CameraActivity extends ToolbarActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
    }
}
