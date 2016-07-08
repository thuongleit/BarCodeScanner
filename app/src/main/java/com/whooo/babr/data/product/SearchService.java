package com.whooo.babr.data.product;

import com.whooo.babr.config.Constant;
import com.whooo.babr.data.remote.ParseService;
import com.whooo.babr.data.remote.amazon.AmazonParseService;
import com.whooo.babr.data.remote.amazon.util.AmazonSignedRequestsHelper;
import com.whooo.babr.data.remote.searchupc.SearchUpcParseService;
import com.whooo.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.whooo.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.whooo.babr.data.remote.walmartlabs.WalmartlabsParseService;
import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    private final ParseService mAppService;

    @Inject
    public SearchService(SearchUpcParseService upcParseService,
                         WalmartlabsParseService walmartService,
                         UpcItemDbParseService upcItemDbService,
                         UpcDatabaseParseService upcDatabaseService,
                         AmazonParseService amazonService,
                         ParseService service) {
        this.mUpcService = upcParseService;
        this.mWalmartService = walmartService;
        this.mUpcItemDbService = upcItemDbService;
        this.mUpcDatabaseService = upcDatabaseService;
        this.mAmazonService = amazonService;
        this.mAppService = service;
    }

    public Observable<List<Product>> searchProducts(ProductSource source, String code) {
        switch (source) {
            case UPC:
                return mUpcService.getProductSearchUpc(code).toList();
            case WALMART:
                return mWalmartService.getProductWalmart(code).toList();
            case UPC_ITEM_DB:
                return mUpcItemDbService.getUpcItemDbParseService(code).toList();
            case UPC_DATABASE:
                return mUpcDatabaseService.getProductUpcDatabase(code).toList();
            case AMAZON:
                String signedUrl = null;
                try {
                    AmazonSignedRequestsHelper signer = AmazonSignedRequestsHelper.
                            getInstance(Constant.AWS_ACCESS_KEY_ID, Constant.AWS_SECRET_KEY);
                    signedUrl = signer.sign(code);
                } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
                    // if the key is invalid due to a programming error
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                if (signedUrl != null) {
                    return mAmazonService.parse(signedUrl).toList();
                }
                return Observable.empty();
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
