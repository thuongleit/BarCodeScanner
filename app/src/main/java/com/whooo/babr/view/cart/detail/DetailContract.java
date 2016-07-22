package com.whooo.babr.view.cart.detail;

import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;

public class DetailContract {

    interface View extends BaseView{

    }

    public interface Presenter extends BasePresenter{

        DetailViewModel getViewModel();

    }
}
