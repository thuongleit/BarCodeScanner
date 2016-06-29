package com.jokotech.babr.view.session.splash;

import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.view.base.BaseView;

public interface SplashContract {

    interface View extends BaseView {

        void onUserHasSignedIn();

        void onUserNotSignedIn();
    }

    interface Presenter extends BasePresenter {
    }
}
