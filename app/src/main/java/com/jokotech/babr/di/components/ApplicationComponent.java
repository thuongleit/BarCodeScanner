package com.jokotech.babr.di.components;

import android.app.Application;
import android.content.Context;

import com.jokotech.babr.config.Config;
import com.jokotech.babr.data.DataManager;
import com.jokotech.babr.data.local.ProductModel;
import com.jokotech.babr.data.remote.ApiModule;
import com.jokotech.babr.data.remote.ParseService;
import com.jokotech.babr.data.remote.amazon.AmazonService;
import com.jokotech.babr.di.ApplicationScope;
import com.jokotech.babr.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by thuongle on 12/23/15.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    Application application();

    @ApplicationScope
    Context context();

    ParseService parseService();

    AmazonService amazonService();

    DataManager dataManager();

    ProductModel productModel();

    Config config();

    void inject(DataManager manager);
}
