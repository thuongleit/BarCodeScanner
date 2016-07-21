package com.whooo.babr;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

public class DebugApplication extends BarApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //install debug tools
        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);
    }
}
