package com.whooo.barscanner.activities;

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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.whooo.barscanner.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.input_username)
    EditText mInputUsername;
    @Bind(R.id.input_email)
    EditText mInputEmail;
    @Bind(R.id.input_password)
    EditText mInputPassword;
    @Bind(R.id.input_confirm_password)
    EditText mInputConfirmPassword;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        mInputConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.btn_signup || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });
    }

    public void onButtonCreateAccountClick(View view) {
        attemptSignUp();
    }


    public void onButtonSignInClick(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {
        showProgress();
        // Reset errors.
        mInputUsername.setError(null);
        mInputUsername.setError(null);
        mInputPassword.setError(null);
        mInputConfirmPassword.setError(null);

        // Store values at the time of the login attempt.
        final String username = mInputUsername.getText().toString();
        final String email = mInputEmail.getText().toString();
        final String password = mInputPassword.getText().toString();
        final String confirmPassword = mInputConfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //check for a valid username
        if (TextUtils.isEmpty(username)) {
            mInputUsername.setError(getString(R.string.error_field_required));
            focusView = mInputUsername;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mInputPassword.setError(getString(R.string.error_invalid_password));
            focusView = mInputPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mInputEmail.setError(getString(R.string.error_field_required));
            focusView = mInputEmail;
            cancel = true;
        }

        if (!isEmailValid(email)) {
            mInputEmail.setError(getString(R.string.error_invalid_email));
            focusView = mInputEmail;
            cancel = true;
        }

        if (!isPasswordMatch(password, confirmPassword)) {
            mInputConfirmPassword.setError("Password mismatch");
            focusView = mInputConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            ParseUser parseUser = new ParseUser();
            parseUser.setUsername(username);
            parseUser.setEmail(email);
            parseUser.setPassword(password);

            parseUser.signUpInBackground(new SignUpCallback() {
                                             @Override
                                             public void done(ParseException e) {
                                                 hideProgress();
                                                 if (e == null) {
                                                     //the user is logged in
                                                     Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                     startActivity(intent);
                                                 } else {
                                                     AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                                     builder.setTitle("Register Error");
                                                     builder.setMessage("Cannot sign up " + e.getMessage());
                                                     builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                         @Override
                                                         public void onClick(DialogInterface dialog, int which) {
                                                             dialog.dismiss();
                                                         }
                                                     });
                                                     builder.setCancelable(true);
                                                     builder.create().show();
                                                 }
                                             }
                                         }

            );

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isPasswordMatch(String password, String confirmPassword) {
        return password.equalsIgnoreCase(confirmPassword);
    }
    private void showProgress() {
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Creating account...");
        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}