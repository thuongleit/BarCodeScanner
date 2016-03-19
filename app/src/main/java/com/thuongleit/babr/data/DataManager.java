package com.thuongleit.babr.data;

import android.app.Application;

import com.thuongleit.babr.BarApplication;
import com.thuongleit.babr.config.Config;
import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.data.local.ProductModel;
import com.thuongleit.babr.data.remote.ParseService;
import com.thuongleit.babr.data.remote.amazon.AmazonParseService;
import com.thuongleit.babr.data.remote.amazon.AmazonService;
import com.thuongleit.babr.data.remote.amazon.model.AmazonProductResponse;
import com.thuongleit.babr.data.remote.amazon.util.AmazonSignedRequestsHelper;
import com.thuongleit.babr.data.remote.searchupc.SearchUpcParseService;
import com.thuongleit.babr.data.remote.upc.UpcParseService;
import com.thuongleit.babr.data.remote.upcdatabase.UpcDatabaseParseService;
import com.thuongleit.babr.data.remote.upcitemdb.UpcItemDbParseService;
import com.thuongleit.babr.vo.Product;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by thuongle on 1/30/16.
 */
@Singleton
public class DataManager {

    @Inject
    ProductModel mProductModel;
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
    UpcItemDbParseService upcItemDbParseService;



    @Inject
    public DataManager(Application app) {
        ((BarApplication) app).getAppComponent().inject(this);
    }

    public Observable<Product> getProduct(String qrCode) {
        return mUpcParseService.getProduct(Constant.UPC_ENDPOINT_URL + qrCode)
                .doOnNext(product -> {
                    //save to db
                    mProductModel.saveProduct(product);

                    if (mConfig.isUserLogin()) {
                        //save to parse service
                        mParseService.saveProduct(product);
                    }
                });
    }

    //missing check login
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

    //missing check login
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

    //missing check login
    public Observable<List<Product>> getProductUpcItemDb(String code) {

        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<Product>();
            upcItemDbParseService.getUpcItemDbParseService(code).doOnNext(product -> {
                products.add(product);
            }).doOnCompleted(() -> {
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }


    public Observable<List<Product>> getProducts() {
        if (mConfig.isUserLogin()) {
            return Observable.create(subscriber -> {
                List<Product> products = new ArrayList<>();
                mParseService.getProducts().doOnNext(product -> {
                    products.add(product);

                }).doOnCompleted(() -> {
                    subscriber.onNext(products);
                    subscriber.onCompleted();
                })
                        .subscribeOn(Schedulers.newThread())
                        .subscribe();
            });
        } else {
            return Observable.create(subscriber -> {
                try {
                    subscriber.onNext(mProductModel.loadProducts());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            });
        }
    }


    public Observable<List<Product>> getProductsBABR(String id) {

        return Observable.create(subscriber -> {
            List<Product> products = new ArrayList<>();
            mParseService.getProductBABR(id).doOnNext(product -> {
                products.add(product);
            }).doOnCompleted(() ->{
                subscriber.onNext(products);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe();
        });
    }

    public Observable<AmazonProductResponse> searchProductsInAmazon(String keyword) {
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
