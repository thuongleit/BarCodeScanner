package com.jokotech.babr.view.session.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jokotech.babr.R;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.view.main.MainActivity;
import com.jokotech.babr.view.session.base.MainSignInActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashContract.View {

    @Inject
    SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DaggerSplashComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onUserHasSignedIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserNotSignedIn() {
        Intent intent = new Intent(this, MainSignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showNetworkError() {
        showNetworkErrorDialog();
    }

    @Override
    public void showInAppError() {
        showInAppErrorDialog();
    }
}
