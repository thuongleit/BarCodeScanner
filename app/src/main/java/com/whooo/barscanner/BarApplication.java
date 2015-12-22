package com.whooo.barscanner;

import android.app.Application;

import com.whooo.barscanner.utils.Log;
import com.parse.Parse;

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

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}
