package com.whooo.babr.view.scan.result;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;
import com.whooo.babr.view.binding.ItemTouchHandler;
import com.whooo.babr.vo.Product;

import java.util.List;

public interface ResultContract {

    interface View extends BaseView {

        void requestFailed(String message);

        void onSaveSuccess(List<Product> products);

        void showProgress(boolean show);

        void addPendingRemove(int position, Product product);
    }

    interface Presenter extends BasePresenter {

        ResultViewModel getViewModel();

        void saveProducts(String cardId);

        ItemTouchHandler<Product> itemTouchHandler();

        void undoRemovedProduct(int position, Product product);

        void removeProducts(Product product);
    }
}
