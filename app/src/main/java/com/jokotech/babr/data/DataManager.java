package com.jokotech.babr.data;

import android.app.Application;
import android.util.Log;

import com.jokotech.babr.BarApplication;
import com.jokotech.babr.config.Config;
import com.jokotech.babr.config.Constant;
import com.jokotech.babr.data.remote.ParseService;
import com.jokotech.babr.data.remote.amazon.AmazonParseService;
import com.jokotech.babr.data.remote.amazon.AmazonService;
import com.jokotech.babr.data.remote.amazon.model.AmazonProductResponse;
import com.jokotech.babr.data.remote.amazon.util.AmazonSignedRequestsHelper;
import com.jokotech.babr.data.remote.searchupc.SearchUpcParseService;
import com.jokotech.babr.data.remote.upc.UpcParseService;
import com.jokotech.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.jokotech.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.jokotech.babr.data.remote.walmartlabs.WalmartlabsParseService;
import com.jokotech.babr.vo.CheckoutHistory;
import com.jokotech.babr.vo.Product;

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
public class DataManager {

    @Inject
    UpcParseService mUpcParseService;
    @Inject
    AmazonParseService mAmazonParseService;
    @Inject
    ParseService mParseService;
    @Inject
    Config mConfig;
    @Inject
    AmazonService mAmazonService;
    @Inject
    SearchUpcParseService searchUpcParseService;
    @Inject
    UpcDatabaseParseService upcDatabaseParseService;
    @Inject
    WalmartlabsParseService walmartlabsParseService;
    @Inject
    UpcItemDbParseService upcItemDbParseService;


    @Inject
    public DataManager(Application app) {
        ((BarApplication) app).getAppComponent().inject(this);
    }


    public Observable<List<Product>> getProductSearchUpc(String code) {

        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<Product>();
            searchUpcParseService.getProductSearchUpc(code).doOnNext(product -> {
                products.add(product);
            }).doOnCompleted(() -> {
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }

    public Observable<List<Product>> getProductUpcDatabase(String code) {

        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<Product>();
            upcDatabaseParseService.getProductUpcDatabase(code).doOnNext(product -> {
                products.add(product);
            }).doOnCompleted(() -> {
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }

    public Observable<List<Product>> getProductWalmartlabs(String code) {
        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<Product>();
            walmartlabsParseService
                    .getProductWalmart(code)
                    .doOnNext(product -> {
                        products.add(product);
                    })
                    .doOnCompleted(() -> {
                        subscriber.onNext(products);
                        subscriber.onCompleted();
                    })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }

    //missing check login
    public Observable<List<Product>> getProductUpcItemDb(String code) {

        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<Product>();
            upcItemDbParseService
                    .getUpcItemDbParseService(code)
                    .doOnNext(product -> products.add(product))
                    .doOnCompleted(() -> {
                        subscriber.onNext(products);
                        subscriber.onCompleted();
                    })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }


    public Observable<List<Product>> getProductsCheckout(String listId) {
        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<>();
            mParseService.getProductsCheckout(listId).doOnNext(product -> {
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
            mParseService.getProductsCheckoutScan(listId).doOnNext(product -> {
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
            mParseService.getProductsHistory().doOnNext(product -> {
                products.add(product);

            }).doOnCompleted(() -> {
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }


    public Observable<List<Product>> getProductsBABR(String id) {

        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<>();
            mParseService.getProductBABR(id).doOnNext(product -> {
                products.add(product);
            }).doOnCompleted(() -> {
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }

    public Observable<AmazonProductResponse> searchProductsInAmazon(String keyword) {
        Log.d("passedScanner", "searchProductsInAmazon" + keyword);
        String signedUrl = null;
        try {
            AmazonSignedRequestsHelper signer = AmazonSignedRequestsHelper.
                    getInstance(Constant.AWS_ACCESS_KEY_ID, Constant.AWS_SECRET_KEY);
            signedUrl = signer.sign(keyword);
        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            // if the key is invalid due to a programming error
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (signedUrl != null) {
            return mAmazonService.search(signedUrl);
        }
        return null;
    }


    public Observable<Product> parseProductFromAmazon(String detailPageURL) {
        return mAmazonParseService.parse(detailPageURL);
    }

}
