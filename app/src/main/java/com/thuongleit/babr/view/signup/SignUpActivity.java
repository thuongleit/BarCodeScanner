package com.thuongleit.babr.view.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.thuongleit.babr.R;
import com.thuongleit.babr.di.ActivityScope;
import com.thuongleit.babr.util.DialogFactory;
import com.thuongleit.babr.view.base.BaseActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements SignUpView {

    @Bind(R.id.input_username) EditText mInputUsername;
    @Bind(R.id.input_email) EditText mInputEmail;
    @Bind(R.id.input_password) EditText mInputPassword;
    @Bind(R.id.input_confirm_password) EditText mInputConfirmPassword;

    @Inject SignUpPresenter mSignUpPresenter;
    @Inject @ActivityScope Context mContext;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        getComponent().inject(this);
        mSignUpPresenter.attachView(this);

        mInputConfirmPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.button_sign_up || id == EditorInfo.IME_NULL) {
                attemptSignUp();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onSignUpSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSignUpFailed(String message) {
        DialogFactory.createGenericErrorDialog(mContext, message).show();
    }

    @Override
    public void showProgress(boolean show) {
        if(mProgressDialog == null){
            mProgressDialog = DialogFactory.createProgressDialog(mContext, "Creating account...");
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

    @OnClick(R.id.button_sign_up)
    public void signUp(View view) {
        attemptSignUp();
    }


    @OnClick(R.id.button_sign_in)
    public void backToSignIn(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {
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
            mSignUpPresenter.signUp(username, email, password);
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
}