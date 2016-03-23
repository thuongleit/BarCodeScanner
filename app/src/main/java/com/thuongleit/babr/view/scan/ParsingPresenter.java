package com.thuongleit.babr.view.scan;

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
 * Created by thuongle on 2/25/16.
 */
public class ParsingPresenter extends BasePresenter<ParsingView> {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public ParsingPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void parse(String detailPageURL) {
        checkViewAttached();
        mSubscription = mDataManager
                .parseProductFromAmazon(detailPageURL)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        product -> mView.onParseSuccess(product),
                        e -> {
                            if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                                mView.showNetworkError();
                            } else {
                                mView.showGeneralError(e.getMessage());
                            }
                        });
    }
}
