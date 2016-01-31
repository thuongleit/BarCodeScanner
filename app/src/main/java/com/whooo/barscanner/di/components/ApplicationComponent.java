package com.whooo.barscanner.di.components;

import android.app.Application;
import android.content.Context;

import com.whooo.barscanner.config.Config;
import com.whooo.barscanner.data.DataManager;
import com.whooo.barscanner.data.local.ProductModel;
import com.whooo.barscanner.data.remote.ParseService;
import com.whooo.barscanner.di.ApplicationScope;
import com.whooo.barscanner.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by thuongle on 12/23/15.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    @ApplicationScope
    Context context();

    ParseService parseService();

    DataManager dataManager();

    ProductModel productModel();

    Config config();

    void inject(DataManager manager);
}
