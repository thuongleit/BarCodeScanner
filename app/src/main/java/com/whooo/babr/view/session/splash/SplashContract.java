package com.whooo.babr.view.session.splash;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;

interface SplashContract {

    interface View extends BaseView {

        void onSessionExists();

        void onSessionNotFound();
    }

    interface Presenter extends BasePresenter {
    }
}
