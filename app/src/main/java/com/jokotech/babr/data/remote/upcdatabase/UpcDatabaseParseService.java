package com.jokotech.babr.data.remote.upcdatabase;

import android.util.Log;

import com.jokotech.babr.vo.Product;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by ThongLe on 3/19/2016.
 */
@Singleton
public class UpcDatabaseParseService {

    @Inject
    public UpcDatabaseParseService() {
    }

    public Observable<Product> getProductUpcDatabase(final String code) {
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {
                Log.d("passedScanner","getProductUpcDatabase" + code);
                Product product = new Product();
                Retrofit retrofit = new Retrofit.Builder()
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://api.upcdatabase.org/")
                        .build();

                UpcDatabaseService upcDatabaseService = retrofit.create(UpcDatabaseService.class);
                Call<UpcDatabase> observable = upcDatabaseService.getProductUpcDatabase(code);
                observable.enqueue(new Callback<UpcDatabase>() {
                    @Override
                    public void onResponse(Call<UpcDatabase> call, Response<UpcDatabase> response) {
                        UpcDatabase upcDatabase = response.body();
                        if (upcDatabase != null && upcDatabase.getItemname() != null) {
                            Timber.d(upcDatabase.getItemname());
                            product.setName(upcDatabase.getItemname());
                            product.setSource("upcdatabase.com");
                            product.setListId("a");
                            subscriber.onNext(product);
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<UpcDatabase> call, Throwable t) {

                    }
                });

            }
        });
    }
}
