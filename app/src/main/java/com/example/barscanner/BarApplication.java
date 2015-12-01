package com.example.barscanner;

import android.app.Application;

import com.example.barscanner.utils.Log;

/**
 * Created by thuongle on 11/24/15.
 */
public class BarApplication extends Application {

    static {
        System.loadLibrary("iconv");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.init(BuildConfig.DEBUG);
    }
}
