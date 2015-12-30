package com.whooo.barscanner.mvp.views;

/**
 * Created by thuongle on 12/30/15.
 */
public interface ErrorableView extends View {
    void showProgress();

    void hideProgress();

    void onError(Exception e);

}
