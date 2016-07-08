package com.whooo.babr.data.product;

import com.whooo.babr.data.remote.ParseServiceOK;
import com.whooo.babr.data.remote.amazon.AmazonParseService;
import com.whooo.babr.data.remote.searchupc.SearchUpcParseService;
import com.whooo.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.whooo.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.whooo.babr.data.remote.walmartlabs.WalmartlabsParseService;
import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class SearchService {

    private final SearchUpcParseService mUpcService;
    private final WalmartlabsParseService mWalmartService;
    private final UpcItemDbParseService mUpcItemDbService;
    private final UpcDatabaseParseService mUpcDatabaseService;
    private final AmazonParseService mAmazonService;
    private final ParseServiceOK mAppService;

    @Inject
    public SearchService(SearchUpcParseService upcParseService,
                         WalmartlabsParseService walmartService,
                         UpcItemDbParseService upcItemDbService,
                         UpcDatabaseParseService upcDatabaseService,
                         AmazonParseService amazonService,
                         ParseServiceOK service) {
        this.mUpcService = upcParseService;
        this.mWalmartService = walmartService;
        this.mUpcItemDbService = upcItemDbService;
        this.mUpcDatabaseService = upcDatabaseService;
        this.mAmazonService = amazonService;
        this.mAppService = service;
    }

    public Observable<List<Product>> searchProducts(ProductSource source, String code) {
        switch (source) {
            case SEARCH_UPC:
                return mUpcService.searchProductsByCode(code);
            case WALMART:
                return mWalmartService.searchProductsByCode(code);
            case UPC_ITEM_DB:
                return mUpcItemDbService.searchProductsByCode(code);
            case UPC_DATABASE:
                return mUpcDatabaseService.searchProductsByCode(code);
            case AMAZON:
                return mAmazonService.searchProductsByCode(code);
            case IN_APP:
                return mAppService.getProductBABR(code).toList();
            default:
                return Observable.error(new IllegalArgumentException("Need to put right product source"));

        }
    }

    public Observable<List<Product>> getProductsCheckout(String listId) {
        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<>();
            mAppService.getProductsCheckout(listId).doOnNext(product -> {
                products.add(product);

            }).doOnCompleted(() -> {
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }

    public Observable<List<Product>> getProductsCheckoutScan(String listId) {

        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<>();
            mAppService.getProductsCheckoutScan(listId).doOnNext(product -> {
                products.add(product);

            }).doOnCompleted(() -> {
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }

    public Observable<List<CheckoutHistory>> getProductsHistory() {
        return Observable.create(subscriber -> {
            List<CheckoutHistory> products = new ArrayList<>();
            mAppService.getProductsHistory().doOnNext(product -> {
                products.add(product);

            }).doOnCompleted(() -> {
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }
}
