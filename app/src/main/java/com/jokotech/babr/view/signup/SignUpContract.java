package com.jokotech.babr.view.signup;

import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.view.base.BaseView;

public interface SignUpContract {

    interface View extends BaseView<Presenter> {

        void onCreateUserSuccess();
    }

    interface Presenter extends BasePresenter {

        void createUser(User user);

    }
}
