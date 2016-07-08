package com.whooo.babr.data.remote.searchupc;

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
public class SearchUpcParseService implements ParseService {
    private static final String REQUEST_TYPE = "3";
    private static final String ACCESS_TOKEN = "B26844C1-236A-4F4D-AB78-8676B1E3E0CA";

    private final RetrofitService mService;

    @Inject
    public SearchUpcParseService(SearchUpcParseService.RetrofitService retrofitService) {
        mService = retrofitService;
    }

    @Override
    public Observable<List<Product>> searchProductsByCode(@NonNull String code) {
        return mService
                .getProducts(REQUEST_TYPE, ACCESS_TOKEN, code)
                .filter(item -> item.nodes != null && !item.nodes.isEmpty())
                .flatMap(item -> Observable.from(item.nodes))
                .flatMap(childNode -> {
                    Product product = new Product();
                    product.source = ProductSource.SEARCH_UPC.getDisplay();
                    product.listId = "a";

                    product.name = childNode.productName;
                    product.imageUrl = childNode.imageUrl;
                    product.upcA = code;

                    // TODO: 7/8/16 Store price, currency?

                    return Observable.just(product);
                })
                .toSortedList();
    }

    public interface RetrofitService {
        @GET("upcsearch.ashx")
        Observable<Item> getProducts(@Query("request_type") String requestType,
                                     @Query("access_token") String accessToken,
                                     @Query("upc") String code);
    }
}
