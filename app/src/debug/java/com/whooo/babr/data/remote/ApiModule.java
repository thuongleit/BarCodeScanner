package com.whooo.babr.data.remote;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whooo.babr.data.product.ProductSource;
import com.whooo.babr.data.remote.amazon.AmazonParseService;
import com.whooo.babr.data.remote.searchupc.SearchUpcParseService;
import com.whooo.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.whooo.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.whooo.babr.data.remote.walmartlabs.WalmartlabsParseService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class ApiModule {

    public ApiModule() {
    }

    @Provides
    @Singleton
    public SimpleXmlConverterFactory provideSimpleXmlConverter() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        return JacksonConverterFactory.create(mapper);
        return SimpleXmlConverterFactory.create();
    }

    @Provides
    @Singleton
    public JacksonConverterFactory provideJacksonConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return JacksonConverterFactory.create(mapper);
    }

    @Provides
    @Singleton
    public CallAdapter.Factory provideCallAdapter() {
        return RxJavaCallAdapterFactory.create();
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
    SearchUpcParseService.RetrofitService provideSearchParseUpcService(OkHttpClient client, JacksonConverterFactory converter, CallAdapter.Factory callAdapter) {
        Retrofit retrofit = buildRetrofit(client, converter, callAdapter, ProductSource.SEARCH_UPC.getEndpoint());

        return retrofit.create(SearchUpcParseService.RetrofitService.class);
    }

    @Provides
    @Singleton
    UpcItemDbParseService.RetrofitService provideUpcItemDbParseService(OkHttpClient client, JacksonConverterFactory converter, CallAdapter.Factory callAdapter) {
        Retrofit retrofit = buildRetrofit(client, converter, callAdapter, ProductSource.UPC_ITEM_DB.getEndpoint());

        return retrofit.create(UpcItemDbParseService.RetrofitService.class);
    }

    @Provides
    @Singleton
    UpcDatabaseParseService.RetrofitService provideUpcDbParseService(OkHttpClient client, JacksonConverterFactory converter, CallAdapter.Factory callAdapter) {
        Retrofit retrofit = buildRetrofit(client, converter, callAdapter, ProductSource.UPC_DATABASE.getEndpoint());

        return retrofit.create(UpcDatabaseParseService.RetrofitService.class);
    }

    @Provides
    @Singleton
    WalmartlabsParseService.RetrofitService provideWalmartParseService(OkHttpClient client, JacksonConverterFactory converter, CallAdapter.Factory callAdapter) {
        Retrofit retrofit = buildRetrofit(client, converter, callAdapter, ProductSource.WALMART.getEndpoint());

        return retrofit.create(WalmartlabsParseService.RetrofitService.class);
    }

    @Provides
    @Singleton
    AmazonParseService.RetrofitService provideAmazonParseService(OkHttpClient client, SimpleXmlConverterFactory converter, CallAdapter.Factory callAdapter) {
        Retrofit retrofit = buildRetrofit(client, converter, callAdapter, ProductSource.AMAZON.getEndpoint());

        return retrofit.create(AmazonParseService.RetrofitService.class);
    }

    @NonNull
    private Retrofit buildRetrofit(OkHttpClient client, Converter.Factory converter, CallAdapter.Factory callAdapter, String url) {
        return new Retrofit.Builder()
                    .baseUrl(url)
                    .client(client)
                    .addCallAdapterFactory(callAdapter)
                    .addConverterFactory(converter)
                    .build();
    }
}
