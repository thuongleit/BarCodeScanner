package com.whooo.barscanner.mvp.presenters;

import com.whooo.barscanner.mvp.views.View;

/**
 * Created by thuongle on 10/18/15.
 */
public interface Presenter<T extends View> {

    void attach(T view);

    void deAttach();
}
