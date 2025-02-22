package com.whooo.babr.view.base;


/**
 * Base interface that any class that wants to act as a View in the MVP (Item View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface BaseView {

    void showNetworkError();

    void showInAppError();
}
