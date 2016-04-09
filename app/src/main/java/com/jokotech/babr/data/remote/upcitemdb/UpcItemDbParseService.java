package com.jokotech.babr.data.remote.upcitemdb;

import com.jokotech.babr.data.remote.upcitemdb.model.Item;
import com.jokotech.babr.data.remote.upcitemdb.model.UpcItemDb;
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
public class UpcItemDbParseService {

    @Inject
    public UpcItemDbParseService() {
    }

    public Observable<Product> getUpcItemDbParseService(final String code) {
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {

                Timber.d("getUpcItemDbParseService" + code);

                Product product = new Product();
                Retrofit retrofit = new Retrofit.Builder()
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://api.upcitemdb.com/")
                        .build();

                UpcItemDbService upcDatabaseService = retrofit.create(UpcItemDbService.class);
                Call<UpcItemDb> observable = upcDatabaseService.getProductUpcItemDb(code);
                observable.enqueue(new Callback<UpcItemDb>() {
                    @Override
                    public void onResponse(Call<UpcItemDb> call, Response<UpcItemDb> response) {
                        UpcItemDb upcItemDb = response.body();
                        if (upcItemDb != null && upcItemDb.getItems() != null && !upcItemDb.getItems().isEmpty()) {
                            Item item = upcItemDb.getItems().get(0);
                            if (upcItemDb.getCode().equals("OK")) {
                                product.setSource("upcitemdb.com");
                                product.setListId("a");
                                product.setName(item.getTitle());
                                product.setImageUrl(item.getImages().get(0));
                                product.setEan(item.getEan());
                                product.setManufacture(item.getBrand());
                                product.setModel(item.getModel());
                                product.setUpcA(item.getUpc());
                                subscriber.onNext(product);
                                subscriber.onCompleted();
                            }
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<UpcItemDb> call, Throwable t) {

                    }
                });

            }
        });
    }


}
