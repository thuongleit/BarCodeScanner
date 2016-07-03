package com.whooo.babr;

import android.app.Application;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.whooo.babr.di.components.ApplicationComponent;
import com.whooo.babr.di.components.DaggerApplicationComponent;
import com.whooo.babr.di.modules.ApplicationModule;
import com.parse.Parse;
import com.raizlabs.android.dbflow.config.FlowManager;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by thuongle on 11/24/15.
 */
public class BarApplication extends Application {

    static {
        System.loadLibrary("iconv");
    }

    private ApplicationComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        //enable logger
        CrashlyticsCore crashCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder()
                .core(crashCore)
                .build());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Timber.plant(new CrashlyticsTree());

        mAppComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getAppComponent() {
        return mAppComponent;
    }

    public class CrashlyticsTree extends Timber.Tree {
        private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
        private static final String CRASHLYTICS_KEY_TAG = "tag";
        private static final String CRASHLYTICS_KEY_MESSAGE = "message";

        @Override
        protected void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable t) {
            if (priority == android.util.Log.VERBOSE || priority == android.util.Log.DEBUG || priority == android.util.Log.INFO) {
                return;
            }

            Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
            Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
            Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);

            if (t == null) {
                Crashlytics.logException(new Exception(message));
            } else {
                Crashlytics.logException(t);
            }
        }
    }
}
