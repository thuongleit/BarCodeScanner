package com.whooo.barscanner.mvp.presenters;

/**
 * Created by thuongle on 10/18/15.
 */
public interface Presenter<T> {

    void attach(T view);

    void deAttach();
}
