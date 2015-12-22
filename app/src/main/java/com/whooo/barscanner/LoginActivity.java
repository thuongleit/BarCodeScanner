package com.whooo.barscanner;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.whooo.barscanner.utils.Log;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;
    @Bind(R.id.input_username)
    EditText mInputUsername;
    @Bind(R.id.input_password)
    EditText mInputPassword;
    private SocialAuthAdapter mSocialAuthAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mInputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.btn_login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
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

                    }

                    @Override
                    public void onError(SocialAuthError socialAuthError) {
                        Log.e(socialAuthError.getMessage());
                    }
                });
            }

            @Override
            public void onError(SocialAuthError socialAuthError) {
                Log.e(socialAuthError.getMessage());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });
    }

    public void onButtonLoginClick(View view) {
        attemptLogin();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
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
            showProgress();

            if (!TextUtils.isEmpty(password)) {
                //try log-in first
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        showProgress();
                        if (e == null) {
                            //the user is logged in
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            hideProgress();
                            LoginActivity.this.finish();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Login Error");
                            builder.setMessage("Cannot log in " + e.getMessage());
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    hideProgress();
                                    dialog.dismiss();
                                }
                            });
                            builder.setCancelable(true);
                            builder.create().show();
                        }
                    }
                });

            }
        }
    }

    private void showProgress() {
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    public void onButtonLoginFBClick(View view) {
        mSocialAuthAdapter.authorize(this, SocialAuthAdapter.Provider.FACEBOOK);
    }

    public void onButtonLoginGplusClick(View view) {
        mSocialAuthAdapter.authorize(this, SocialAuthAdapter.Provider.GOOGLEPLUS);
    }

    public void onButtonSignUpClick(View view) {
        startActivityForResult(new Intent(this, SignUpActivity.class), REQUEST_SIGNUP);
    }

    public void onLinkAsGuestClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}

