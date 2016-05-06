package com.jokotech.babr.view.signin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jokotech.babr.R;
import com.jokotech.babr.di.ActivityScope;
import com.jokotech.babr.util.dialog.DialogFactory;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.main.MainActivity;
import com.parse.ParseUser;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shem.com.materiallogin.MaterialLoginView;
import shem.com.materiallogin.MaterialLoginViewListener;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity implements SignInView {

    @Bind(R.id.login_view)
    MaterialLoginView mLoginView;

    @Inject
    SignInPresenter mSignInPresenter;
    @Inject
    @ActivityScope
    Context mContext;

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

        mLoginView.setListener(new MaterialLoginViewListener() {
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                String email = registerUser.getEditText().getText().toString();
                String pass = registerPass.getEditText().getText().toString();
                String rePass = registerPassRep.getEditText().getText().toString();

                boolean isValid = true;
                if (!pass.equals(rePass)) {
                    registerPassRep.getEditText().setError(getString(R.string.error_in_same_password));
                    registerPassRep.getEditText().requestFocus();
                    isValid = false;
                } else {
                    registerPassRep.getEditText().setError(null);
                }
                if (!TextUtils.isEmpty(pass)) {
                    registerPass.getEditText().setError(getString(R.string.error_invalid_password));
                    registerPass.getEditText().requestFocus();
                    isValid = false;
                } else {
                    registerPass.getEditText().setError(null);
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    registerUser.getEditText().setError(getString(R.string.error_invalid_email));
                    registerUser.getEditText().requestFocus();
                    isValid = false;
                } else {
                    registerUser.getEditText().setError(null);
                }

                if (isValid) {
                    mSignInPresenter.signUp(email, pass);
                }
            }

            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
                String email = loginUser.getEditText().getText().toString();
                String password = loginPass.getEditText().getText().toString();

                boolean isValid = true;
                if (!TextUtils.isEmpty(password)) {
                    loginPass.getEditText().setError(getString(R.string.error_invalid_password));
                    loginPass.getEditText().requestFocus();
                    isValid = false;
                } else {
                    loginPass.getEditText().setError(null);
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    loginUser.getEditText().setError(getString(R.string.error_invalid_email));
                    loginUser.getEditText().requestFocus();
                    isValid = false;
                } else {
                    loginUser.getEditText().setError(null);
                }

                if (isValid) {
                    mSignInPresenter.login(email, password);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSignInPresenter.detachView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActionSuccess(ParseUser user) {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActionFailed(String message) {
        DialogFactory.createGenericErrorDialog(mContext, message).show();
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(mContext, R.string.dialog_authenticating);
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showNetworkError() {
        DialogFactory.createGenericErrorDialog(mContext, R.string.dialog_internet_disconnnect_error).show();
    }

    @Override
    public void showGeneralError(String message) {
        DialogFactory.createGenericErrorDialog(mContext, message).show();
    }

    @OnClick(R.id.text_login_as_guest)
    public void loginAsGuest(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_login_as_guest_warning));
        builder.setPositiveButton(getString(R.string.button_ok), (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setNegativeButton(getString(R.string.button_ignore), (dialog, which) -> {
            dialog.dismiss();
            onActionSuccess(null);
        });
        builder.create().show();
    }
}

