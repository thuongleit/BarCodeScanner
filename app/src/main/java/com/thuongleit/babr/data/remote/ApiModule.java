package com.thuongleit.babr.data.remote;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.thuongleit.babr.data.remote.amazon.AmazonService;
import com.thuongleit.babr.data.remote.amazon.util.AmazonSignedRequestsHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.CallAdapter;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.SimpleXmlConverterFactory;

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

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().clear();
        // add logging as last interceptor
        okHttpClient.interceptors().add(logging);

        return okHttpClient;
    }

    @Provides
    @Singleton
    AmazonService provideAmazonService(Retrofit retrofit) {
        return retrofit.create(AmazonService.class);
    }

}
