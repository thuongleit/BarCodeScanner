package com.whooo.babr.view.scan.result;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;
import com.whooo.babr.vo.Product;

import java.util.List;

interface ResultContract {

    interface View extends BaseView {

        void requestFailed(String message);

        void onSaveSuccess(List<Product> products);
    }

    interface Presenter extends BasePresenter {

        ResultViewModel getViewModel();

        void saveProducts();
    }
}
