package com.thuongleit.babr.di.components;

import android.app.Application;
import android.content.Context;

import com.thuongleit.babr.config.Config;
import com.thuongleit.babr.data.DataManager;
import com.thuongleit.babr.data.local.ProductModel;
import com.thuongleit.babr.data.remote.ApiModule;
import com.thuongleit.babr.data.remote.ParseService;
import com.thuongleit.babr.data.remote.amazon.AmazonService;
import com.thuongleit.babr.di.ApplicationScope;
import com.thuongleit.babr.di.modules.ApplicationModule;

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
