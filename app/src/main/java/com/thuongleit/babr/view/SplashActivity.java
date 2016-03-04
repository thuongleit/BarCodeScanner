package com.thuongleit.babr.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;
import com.thuongleit.babr.view.main.MainActivity;
import com.thuongleit.babr.view.signin.SignInActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by thuongle on 12/22/15.
 */
public class SplashActivity extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Observable.timer(2, TimeUnit.SECONDS).subscribe(a->{
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
