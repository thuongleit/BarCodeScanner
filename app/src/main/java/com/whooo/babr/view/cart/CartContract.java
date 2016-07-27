package com.whooo.babr.view.cart;

import android.support.annotation.NonNull;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;
import com.whooo.babr.vo.Cart;

public interface CartContract {

    interface View extends BaseView {

        void startDetailActivity(Cart cart);

        void showProgress(boolean show);

        void showConfirmDialogInDelete(@NonNull Cart cart);

        void generateQRCode(@NonNull Cart cart);

        void showConfirmDialogInCheckout(@NonNull Cart cart);
    }

    interface Presenter extends BasePresenter {

        CartViewModel getViewModel();

        void deleteCart(@NonNull Cart cart);

        void checkout(@NonNull Cart cart);
    }
}