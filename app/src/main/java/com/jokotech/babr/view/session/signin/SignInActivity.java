package com.jokotech.babr.view.session.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jokotech.babr.R;
import com.jokotech.babr.util.dialog.DialogFactory;
import com.jokotech.babr.view.base.BaseActivity;
import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.view.session.signup.SignUpActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity implements SignInContract.View {

    private static final int REQUEST_SIGN_UP = 1;
    @Bind(R.id.button_sign_in)
    Button mBtnSignin;

    @Inject
    SignInContract.Presenter mSignInPresenter;

    private ProgressDialog mProgressDialog;

    @Override
    protected BasePresenter getPresenter() {
        return mSignInPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
            mProgressDialog = DialogFactory
                    .createProgressDialog(this, R.string.dialog_processing_title, R.string.dialog_processing_message);
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setSignInBtnEnable(boolean enabled) {
        mBtnSignin.setEnabled(enabled);
    }

    @OnClick(R.id.text_link_sign_up)
    void onMoveSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_UP);
    }
}