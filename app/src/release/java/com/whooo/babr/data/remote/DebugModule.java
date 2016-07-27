package com.whooo.babr.data.remote;

import com.whooo.babr.di.Interceptor;

import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DebugModule {

    @Provides
    @Singleton
    @Interceptor
    List<okhttp3.Interceptor> provideInterceptors() {
        return Collections.emptyList();
    }
}
