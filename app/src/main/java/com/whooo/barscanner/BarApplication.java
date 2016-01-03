package com.whooo.barscanner;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.whooo.barscanner.injectors.components.ApplicationComponent;
import com.whooo.barscanner.injectors.components.DaggerApplicationComponent;
import com.whooo.barscanner.injectors.modules.ApplicationModule;
import com.whooo.barscanner.utils.Log;
import io.fabric.sdk.android.Fabric;

/**
 * Created by thuongle on 11/24/15.
 */
public class BarApplication extends Application {

    static {
        System.loadLibrary("iconv");
    }

    private ApplicationComponent mApplicationModule;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Log.init(BuildConfig.DEBUG);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        initializeInjectors();
    }

    private void initializeInjectors() {
        mApplicationModule = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationModule;
    }
}
