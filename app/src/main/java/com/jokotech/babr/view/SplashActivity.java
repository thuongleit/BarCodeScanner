package com.jokotech.babr.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jokotech.babr.R;
import com.jokotech.babr.util.dialog.DialogFactory;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.main.MainActivity;
import com.jokotech.babr.view.signin.SignInActivity;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by thuongle on 12/22/15.
 */
public class SplashActivity extends BaseActivity {

    private Context mContext;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        ImageView imgBgr = (ImageView) findViewById(R.id.image_bgr);
        getApp().getAppComponent().picasso()
                .load(R.drawable.bgr)
                .priority(Picasso.Priority.LOW)
                .into(imgBgr);

        mSubscription = Observable
                .timer(1, TimeUnit.SECONDS)
                .doOnError(e -> {
                    Timber.e(e.getMessage(), "Cannot start app");
                    DialogFactory.createGenericErrorDialog(mContext, R.string.error_cannot_start_app).show();
                })
                .subscribe(a -> {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
