package com.jokotech.babr.data.local;

import android.app.Application;

import javax.inject.Inject;

/**
 * Created by thuongle on 1/30/16.
 */
public class BaseModel  {

    private final Application mApp;

    @Inject
    public BaseModel(Application application) {
        mApp = application;
    }
}
