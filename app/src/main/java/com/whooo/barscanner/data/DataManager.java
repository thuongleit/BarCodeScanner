package com.whooo.barscanner.data;

import android.app.Application;

import com.whooo.barscanner.BarApplication;
import com.whooo.barscanner.config.Config;
import com.whooo.barscanner.config.Constant;
import com.whooo.barscanner.data.local.ProductModel;
import com.whooo.barscanner.data.remote.ParseService;
import com.whooo.barscanner.data.remote.ProductService;
import com.whooo.barscanner.vo.Product;

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
