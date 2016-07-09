package com.whooo.babr.view.scan;

import com.google.repacked.antlr.v4.runtime.misc.Nullable;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;
import com.whooo.babr.vo.Product;

import java.util.List;

interface CameraContract {
    interface View extends BaseView {

        void showProgress(boolean show);

        void playRingtone();

        void onSearchSuccess(List<Product> products);

        void onEmptyResponse();
    }

    interface Presenter extends BasePresenter {

        void searchProducts(@Nullable String code);
    }
}
