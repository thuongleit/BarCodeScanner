package com.jokotech.babr.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jokotech.babr.data.remote.amazon.AmazonService;
import com.jokotech.babr.data.remote.amazon.util.AmazonSignedRequestsHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class ApiModule {

    public ApiModule() {
    }

    @Provides
    @Singleton
    public SimpleXmlConverterFactory provideConverter() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        return JacksonConverterFactory.create(mapper);
        return SimpleXmlConverterFactory.create();
    }

    @Provides
    @Singleton
    public CallAdapter.Factory provideCallAdapter() {
        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, SimpleXmlConverterFactory converter, CallAdapter.Factory callAdapter) {
        return new Retrofit.Builder()
                .baseUrl("http://" + AmazonSignedRequestsHelper.ENDPOINT + AmazonSignedRequestsHelper.REQUEST_URI)
                .client(client)
                .addCallAdapterFactory(callAdapter)
                .addConverterFactory(converter)
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.networkInterceptors().add(new StethoInterceptor());
        // add logging as last interceptor
        builder.interceptors().add(logging);
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    AmazonService provideAmazonService(Retrofit retrofit) {
        return retrofit.create(AmazonService.class);
    }
}
