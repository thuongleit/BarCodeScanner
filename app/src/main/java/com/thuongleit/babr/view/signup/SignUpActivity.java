package com.thuongleit.babr.view.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.thuongleit.babr.R;
import com.thuongleit.babr.util.dialog.DialogFactory;
import com.thuongleit.babr.view.base.BaseActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements SignUpView {

    @Bind(R.id.input_username)
    EditText mInputUsername;
    @Bind(R.id.input_email)
    EditText mInputEmail;
    @Bind(R.id.input_password)
    EditText mInputPassword;
    @Bind(R.id.input_confirm_password)
    EditText mInputConfirmPassword;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    SignUpPresenter mSignUpPresenter;

    private Context mContext;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = this;
        ButterKnife.bind(this);
        getComponent().inject(this);
        mSignUpPresenter.attachView(this);

        setSupportActionBar(mToolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back_signin);

        mInputConfirmPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                attemptSignUp();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mSignUpPresenter.detachView();
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
        if (mProgressDialog == null) {
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