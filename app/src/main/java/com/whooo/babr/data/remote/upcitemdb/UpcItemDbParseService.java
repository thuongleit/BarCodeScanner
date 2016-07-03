package com.whooo.babr.data.remote.upcitemdb;

import android.util.Log;

import com.whooo.babr.data.remote.upcitemdb.model.Item;
import com.whooo.babr.data.remote.upcitemdb.model.UpcItemDb;
import com.whooo.babr.vo.Product;

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

                Log.d("passedScanner", "getUpcItemDbParseService" + code);

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
                                product.source = "upcitemdb.com";
                                product.listId = "a";
                                product.name = item.getTitle();
                                if (item.getImages().size() > 0)
                                    product.imageUrl = item.getImages().get(0);
                                product.ean = item.getEan();
                                product.manufacture = item.getBrand();
                                product.model = item.getModel();
                                product.upcA = item.getUpc();
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
