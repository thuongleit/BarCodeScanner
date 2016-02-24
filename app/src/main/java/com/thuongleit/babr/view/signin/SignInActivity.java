package com.thuongleit.babr.view.signin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;
import com.thuongleit.babr.R;
import com.thuongleit.babr.di.ActivityScope;
import com.thuongleit.babr.util.DialogFactory;
import com.thuongleit.babr.view.main.MainActivity;
import com.thuongleit.babr.view.base.BaseActivity;
import com.thuongleit.babr.view.signup.SignUpActivity;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity implements SignInView {
    private static final int REQUEST_SIGN_UP = 1;

    @Bind(R.id.input_username) EditText mInputUsername;
    @Bind(R.id.input_password) EditText mInputPassword;

    @Inject SignInPresenter mSignInPresenter;
    @Inject @ActivityScope Context mContext;

    private SocialAuthAdapter mSocialAuthAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        getComponent().inject(this);

        mSignInPresenter.attachView(this);

        mInputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.button_sign_in || id == EditorInfo.IME_NULL) {
                    attemptSignIn();
                    return true;
                }
                return false;
            }
        });

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
        if (requestCode == REQUEST_SIGN_UP && resultCode == RESULT_OK) {
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

    @OnClick(R.id.button_sign_in)
    public void signIn(View view) {
        attemptSignIn();
    }

    @OnClick(R.id.button_facebook_login)
    public void loginFacebook(View view) {
        mSocialAuthAdapter.authorize(this, SocialAuthAdapter.Provider.FACEBOOK);
    }

    @OnClick(R.id.button_google_login)
    public void loginGooglePlus(View view) {
        mSocialAuthAdapter.authorize(this, SocialAuthAdapter.Provider.GOOGLEPLUS);
    }

    @OnClick(R.id.button_sign_up)
    public void onButtonSignUpClick(View view) {
        startActivityForResult(new Intent(this, SignUpActivity.class), REQUEST_SIGN_UP);
    }

    @OnClick(R.id.text_login_as_guest)
    public void loginAsGuest(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Login as guest will store your products locally. If you want to save them in the cloud, login as your account instead.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onSignInSuccess(null);
            }
        });
        builder.create().show();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignIn() {
        // Reset errors.
        mInputUsername.setError(null);
        mInputPassword.setError(null);

        // Store values at the time of the login attempt.
        final String password = mInputPassword.getText().toString();
        final String username = mInputUsername.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mInputPassword.setError(getString(R.string.error_invalid_password));
            focusView = mInputPassword;
            cancel = true;
        }

        //check for a valid username
        if (TextUtils.isEmpty(username)) {
            mInputUsername.setError(getString(R.string.error_invalid_username));
            focusView = mInputUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (!TextUtils.isEmpty(password)) {
                mSignInPresenter.login(username, password);
            }
        }
    }
}

