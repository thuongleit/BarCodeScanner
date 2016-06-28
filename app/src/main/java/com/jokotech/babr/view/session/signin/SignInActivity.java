package com.jokotech.babr.view.session.signin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.jokotech.babr.R;
import com.jokotech.babr.config.Config;
import com.jokotech.babr.di.ActivityScope;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.session.signup.SignUpPresenter;

import javax.inject.Inject;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity {

    @Inject
    SignInPresenter mSignInPresenter;
    @Inject
    SignUpPresenter mSignUpPresenter;
    @Inject
    @ActivityScope
    Context mContext;
    @Inject
    Config mConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_main);
    }
}