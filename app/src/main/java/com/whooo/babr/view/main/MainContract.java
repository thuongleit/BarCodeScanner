package com.whooo.babr.view.main;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;
import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.util.List;

public interface MainContract {
    interface View extends BaseView {
        void onSaveProductsSuccess();

        void onRemoveProductsSuccess();

        void requestFailed(String message);

        void onEmptyResponse();
    }

    interface Presenter extends BasePresenter {
        void getProducts();

        void saveProducts(List<Product> products);

        void saveProductsHistory(CheckoutHistory history, List<Product> products);

        void removeProducts(Product product);

        MainViewModel getViewModel();
    }
}
