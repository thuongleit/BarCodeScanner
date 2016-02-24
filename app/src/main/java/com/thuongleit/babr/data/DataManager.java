package com.thuongleit.babr.data;

import android.app.Application;

import com.thuongleit.babr.BarApplication;
import com.thuongleit.babr.config.Config;
import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.data.local.ProductModel;
import com.thuongleit.babr.data.remote.ParseService;
import com.thuongleit.babr.data.remote.amazon.model.AmazonProductResponse;
import com.thuongleit.babr.data.remote.amazon.AmazonService;
import com.thuongleit.babr.data.remote.amazon.model.AmazonSearchProduct;
import com.thuongleit.babr.data.remote.amazon.util.AmazonSignedRequestsHelper;
import com.thuongleit.babr.data.remote.upc.ProductService;
import com.thuongleit.babr.vo.UpcProduct;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
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
    AmazonService mAmazonService;

    @Inject
    public DataManager(Application app) {
        ((BarApplication) app).getAppComponent().inject(this);
    }

    public Observable<UpcProduct> getProduct(String qrCode) {
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

    public Observable<List<UpcProduct>> getProducts() {
        if (mConfig.isUserLogin()) {
            return Observable.create(subscriber -> {
                List<UpcProduct> upcProducts = new ArrayList<>();
                mParseService.getProducts().doOnNext(product -> {
                    upcProducts.add(product);
                    subscriber.onNext(upcProducts);
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

    public Observable<List<UpcProduct>> searchProductsInAmazon(String keyword) {

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
            return mAmazonService.search(signedUrl).map(new Func1<AmazonProductResponse, List<UpcProduct>>() {
                @Override
                public List<UpcProduct> call(AmazonProductResponse amazonProductResponse) {
                    List<AmazonSearchProduct> amazonProducts = amazonProductResponse.getProducts();
                    for (AmazonSearchProduct product : amazonProducts) {

                    }

                    return null;
                }
            });
        }
        return null;
    }
}
