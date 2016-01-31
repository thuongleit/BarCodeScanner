package com.whooo.barscanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;
import com.whooo.barscanner.view.main.MainActivity;
import com.whooo.barscanner.view.signin.SignInActivity;

/**
 * Created by thuongle on 12/22/15.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if (ParseUser.getCurrentUser() != null) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, SignInActivity.class);
        }

        startActivity(intent);
        this.finish();
    }
}
