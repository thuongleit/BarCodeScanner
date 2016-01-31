package com.whooo.barscanner.view.base;

/**
 * Created by thuongle on 12/30/15.
 */
public interface ErrorableView extends MvpView {

    void showNetworkError();

    void showGeneralError(String message);

}
