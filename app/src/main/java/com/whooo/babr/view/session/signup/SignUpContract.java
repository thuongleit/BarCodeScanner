package com.whooo.babr.view.session.signup;

import android.support.annotation.Nullable;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;

public interface SignUpContract {

    interface View extends BaseView {

        void onSignUpSuccess();

        void onSignUpFailed(String error);

        void showProgress(boolean show);

        void setSignUpBtnEnable(boolean enabled);

        boolean validateInput(@Nullable String email, @Nullable String password, @Nullable String confirmPassword);
    }

    interface Presenter extends BasePresenter {

        void createUser(@Nullable String email, @Nullable String password, @Nullable String confirmPassword);

    }
}
