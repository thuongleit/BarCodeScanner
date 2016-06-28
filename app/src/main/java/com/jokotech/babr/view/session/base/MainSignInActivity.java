package com.jokotech.babr.view.session.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jokotech.babr.R;
import com.jokotech.babr.view.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by thuongle on 6/28/16.
 */

public class MainSignInActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_main);
        ButterKnife.bind(this);
    }
}
