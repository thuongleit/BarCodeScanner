package com.whooo.babr.view.session.signin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivitySignInBinding;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.session.signup.SignUpActivity;

import javax.inject.Inject;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity implements SignInContract.View, View.OnClickListener {

    private static final int REQUEST_SIGN_UP = 1;

    @Inject
    SignInContract.Presenter mSignInPresenter;

    private AlertDialog mProgressDialog;
    private Button mBtnSignIn;
    private EditText mInputEmail;
    private EditText mInputPassword;

    @Override
    protected BasePresenter getPresenter() {
        return mSignInPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        initializeInjector();
        ActivitySignInBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        binding.setPresenter(mSignInPresenter);

        mBtnSignIn = binding.buttonSignIn;
        mInputEmail = binding.inputEmail;
        mInputPassword = binding.inputPassword;

        binding.inputPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mSignInPresenter.performSignIn(binding.getEmail(), binding.getPassword());
                return true;
            }
            return false;
        });

        binding.textForgotPassword.setOnClickListener(this);
        binding.textLinkSignUp.setOnClickListener(this);
    }

    private void initializeInjector() {
        DaggerSignInComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .signInModule(new SignInModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == RESULT_OK) {
                onSignInSuccess();
            }
        }
    }

    @Override
    public void showNetworkError() {
        showNetworkErrorDialog();
    }

    @Override
    public void showInAppError() {
        showInAppErrorDialog();
    }

    @Override
    public void onSignInSuccess() {
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void onSignInFailed(String error) {
        DialogFactory.createSimpleNoTitleDialog(this, error).show();
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(this);
            setSignInBtnEnable(true);
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setSignInBtnEnable(boolean enabled) {
        mBtnSignIn.setEnabled(enabled);
    }

    @Override
    public boolean validateInput(String email, String password) {
        boolean valid = true;
        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mInputEmail.setError(getString(R.string.error_invalid_email));
            mInputEmail.requestFocus();
            valid = false;
        } else {
            mInputEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            mInputPassword.setError(getString(R.string.error_incorrect_password));
            mInputPassword.requestFocus();
            valid = false;
        } else {
            mInputPassword.setError(null);
        }
        return valid;
    }

    @Override
    public boolean validateInput(@Nullable String email) {
        boolean valid = true;
        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mInputEmail.setError(getString(R.string.error_invalid_email));
            mInputEmail.requestFocus();
            valid = false;
        } else {
            mInputEmail.setError(null);
        }
        return valid;
    }

    @Override
    public void onResetPasswordSuccess() {
        DialogFactory.createGenericErrorDialog(this, "Reset password successful. Check your email for the instructor!").show();
    }

    @Override
    public void onResetPasswordFailed(String error) {
        DialogFactory.createGenericErrorDialog(this, error).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_link_sign_up:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGN_UP);
                break;
        }
    }
}