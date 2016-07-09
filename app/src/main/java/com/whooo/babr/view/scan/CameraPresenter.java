package com.whooo.babr.view.scan;

import com.google.repacked.antlr.v4.runtime.misc.Nullable;
import com.whooo.babr.data.product.ProductRepository;

import java.io.IOException;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

class CameraPresenter implements CameraContract.Presenter {

    private CameraContract.View mView;
    private final ProductRepository mRepository;
    private Subscription mSubscription = Subscriptions.empty();

    CameraPresenter(CameraContract.View mView, ProductRepository repository) {
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
                .searchProducts(code)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .first()
                .subscribe(products -> {
                            if (products.isEmpty()) {
                                mView.onEmptyResponse();
                            } else {
                                mView.onSearchSuccess(products);
                            }
                        },
                        e -> {
                            mView.showProgress(false);
                            if (e instanceof IOException) {
                                mView.showNetworkError();
                            } else {
                                mView.showInAppError();
                            }
                        }, () -> mView.showProgress(false));
    }
}
