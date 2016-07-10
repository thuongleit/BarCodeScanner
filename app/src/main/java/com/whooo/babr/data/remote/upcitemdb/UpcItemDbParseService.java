package com.whooo.babr.data.remote.upcitemdb;

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
public class UpcItemDbParseService implements ParseService {

    private final RetrofitService mService;

    @Inject
    public UpcItemDbParseService( UpcItemDbParseService.RetrofitService service) {
        mService = service;
    }

    @Override
    public Observable<List<Product>> searchProductsByCode(@NonNull String code) {
        return mService
                .getProducts(code)
                .filter(item -> item.nodes != null && !item.nodes.isEmpty())
                .flatMap(item -> Observable.from(item.nodes))
                .flatMap(childNode -> {
                    Product product = new Product();

                    product.source = ProductSource.UPC_ITEM_DB.getDisplay();
                    product.listId = "a";
                    product.name = childNode.title;
                    product.ean = childNode.ean;
                    product.upcA = childNode.upc;
                    if (!childNode.images.isEmpty()) {
                        product.imageUrl = childNode.images.get(0);
                    }
                    return Observable.just(product);
                })
                .toSortedList();
    }

    public interface RetrofitService {
        @GET("prod/trial/lookup")
        Observable<Item> getProducts(@Query("upc") String code);
    }
}
