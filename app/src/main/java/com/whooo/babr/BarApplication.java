package com.whooo.babr;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.whooo.babr.di.ApplicationComponent;
import com.whooo.babr.di.ApplicationModule;
import com.whooo.babr.di.DaggerApplicationComponent;

import timber.log.Timber;

public class BarApplication extends Application {

    static {
        System.loadLibrary("iconv");
    }

    private ApplicationComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //enable to save local db
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Timber.plant(new CrashlyticsTree());

        mAppComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        DataBindingUtil.setDefaultComponent(mAppComponent);
    }

    public ApplicationComponent getAppComponent() {
        return mAppComponent;
    }

    private class CrashlyticsTree extends Timber.Tree {

        @Override
        protected void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable t) {
            switch (priority) {
                case Log.DEBUG:
                case Log.WARN:
                    FirebaseCrash.logcat(priority, tag, message);
                    return;
                case Log.ERROR:
                    report(priority, tag, message, t);
                    return;
                default: //mean Log.INFO || Log.VERBOSE || Log.ASSERT
            }
        }

        private void report(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable t) {
            FirebaseCrash.logcat(priority, tag, message);
            if (t == null) {
                FirebaseCrash.report(new Exception(message));
            } else {
                FirebaseCrash.report(t);
            }
        }
    }
}
