package com.whooo.barscanner.view.main;

import com.whooo.barscanner.data.DataManager;
import com.whooo.barscanner.view.base.BasePresenter;
import com.whooo.barscanner.vo.Product;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
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
        mSubscription = mDataManager.getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Product>>() {
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
                    public void onNext(List<Product> products) {
                        mView.showProducts(products);
                    }
                });
    }
}
