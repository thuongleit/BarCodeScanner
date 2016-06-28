package com.jokotech.babr.view.session.signup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jokotech.babr.R;
import com.jokotech.babr.databinding.ActivitySignUpBinding;
import com.jokotech.babr.util.ActivityUtils;
import com.jokotech.babr.view.base.BaseActivity;

import javax.inject.Inject;

public class SignUpActivity extends BaseActivity {

    @Inject
    SignUpPresenter mSignUpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySignUpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        setSupportActionBar(binding.toolbar);

        SignUpFragment fragment = (SignUpFragment) getSupportFragmentManager().findFragmentById(R.id.layout_content);
        if (fragment == null) {
            // Create the fragment
            fragment = SignUpFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.layout_content);
        }

        //inject dependency
    }
}