package com.jokotech.babr.view.session.signup;

import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.view.base.BaseView;
import com.jokotech.babr.view.session.base.User;

public interface SignUpContract {

    interface View extends BaseView {

        void onSignUpSuccess();

        void onSignUpFailed(String error);

        void showProgress(boolean show);

        void setSignUpBtnEnable(boolean enabled);
    }

    interface Presenter extends BasePresenter {

        void createUser(User user);

    }
}
