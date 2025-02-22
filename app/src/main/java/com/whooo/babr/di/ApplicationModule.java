package com.whooo.babr.di;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.whooo.babr.data.cart.CartRepository;
import com.whooo.babr.data.cart.CartRepositoryImpl;
import com.whooo.babr.data.product.ProductRepositoryImpl;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.data.product.SearchService;
import com.whooo.babr.util.CircleTransform;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class    ApplicationModule {

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
    ProductRepository provideProductRepository(SearchService searchService) {
        return new ProductRepositoryImpl(searchService);
    }

    @Provides
    @Singleton
    CartRepository provideCartRepository(){
        return new CartRepositoryImpl();
    }

    @Provides
    CircleTransform provideCircleTransform(){
        return new CircleTransform();
    }
}
