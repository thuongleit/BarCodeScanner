package com.whooo.barscanner.injectors.modules;

import android.app.Application;

import com.whooo.barscanner.database.SQLHelper;
import com.whooo.barscanner.mvp.schedulers.ObserverOn;
import com.whooo.barscanner.mvp.schedulers.SubscribeOn;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thuongle on 12/23/15.
 */
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.application;
    }

    @Provides
    @Singleton
    ObserverOn provideObserverOn() {
        return new ObserverOn() {
            @Override
            public Scheduler getSubscriber() {
                return AndroidSchedulers.mainThread();
            }
        };
    }

    @Provides
    @Singleton
    SubscribeOn provideSubscribeOn() {
        return new SubscribeOn() {
            @Override
            public Scheduler getSubscriber() {
                return Schedulers.newThread();
            }
        };
    }

    @Provides
    @Singleton
    SQLHelper provideDatabaseHelper() {
        return new SQLHelper(application);
    }
}
