package com.whooo.babr.view.scan.camera;

import android.os.Parcelable;

import com.google.repacked.antlr.v4.runtime.misc.Nullable;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.vo.Product;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class CameraPresenter implements CameraContract.Presenter {

    private CameraContract.View mView;
    private final ProductRepository mRepository;
    private Subscription mSubscription = Subscriptions.empty();

    public CameraPresenter(CameraContract.View mView, ProductRepository repository) {
        this.mView = mView;
        mRepository = repository;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
        mSubscription = null;
    }

    @Override
    public void searchProducts(@Nullable String code) {
        // TODO: 7/8/16 check NPE

        unsubscribe();
        mView.showProgress(true);
        mView.playRingtone();

        mSubscription = mRepository
                .scanProducts(code)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .first()
                .subscribe();

        // TODO: 7/8/16 add action
    }

    @Override
    public void onRequestSuccess(Parcelable parcelable) {

    }

    @Override
    public void onRequestSuccessList(List<Product> parcelables) {

    }
}
