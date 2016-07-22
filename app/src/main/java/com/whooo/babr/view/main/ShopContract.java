package com.whooo.babr.view.main;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;
import com.whooo.babr.view.binding.ItemTouchHandler;
import com.whooo.babr.vo.Product;

public interface ShopContract {
    interface View extends BaseView {
        void onCheckoutSuccess(String keyOfCart);

        void onRemoveProductsSuccess();

        void requestFailed(String message);

        void onEmptyResponse();

        void removeEmptyViewIfNeeded();

        void addPendingRemove(int position, Product clone);

        void showStandaloneProgress(boolean show);
    }

    interface Presenter extends BasePresenter {
        void getProducts();

        void checkout(String cartName);

        void addToPending(String cartName);

        void removeProducts(Product product);

        ShopViewModel getViewModel();

        ItemTouchHandler<Product> itemTouchHandler();

        void undoRemovedProduct(int position, Product product);
    }
}
