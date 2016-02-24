package com.thuongleit.babr.data;

import android.app.Application;

import com.thuongleit.babr.BarApplication;
import com.thuongleit.babr.config.Config;
import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.data.local.ProductModel;
import com.thuongleit.babr.data.remote.ParseService;
import com.thuongleit.babr.data.remote.ProductService;
import com.thuongleit.babr.vo.Product;

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
    ProductService mProductService;
    @Inject
    ParseService mParseService;
    @Inject
    Config mConfig;

    @Inject
    public DataManager(Application app) {
        ((BarApplication) app).getAppComponent().inject(this);
    }

    public Observable<Product> getProduct(String qrCode) {
        return mProductService.getProduct(Constant.UPC_ENDPOINT_URL + qrCode)
                .doOnNext(product -> {
                    //save to db
                    mProductModel.saveProduct(product);

                    if (mConfig.isUserLogin()) {
                        //save to parse service
                        mParseService.saveProduct(product);
                    }
                });
    }

    public Observable<List<Product>> getProducts() {
        if (mConfig.isUserLogin()) {
            return Observable.create(subscriber -> {
                List<Product> products = new ArrayList<>();
                mParseService.getProducts().doOnNext(product -> {
                    products.add(product);
                    subscriber.onNext(products);
                }).doOnCompleted(() -> subscriber.onCompleted())
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
}
