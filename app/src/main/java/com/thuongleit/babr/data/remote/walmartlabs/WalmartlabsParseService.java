package com.thuongleit.babr.data.remote.walmartlabs;

import com.thuongleit.babr.data.remote.walmartlabs.model.Walmartlabs;
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
public class WalmartlabsParseService {

    @Inject
    public WalmartlabsParseService(){

    }

    public Observable<Product> getProductWalmart(final String code) {
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {
                Timber.d("getProductWalmart" + code);
                Product product = new Product();
                Retrofit retrofit = new Retrofit.Builder()
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://api.walmartlabs.com/v1/")
                        .build();

                WalmartlabsService walmartlabsService = retrofit.create(WalmartlabsService.class);
                Call<Walmartlabs> observable = walmartlabsService.getProductWalmartlabs(code);
                observable.enqueue(new Callback<Walmartlabs>() {
                    @Override
                    public void onResponse(Response<Walmartlabs> response, Retrofit retrofit) {
                        Walmartlabs walmartlabs = response.body();
                        product.setSource("walmartlabs.com");
                        if (walmartlabs.getItems().get(0).getName()!=null) {
                            product.setName(walmartlabs.getItems().get(0).getName());
                        }
                        if (walmartlabs.getItems().get(0).getLargeImage()!=null) {
                            product.setImageUrl(walmartlabs.getItems().get(0).getLargeImage());
                        }
                        if (walmartlabs.getItems().get(0).getUpc()!=null){
                            product.setUpcA(walmartlabs.getItems().get(0).getUpc());
                        }
                        
                        subscriber.onNext(product);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });
    }
}
