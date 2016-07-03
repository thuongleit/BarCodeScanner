package com.whooo.babr.view.session.signup;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivitySignUpBinding;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;

import javax.inject.Inject;

public class SignUpActivity extends BaseActivity implements SignUpContract.View {

    Button mBtnSignUp;

    @Inject
    SignUpContract.Presenter mSignUpPresenter;

    private ProgressDialog mProgressDialog;

    @Override
    protected BasePresenter getPresenter() {
        return mSignUpPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        initializeInjector();

        ActivitySignUpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        binding.setPresenter(mSignUpPresenter);
    }

    private void initializeInjector() {
        DaggerSignUpComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .signUpModule(new SignUpModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void setSignUpBtnEnable(boolean enabled) {
        mBtnSignUp.setEnabled(enabled);
    }

    @Override
    public void showNetworkError() {
        showNetworkErrorDialog();
    }

    @Override
    public void showInAppError() {
        showInAppErrorDialog();
    }
}