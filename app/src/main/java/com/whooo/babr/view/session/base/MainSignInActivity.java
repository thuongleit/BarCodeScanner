package com.whooo.babr.view.session.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.whooo.babr.R;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.main.MainActivity;
import com.whooo.babr.view.session.signin.SignInActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainSignInActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, SignInBaseContract.View {

    private static final int REQUEST_GOOGLE_SIGN_IN = 1;
    private static final int REQUEST_EMAIL_SIGN_IN = 2;

    @Bind(R.id.fb_original_btn)
    LoginButton mFbOriginalBtn;
    @Bind(R.id.button_facebook_sign_in)
    ImageButton mBtnFbSignIn;
    @Bind(R.id.button_google_sign_in)
    ImageButton mBtnGoogleSignIn;
    @Bind(R.id.button_sign_in_anonymous)
    Button mBtnAnonymousSignIn;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    @Inject
    SignInBaseContract.Presenter mPresenter;

    @Inject
    FirebaseAuth mFirebaseAuth;

    private ProgressDialog mProgressDialog;

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
        ButterKnife.bind(this);

        initializeSocialAuth();

        DaggerSignInBaseComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .signInBaseModule(new SignInBaseModule(this))
                .build()
                .inject(this);

        initializeFacebookAuth();

//        mFirebaseAuth.createUserWithEmailAndPassword("thongle123@gmail.com", "123123")
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(MainSignInActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            onAuthSuccess(mFirebaseAuth.getCurrentUser());
//                        }
//                    }
//                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("onConnectionFailed:" + connectionResult);
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
                    .createProgressDialog(this, R.string.dialog_processing_title, R.string.dialog_processing_message);
            mProgressDialog.setOnCancelListener(dialog -> {
                dialog.dismiss();
                setButtonFbEnable(true);
                setButtonGoogleEnable(true);
                setButtonSignInAnonymusEnable(true);
            });
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setButtonSignInAnonymusEnable(boolean enabled) {
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

    @OnClick(R.id.button_facebook_sign_in)
    void performFbSignIn() {
        mFbOriginalBtn.performClick();
    }

    @OnClick(R.id.button_google_sign_in)
    void performGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGN_IN);
    }

    @OnClick(R.id.button_email_sign_in)
    void performSignInUsingEmail() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, REQUEST_EMAIL_SIGN_IN);

    }

    @OnClick(R.id.button_sign_in_anonymous)
    void performSignInAnonymous() {
        mPresenter.signInAnonymous();
    }

    private void initializeSocialAuth() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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
                Timber.d("facebook:onSuccess:" + loginResult);
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
