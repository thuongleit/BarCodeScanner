package com.thuongleit.babr.view.main;

import com.thuongleit.babr.data.DataManager;
import com.thuongleit.babr.view.base.BasePresenter;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by thuongle on 1/31/16.
 */
public class MainPresenter extends BasePresenter<MainView> {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void getProducts() {
        checkViewAttached();
        mView.showProgress(true);
        mSubscription = mDataManager
                .getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        products -> {
                            if (products == null || products.isEmpty()) {
                                mView.showEmptyView();
                            } else {
                                mView.showProducts(products);
                            }
                        },
                        e -> {
                            mView.showProgress(false);
                            if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                                mView.showNetworkError();
                            } else {
                                mView.showGeneralError(e.getMessage());
                            }
                        }
                        , () -> mView.showProgress(false));
    }
}
