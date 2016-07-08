package com.whooo.babr.data.remote.walmartlabs;

import android.support.annotation.NonNull;

import com.whooo.babr.data.product.ProductSource;
import com.whooo.babr.data.remote.ParseService;
import com.whooo.babr.vo.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

@Singleton
public class WalmartlabsParseService implements ParseService {

    private static final String API_KEY = "7z4t6jsjt8g3u5pm6e52xrer";

    private final RetrofitService mService;

    @Inject
    public WalmartlabsParseService( WalmartlabsParseService.RetrofitService service) {
        mService = service;
    }

    @Override
    public Observable<List<Product>> searchProductsByCode(@NonNull String code) {
        return mService
                .getProducts(API_KEY, code)
                .filter(item -> item.items != null && !item.items.isEmpty())
                .flatMap(item -> Observable.from(item.items))
                .flatMap(childNode -> {
                    Product product = new Product();

                    product.source = ProductSource.WALMART.getDisplay();
                    product.name = childNode.name;
                    product.imageUrl = childNode.largeImage;
                    product.upcA = childNode.upc;
                    product.model = childNode.brandName;

                    return Observable.just(product);
                })
                .toSortedList();
    }

    public interface RetrofitService {

        @GET("items")
        Observable<Item> getProducts(@Query("apiKey") String apiKey, @Query("upc") String code);
    }
}
