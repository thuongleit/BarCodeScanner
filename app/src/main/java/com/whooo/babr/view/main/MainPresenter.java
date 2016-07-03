package com.whooo.babr.view.main;

import com.whooo.babr.data.DataManager;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class MainPresenter implements MainContract.Presenter {

    private  DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();

    public MainPresenter() {
    }

//    public void getProducts() {
//        checkViewAttached();
//        mView.showProgress(true);
//        mSubscription = mDataManager
//                .getProducts()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        products -> {
//                            if (products == null || products.isEmpty()) {
//                                mView.showEmptyView();
//                            } else {
//                                mView.showProducts(products);
//                            }
//                        },
//                        e -> {
//                            mView.showProgress(false);
//                            if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
//                                mView.onNetworkFailed();
//                            } else {
//                                mView.onGeneralFailed(e.getMessage());
//                            }
//                        }
//                        , () -> mView.showProgress(false));
//    }

    public void getProductsNotCheckout(String listId) {
        mSubscription = mDataManager
                .getProductsCheckout(listId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        products -> {
                            if (products == null || products.isEmpty()) {
//                                mView.showEmptyView();
                            } else {
//                                mView.showProducts(products);
                            }
                        },
                        e -> {
//                            mView.showProgress(false);
                            if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
//                                mView.onNetworkFailed();
                            } else {
//                                mView.onGeneralFailed(e.getMessage());
                            }
                        });
//                        , () -> mView.showProgress(false));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onDestroy() {

    }
}
