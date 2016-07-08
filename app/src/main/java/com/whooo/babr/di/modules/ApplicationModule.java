package com.whooo.babr.di.modules;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.whooo.babr.di.ApplicationScope;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

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
    @ApplicationScope
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    Picasso providePicasso() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        return new Picasso.Builder(application)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    DatabaseReference provideFirebaseDbRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

}
