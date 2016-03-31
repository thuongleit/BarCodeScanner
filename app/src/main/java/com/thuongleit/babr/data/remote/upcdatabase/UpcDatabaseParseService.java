package com.thuongleit.babr.data.remote.upcdatabase;

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
public class UpcDatabaseParseService {

    @Inject
    public UpcDatabaseParseService() {
    }

    public Observable<Product> getProductUpcDatabase(final String code) {
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {
                Timber.d("getProductUpcDatabase" + code);
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
                    public void onResponse(Response<UpcDatabase> response, Retrofit retrofit) {
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
                    public void onFailure(Throwable t) {
                        subscriber.onError(t);
                    }
                });

            }
        });
    }
}
