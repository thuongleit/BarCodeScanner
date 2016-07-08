package com.whooo.babr.data.remote.searchupc;

import android.util.Log;

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

@Singleton
public class SearchUpcParseService {


    @Inject
    public SearchUpcParseService() {
    }

    public Observable<Product> getProductSearchUpc(final String code) {
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {
                Log.d("passedScanner", "getProductSearchUpc" + code);
                Product product = new Product();
                Retrofit retrofit = new Retrofit.Builder()
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://www.searchupc.com/handlers/")
                        .build();
                SearchUpcService upcService = retrofit.create(SearchUpcService.class);
                Call<SearchUpc> observable = upcService.getProductSearchUpc(code);

                observable.enqueue(new Callback<SearchUpc>() {
                    @Override
                    public void onResponse(Call<SearchUpc> call, Response<SearchUpc> response) {
                        SearchUpc searchUpc = response.body();
                        product.source = "searchupc.com";
                        product.listId = "a";

                        if (searchUpc != null && searchUpc.get0() != null) {
                            if (searchUpc.get0().getImageurl() != null) {
                                product.imageUrl = searchUpc.get0().getImageurl();
                            }
                            if (searchUpc.get0().getProductname() != null) {
                                product.name = searchUpc.get0().getProductname();
                            }
                            subscriber.onNext(product);
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<SearchUpc> call, Throwable t) {

                    }
                });
            }
        });
    }


}
