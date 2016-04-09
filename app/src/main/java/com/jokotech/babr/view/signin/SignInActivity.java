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

import com.parse.ParseUser;
import com.jokotech.babr.R;
import com.jokotech.babr.di.ActivityScope;
import com.jokotech.babr.util.dialog.DialogFactory;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.main.MainActivity;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity implements SignInView {
    private static final int REQUEST_SIGN_IN = 1;

    @Inject
    SignInPresenter mSignInPresenter;
    @Inject
    @ActivityScope
    Context mContext;

    private SocialAuthAdapter mSocialAuthAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        getComponent().inject(this);

        mSignInPresenter.attachView(this);

        mSocialAuthAdapter = new SocialAuthAdapter(new DialogListener() {
            @Override
            public void onComplete(Bundle bundle) {
                mSocialAuthAdapter.getUserProfileAsync(new SocialAuthListener<Profile>() {
                    @Override
                    public void onExecute(String s, Profile profile) {
                        String token = mSocialAuthAdapter.getCurrentProvider().getAccessGrant().getKey();
                        mSignInPresenter.loginWithSocial(profile, token);
                    }

                    @Override
                    public void onError(SocialAuthError socialAuthError) {
                        onError(socialAuthError);
                    }
                });
            }

            @Override
            public void onError(SocialAuthError socialAuthError) {
                Timber.e(socialAuthError, socialAuthError.getMessage());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN && resultCode == RESULT_OK) {
            onSignInSuccess(null);
        }
    }

    @Override
    public void onSignInSuccess(ParseUser user) {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
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
        mSocialAuthAdapter.authorize(this, SocialAuthAdapter.Provider.FACEBOOK);
    }

    @OnClick(R.id.button_google_login)
    public void loginGooglePlus(View view) {
        mSocialAuthAdapter.authorize(this, SocialAuthAdapter.Provider.GOOGLEPLUS);
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
}

