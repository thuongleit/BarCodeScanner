package com.jokotech.babr.view.base;

/**
 * Created by thuongle on 12/30/15.
 */
public interface ErrorableView extends MvpView {

    void onNetworkFailed();

    void onGeneralFailed(String message);
}
