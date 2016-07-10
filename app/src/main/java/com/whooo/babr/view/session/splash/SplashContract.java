package com.whooo.babr.view.session.splash;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;

public interface SplashContract {

    interface View extends BaseView {

        void onUserHasSignedIn();

        void onUserNotSignedIn();
    }

    interface Presenter extends BasePresenter {
    }
}
