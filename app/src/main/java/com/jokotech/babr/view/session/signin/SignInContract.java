package com.jokotech.babr.view.session.signin;

import android.support.annotation.Nullable;

import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.view.base.BaseView;

public interface SignInContract {
    interface View extends BaseView {

        void onSignInSuccess();

        void onSignInFailed(String error);

        void showProgress(boolean show);

        void setSignInBtnEnable(boolean enabled);

        boolean validateInput(String email, String password);
    }

    interface Presenter extends BasePresenter {

        void performSignIn(@Nullable String email, @Nullable String password);

        void askForgotPassword(@Nullable  String email);
    }
}
