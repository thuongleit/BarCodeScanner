package com.whooo.babr.view.session.signup;

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
import com.whooo.babr.databinding.ActivitySignUpBinding;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.session.signin.SignInActivity;

import javax.inject.Inject;

public class SignUpActivity extends BaseActivity implements SignUpContract.View, View.OnClickListener {

    private static final int REQUEST_SIGN_IN = 1;

    private EditText mInputEmail;
    private EditText mInputPassword;
    private EditText mInputPasswordConfirmation;
    private Button mBtnSignUp;

    private AlertDialog mProgressDialog;

    @Inject
    SignUpContract.Presenter mSignUpPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return mSignUpPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        ActivitySignUpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        initializeInjector();
        initViews(binding);
        binding.setPresenter(mSignUpPresenter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                onSignUpSuccess();
            }
        }
    }

    @Override
    public void onSignUpSuccess() {
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void onSignUpFailed(String error) {
        DialogFactory.createSimpleNoTitleDialog(this, error).show();
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(this);
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setSignUpBtnEnable(boolean enabled) {
        mBtnSignUp.setEnabled(enabled);
    }

    @Override
    public boolean validateInput(@Nullable String email, @Nullable String password, @Nullable String confirmPassword) {
        boolean valid = true;

        if (TextUtils.isEmpty(password)) {
            mInputPassword.setError(getString(R.string.error_incorrect_password));
            mInputPassword.requestFocus();
            valid = false;
        } else {
            mInputPassword.setError(null);
        }

        if (password != null && !password.equals(confirmPassword)) {
            mInputPasswordConfirmation.setError(getString(R.string.error_in_same_password));
            valid = false;
        } else {
            mInputPasswordConfirmation.setError(null);
        }

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
    public void showNetworkError() {
        showNetworkErrorDialog();
    }

    @Override
    public void showInAppError() {
        showInAppErrorDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_link_sign_in:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivityForResult(intent, REQUEST_SIGN_IN);
                break;
        }
    }

    private void initializeInjector() {
        DaggerSignUpComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .signUpModule(new SignUpModule(this))
                .build()
                .inject(this);
    }

    private void initViews(ActivitySignUpBinding binding) {
        mInputEmail = binding.inputEmail;
        mInputPassword = binding.inputPassword;
        mInputPasswordConfirmation = binding.inputPasswordConfirmation;
        mBtnSignUp = binding.buttonSignUp;

        binding.textLinkSignIn.setOnClickListener(this);

        mInputPasswordConfirmation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mSignUpPresenter.createUser(mInputEmail.getText().toString(),
                        mInputPassword.getText().toString(),
                        mInputPasswordConfirmation.getText().toString());
                return true;
            }
            return false;
        });
    }
}