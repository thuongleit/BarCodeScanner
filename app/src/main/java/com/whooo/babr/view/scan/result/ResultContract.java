package com.whooo.babr.view.scan.result;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;

public interface ResultContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {

        void getProducts(String detailPageURL);
    }
}
