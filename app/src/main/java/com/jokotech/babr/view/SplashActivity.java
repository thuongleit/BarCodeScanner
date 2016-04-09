package com.jokotech.babr.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.parse.ParseUser;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.main.MainActivity;
import com.jokotech.babr.view.signin.SignInActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by thuongle on 12/22/15.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        Observable.timer(1, TimeUnit.SECONDS).subscribe(a -> {
            Intent intent;
            if (ParseUser.getCurrentUser() != null) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, SignInActivity.class);
            }

            startActivity(intent);
            this.finish();
        });
    }
}
