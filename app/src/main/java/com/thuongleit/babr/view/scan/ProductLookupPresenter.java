package com.thuongleit.babr.view.scan;

import com.thuongleit.babr.config.Constant;
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
 * Created by thuongle on 1/3/16.
 */
public class ProductLookupPresenter extends BasePresenter<ScanView> {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public ProductLookupPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void execute(final String code, String service) {
        checkViewAttached();
        mView.showProgress(true);
        mView.playRingtone();

        switch (service) {
            case Constant.KEY_AMAZON_SERVICE:
                mSubscription = mDataManager
                        .searchProductsInAmazon(code)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getProducts() == null || response.getProducts().isEmpty()) {
                                        mView.onEmptyProductReturn();
                                    } else {
                                        mView.onRequestSuccess(response);
                                    }
                                }, e -> {
                                    if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                                        mView.showNetworkError();
                                    } else {
                                        mView.showGeneralError(e.getMessage());
                                    }
                                },
                                () -> mView.showProgress(false));
                break;
            case Constant.KEY_UPC_SERVICE:
                mSubscription = mDataManager
                        .getProduct(code)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(product -> {
                                    if (product == null) {
                                        mView.onEmptyProductReturn();
                                    } else {
                                        mView.onRequestSuccess(product);
                                    }
                                },
                                e -> {
                                    if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                                        mView.showNetworkError();
                                    } else {
                                        mView.showGeneralError(e.getMessage());
                                    }
                                }, () -> mView.showProgress(false));
                break;
        }
    }
}
