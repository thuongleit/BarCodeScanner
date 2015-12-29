package com.whooo.barscanner.injectors.components;

import android.app.Application;

import com.whooo.barscanner.database.SQLHelper;
import com.whooo.barscanner.injectors.modules.ApplicationModule;
import com.whooo.barscanner.mvp.schedulers.ObserverOn;
import com.whooo.barscanner.mvp.schedulers.SubscribeOn;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by thuongle on 12/23/15.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    ObserverOn observerOn();

    SubscribeOn subscribeOn();

    SQLHelper databaseHelper();

}
