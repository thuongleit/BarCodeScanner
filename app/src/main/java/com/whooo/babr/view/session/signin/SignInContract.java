package com.whooo.babr.view.session.signin;

import android.support.annotation.Nullable;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;

public interface SignInContract {
    interface View extends BaseView {

        void onSignInSuccess();

        void onSignInFailed(String error);

        void showProgress(boolean show);

        void setSignInBtnEnable(boolean enabled);

        boolean validateInput(@Nullable String email, @Nullable String password);

        boolean validateInput(@Nullable String email);

        void onResetPasswordSuccess();

        void onResetPasswordFailed(String error);
    }

    interface Presenter extends BasePresenter {

        void performSignIn(@Nullable String email, @Nullable String password);

        void askForgotPassword(@Nullable String email);
    }
}
