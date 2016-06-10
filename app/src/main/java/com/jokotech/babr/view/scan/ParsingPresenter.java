package com.jokotech.babr.view.scan;

import com.jokotech.babr.data.DataManager;

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
public class ParsingPresenter {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();
    private SearchResultActivity mView;

    @Inject
    public ParsingPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void detachView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void parse(String detailPageURL) {
        mView.showProcess(true);
        mSubscription = mDataManager
                .parseProductFromAmazon(detailPageURL)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        product -> mView.onParseSuccess(product),
                        e -> {
                            if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                                mView.onNetworkFailed();
                                mView.showProcess(false);
                            } else {
                                mView.onGeneralFailed(e.getMessage());
                                mView.showProcess(false);
                            }
                        });
    }
}
