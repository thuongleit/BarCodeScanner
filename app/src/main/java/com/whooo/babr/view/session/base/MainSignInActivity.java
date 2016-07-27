package com.whooo.babr.view.session.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.whooo.babr.R;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.main.MainActivity;
import com.whooo.babr.view.session.signin.SignInActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class MainSignInActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, SignInBaseContract.View, View.OnClickListener {

    private static final int REQUEST_GOOGLE_SIGN_IN = 1;
    private static final int REQUEST_EMAIL_SIGN_IN = 2;

    private LoginButton mFbOriginalBtn;
    private ImageButton mBtnFbSignIn;
    private ImageButton mBtnGoogleSignIn;
    private Button mBtnAnonymousSignIn;

    private AlertDialog mProgressDialog;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    @Inject
    SignInBaseContract.Presenter mPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_main);

        initInjector();
        initViews();
        initializeSocialAuth();
        initializeFacebookAuth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_GOOGLE_SIGN_IN) {
            mPresenter.signInWithGoogle(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        } else if (requestCode == REQUEST_EMAIL_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                onSignInSuccess();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_facebook_sign_in:
                mFbOriginalBtn.performClick();
                break;
            case R.id.button_google_sign_in:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGN_IN);
                break;
            case R.id.button_email_sign_in:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivityForResult(intent, REQUEST_EMAIL_SIGN_IN);
                break;
            case R.id.button_sign_in_anonymous:
                mPresenter.signInAnonymous();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("onConnectionFailed: %s", connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "You're in!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSignInFailed(String message) {
        logoutFacebook();
        DialogFactory.createSimpleNoTitleDialog(this, message).show();
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory
                    .createProgressDialog(this);
            mProgressDialog.setOnCancelListener(dialog -> {
                dialog.dismiss();
                setButtonFbEnable(true);
                setButtonGoogleEnable(true);
                setButtonSignInAnonymousEnable(true);
            });
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setButtonSignInAnonymousEnable(boolean enabled) {
        mBtnAnonymousSignIn.setEnabled(enabled);
    }

    @Override
    public void setButtonFbEnable(boolean enabled) {
        mBtnFbSignIn.setEnabled(enabled);
    }

    @Override
    public void setButtonGoogleEnable(boolean enabled) {
        mBtnGoogleSignIn.setEnabled(enabled);
    }

    @Override
    public void showNetworkError() {
        logoutFacebook();
        showNetworkErrorDialog();
    }

    @Override
    public void showInAppError() {
        logoutFacebook();
        showInAppErrorDialog();
    }

    private void initViews() {
        mFbOriginalBtn = (LoginButton) findViewById(R.id.fb_original_btn);
        mBtnFbSignIn = (ImageButton) findViewById(R.id.button_facebook_sign_in);
        mBtnGoogleSignIn = (ImageButton) findViewById(R.id.button_google_sign_in);
        mBtnAnonymousSignIn = (Button) findViewById(R.id.button_sign_in_anonymous);

        mFbOriginalBtn.setOnClickListener(this);
        mBtnFbSignIn.setOnClickListener(this);
        mBtnGoogleSignIn.setOnClickListener(this);
        mBtnAnonymousSignIn.setOnClickListener(this);
        findViewById(R.id.button_email_sign_in).setOnClickListener(this);
    }

    private void initInjector() {
        DaggerSignInBaseComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .signInBaseModule(new SignInBaseModule(this))
                .build()
                .inject(this);
    }

    private void initializeSocialAuth() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("277691162338-birrp9k8249msj6btlgr2cgp5i7u97of.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
    }

    private void initializeFacebookAuth() {
        mFbOriginalBtn.setReadPermissions("email", "public_profile");
        mFbOriginalBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("facebook:onSuccess: %s", loginResult);
                mPresenter.signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                onSignInFailed(error.getMessage());
            }
        });
    }

    private void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }
}
