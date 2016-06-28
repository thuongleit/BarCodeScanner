package com.jokotech.babr.view.session.splash;

import com.jokotech.babr.view.base.BasePresenter;

public interface SplashContract {

    interface View {

        void setPresenter(Presenter presenter);

        void onUserHasSignedIn();

        void onUserNotSignedIn();

        void inAppError(Throwable e);
    }

    interface Presenter extends BasePresenter {
    }
}
