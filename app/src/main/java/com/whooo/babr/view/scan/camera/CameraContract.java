package com.whooo.babr.view.scan.camera;

import android.support.annotation.Nullable;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;
import com.whooo.babr.vo.Product;

import java.util.List;

interface CameraContract {
    interface View extends BaseView {

        void showProgress(boolean show);

        void onSearchSuccess(List<Product> products);

        void onEmptyResponse();

        void stopCamera();
    }

    interface Presenter extends BasePresenter {

        void searchProducts(@Nullable String code);
    }
}
