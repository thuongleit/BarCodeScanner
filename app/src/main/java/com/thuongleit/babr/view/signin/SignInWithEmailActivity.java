package com.thuongleit.babr.view.signin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.parse.ParseUser;
import com.thuongleit.babr.R;
import com.thuongleit.babr.util.dialog.DialogFactory;
import com.thuongleit.babr.view.base.BaseActivity;
import com.thuongleit.babr.view.signup.SignUpActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by thuongle on 3/6/16.
 */
public class SignInWithEmailActivity extends BaseActivity implements SignInView {
    private static final int REQUEST_SIGN_UP = 1;

    @Bind(R.id.input_username)
    EditText mInputUsername;
    @Bind(R.id.input_password)
    EditText mInputPassword;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    SignInPresenter mSignInPresenter;

    private Context mContext;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_email);
        mContext = this;

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back_signin);
        getComponent().inject(this);
        mSignInPresenter.attachView(this);
        mInputPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                attemptSignIn();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mSignInPresenter.detachView();
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
        setResult(RESULT_OK);
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

    @OnClick(R.id.text_link_sign_up)
    public void onButtonSignUpClick(View view) {
        startActivityForResult(new Intent(this, SignUpActivity.class), REQUEST_SIGN_UP);
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
