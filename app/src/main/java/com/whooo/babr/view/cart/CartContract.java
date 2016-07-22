package com.whooo.babr.view.cart;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;
import com.whooo.babr.vo.Cart;

public interface CartContract {

    interface View extends BaseView {

        void startDetailActivity(Cart cart);
    }

    interface Presenter extends BasePresenter {

        CartViewModel getViewModel();
    }
}
