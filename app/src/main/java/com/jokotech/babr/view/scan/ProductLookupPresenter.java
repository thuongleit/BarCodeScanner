package com.jokotech.babr.view.scan;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jokotech.babr.data.DataManager;
import com.jokotech.babr.data.remote.amazon.model.AmazonProductResponse;
import com.jokotech.babr.di.ApplicationScope;
import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.vo.Product;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * Created by thuongle on 1/3/16.
 */
public class ProductLookupPresenter extends BasePresenter<ScanView> {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    @ApplicationScope
    Context mContext;

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


        Observable<List<Product>> observableUpcItemDb = mDataManager.getProductUpcItemDb(code);
        Observable<List<Product>> observableBabr = mDataManager.getProductsBABR(code);
        Observable<List<Product>> observableWalmartlabs = mDataManager.getProductWalmartlabs(code);
        Observable<List<Product>> observableUpcDatabase = mDataManager.getProductUpcDatabase(code);
        Observable<List<Product>> observableCheckoutScan = mDataManager.getProductsCheckoutScan(code);
        Observable<List<Product>> observableSearchUpc = mDataManager.getProductSearchUpc(code);
        Observable<AmazonProductResponse> observableAmazon = mDataManager.searchProductsInAmazon(code);

        Observable<List<Product>> merge = Observable.merge(observableUpcItemDb, observableWalmartlabs, observableBabr, observableUpcDatabase,
                observableCheckoutScan, observableSearchUpc);

        compositeSubscription.add(Observable.combineLatest(merge, observableAmazon, this::setupData)
                .first(data -> isHavingImage(data))
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object -> {

                            if (object instanceof AmazonProductResponse) {
                                AmazonProductResponse reponse = (AmazonProductResponse) object;
                                mView.onRequestSuccess(reponse);
                            } else {
                                List<Product> reponse = (List<Product>) object;
                                mView.onRequestSuccessList(reponse);
                            }
                            stopConcurrencyExe();

                        }, e -> {
                            if (e instanceof IOException) {
                                mView.showNetworkError();
                            } else {
                                mView.showGeneralError(e.getMessage());
                            }
                        },
                        () -> mView.showProgress(false)));


    }

    private boolean isHavingImage(Object object) {
        if (object instanceof AmazonProductResponse) {
            return true;
        } else {
            List<Product> reponse = (List<Product>) object;
            if (!TextUtils.isEmpty(reponse.get(0).getImageUrl())) {
                return true;
            }
        }
        return false;
    }

    private Object setupData(List<Product> products, AmazonProductResponse response) {

        if (response.getProducts().size()>0 ) {
            return response;
        } else {
            return products;
        }

    }

    private void stopConcurrencyExe() {
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }


}
