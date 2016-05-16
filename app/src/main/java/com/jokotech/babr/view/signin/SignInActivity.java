package com.jokotech.babr.view.signin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.jokotech.babr.R;
import com.jokotech.babr.config.Config;
import com.jokotech.babr.di.ActivityScope;
import com.jokotech.babr.util.dialog.DialogFactory;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.main.MainActivity;
import com.jokotech.babr.view.signup.SignUpPresenter;
import com.parse.ParseUser;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity implements SignInView, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_SIGN_IN = 1;

    @Inject
    SignInPresenter mSignInPresenter;
    @Inject
    SignUpPresenter mSignUpPresenter;
    @Inject
    @ActivityScope
    Context mContext;
    @Inject
    Config mConfig;

    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    private ProgressDialog mProgressDialog;

    //sign_in Facebook
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);
        getComponent().inject(this);

        mSignInPresenter.attachView(this);

        mCallbackManager = CallbackManager.Factory.create();

        initTokenTracker();
        initProfileTracker();
        initLoginManager();

        validateServerClientID();
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        initGoogleApiClient();


    }



    @Override
    protected void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();

    }


    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully
            GoogleSignInAccount acct = result.getSignInAccount();
            String name = acct.getDisplayName();
            String idToken = acct.getId();
            mConfig.putSignInType(1);
            mSignInPresenter.loginWithSocial(name, idToken);
        }
    }



    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        //for Google plus
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            return;
        }
        //for Email
        if (requestCode == REQUEST_SIGN_IN && resultCode == RESULT_OK) {
            onSignInSuccess(null);
            return;
        }
        //for Facebook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loginSuccesfull() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }


    //Facebook


    private void initLoginManager() {
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Profile profile = Profile.getCurrentProfile();
                        Timber.d("PROFILE_NAME" + profile.getName());
                        mConfig.putSignInType(0);
                        AccessToken accessToken = loginResult.getAccessToken();
                        mSignInPresenter.loginWithSocial(profile.getName(), accessToken.getUserId());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignInActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(SignInActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void initTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Timber.d("ACCESS_TOKEN" + currentAccessToken);
            }
        };
    }


    private void initProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Timber.d("CURRENT_PROFILE" + currentProfile);
            }
        };
    }

    //Email

    @Override
    public void onSignInSuccess(ParseUser user) {
        //mConfig.putSignInType(2);
        loginSuccesfull();
    }

    @Override
    public void onSignInFailed(String message) {
        DialogFactory.createGenericErrorDialog(mContext, message).show();
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(mContext, "Authenticating...");
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showNetworkError() {
        DialogFactory.createGenericErrorDialog(mContext, "You has been disconnected!").show();
    }

    @Override
    public void showGeneralError(String message) {
        DialogFactory.createGenericErrorDialog(mContext, message).show();
    }

    @OnClick(R.id.button_facebook_login)
    public void loginFacebook(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("public_profile"));
    }

    @OnClick(R.id.button_google_login)
    public void loginGooglePlus(View view) {

        signInGoogle();
    }

    @OnClick(R.id.button_sign_in_with_email)
    public void signInWithEmail() {
        Intent intent = new Intent(mContext, SignInWithEmailActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_IN);
    }

    @OnClick(R.id.text_login_as_guest)
    public void loginAsGuest(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Login as guest will store your products locally. If you want to save them in the cloud, login as your account instead.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setNegativeButton("Ignore", (dialog, which) -> {
            dialog.dismiss();
            onSignInSuccess(null);
        });
        builder.create().show();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}