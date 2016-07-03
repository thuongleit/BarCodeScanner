package com.whooo.babr.di.components;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.whooo.babr.config.Config;
import com.whooo.babr.data.DataManager;
import com.whooo.babr.data.remote.ApiModule;
import com.whooo.babr.data.remote.ParseService;
import com.whooo.babr.data.remote.amazon.AmazonService;
import com.whooo.babr.di.ApplicationScope;
import com.whooo.babr.di.modules.ApplicationModule;
import com.squareup.picasso.Picasso;

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

    Config config();

    void inject(DataManager manager);

    Picasso picasso();

    FirebaseAuth firebaseAuth();
}
