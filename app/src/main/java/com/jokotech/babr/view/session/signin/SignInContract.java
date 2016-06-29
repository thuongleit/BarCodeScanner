package com.jokotech.babr.view.session.signin;

import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.view.base.BaseView;
import com.jokotech.babr.view.session.base.User;

public interface SignInContract {
    interface View extends BaseView {

        void onSignInSuccess();

        void onSignInFailed(String error);

        void showProgress(boolean show);

        void setSignInBtnEnable(boolean enabled);
    }

    interface Presenter extends BasePresenter {

        void signIn(User user);
    }
}
