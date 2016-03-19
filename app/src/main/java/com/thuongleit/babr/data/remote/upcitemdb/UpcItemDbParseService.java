package com.thuongleit.babr.data.remote.upcitemdb;

import com.thuongleit.babr.data.remote.upcdatabase.UpcDatabase;
import com.thuongleit.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.thuongleit.babr.data.remote.upcdatabase.UpcDatabaseService;
import com.thuongleit.babr.data.remote.upcitemdb.model.Item;
import com.thuongleit.babr.data.remote.upcitemdb.model.UpcItemDb;
import com.thuongleit.babr.vo.Product;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by ThongLe on 3/19/2016.
 */
@Singleton
public class UpcItemDbParseService {

   @Inject
    public UpcItemDbParseService(){}

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
                    public void onResponse(Response<UpcItemDb> response, Retrofit retrofit) {
                        UpcItemDb upcItemDb = response.body();
                        Item item=upcItemDb.getItems().get(0);
                        if (upcItemDb.getCode().equals("OK")) {
                            product.setSource("upcitemdb.com");
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

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });
    }



}
