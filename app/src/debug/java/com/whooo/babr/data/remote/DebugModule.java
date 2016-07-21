package com.whooo.babr.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.whooo.babr.di.Interceptor;

import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by thuongle on 7/21/16.
 */
@Module
public class DebugModule {

    @Provides
    @Singleton
    @Interceptor
    List<okhttp3.Interceptor> provideInterceptors() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return Arrays.asList(new StethoInterceptor(), loggingInterceptor);
    }
}
