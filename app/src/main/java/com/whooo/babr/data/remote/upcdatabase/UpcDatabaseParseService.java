package com.whooo.babr.data.remote.upcdatabase;

import android.support.annotation.NonNull;

import com.whooo.babr.data.product.ProductSource;
import com.whooo.babr.data.remote.ParseService;
import com.whooo.babr.vo.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

@Singleton
public class UpcDatabaseParseService implements ParseService {
    private static final String API_KEY = "7f98df7128b330bb6041bd9741f58683";

    private final RetrofitService mService;

    @Inject
    public UpcDatabaseParseService( UpcDatabaseParseService.RetrofitService service) {
        mService = service;
    }

    @Override
    public Observable<List<Product>> searchProductsByCode(@NonNull String code) {
        return mService
                .getProductUpcDatabase(API_KEY, code)
                .filter(item -> item.valid)
                .flatMap(item -> {
                    Product product = new Product();

                    product.source = ProductSource.UPC_DATABASE.getDisplay();
                    product.name = item.name;
                    product.upc = item.upc;

                    return Observable.just(product);
                })
                .toList();

    }

    public interface RetrofitService {

        @GET("json/{api-key}/{code}")
        Observable<Item> getProductUpcDatabase(@Path("api-key") String apiKey, @Path("code") String code);
    }
}
