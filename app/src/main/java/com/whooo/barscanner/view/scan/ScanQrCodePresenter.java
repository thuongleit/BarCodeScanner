package com.whooo.barscanner.view.scan;

import com.whooo.barscanner.data.DataManager;
import com.whooo.barscanner.view.base.BasePresenter;
import com.whooo.barscanner.vo.Product;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by thuongle on 1/3/16.
 */
public class ScanQrCodePresenter extends BasePresenter<ScanQrCodeView> {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public ScanQrCodePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void executeQrCode(final String qrCode) {
        checkViewAttached();
        mView.showProgress(true);

        mSubscription = mDataManager.getProduct(qrCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Product>() {
                    @Override
                    public void onCompleted() {
                        mView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showProgress(false);
                        mView.showGeneralError(e.getMessage());
                    }

                    @Override
                    public void onNext(Product product) {
                        mView.onExecuteFinished(product);
                    }
                });

    }
}
