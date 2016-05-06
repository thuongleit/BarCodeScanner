package com.jokotech.babr.data.remote.searchupc;

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
 * Created by ThongLe on 3/18/2016.
 */
@Singleton
public class SearchUpcParseService {


    @Inject
    public SearchUpcParseService() {
    }

    public Observable<Product> getProductSearchUpc(final String code) {
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {
                Timber.d("getProductSearchUpc" + code);
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
                        product.setSource("searchupc.com");
                        product.setListId("a");

                        if (searchUpc != null && searchUpc.get0() != null) {
                            if (searchUpc.get0().getImageurl() != null) {
                                product.setImageUrl(searchUpc.get0().getImageurl());
                            }
                            if (searchUpc.get0().getProductname() != null) {
                                product.setName(searchUpc.get0().getProductname());
                            }
                            subscriber.onNext(product);
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<SearchUpc> call, Throwable t) {

                    }
                });

//                Call<List<_0>> observable = upcService.getProductSearchUpc(code);
//
//                observable.enqueue(new Callback<List<_0>>() {
//                    @Override
//                    public void onResponse(Response<List<_0>> response, Retrofit retrofit) {
//                        List<_0> list=response.body();
//                        Log.d("passedonResponse",String.valueOf(list.size()));
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//
//                    }
//                });
            }
        });
    }


}
