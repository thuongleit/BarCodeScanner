package com.whooo.babr.view.session.signup;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;

public interface SignUpContract {

    interface View extends BaseView {

        void onSignUpSuccess();

        void onSignUpFailed(String error);

        void showProgress(boolean show);

        void setSignUpBtnEnable(boolean enabled);
    }

    interface Presenter extends BasePresenter {

        void createUser(String email, String fullname, String password);

    }
}
