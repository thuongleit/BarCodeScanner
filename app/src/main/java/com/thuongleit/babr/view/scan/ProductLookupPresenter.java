package com.thuongleit.babr.view.scan;

import android.content.Context;
import android.widget.Toast;

import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.data.DataManager;
import com.thuongleit.babr.di.ActivityScope;
import com.thuongleit.babr.di.ApplicationScope;
import com.thuongleit.babr.view.base.BasePresenter;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * Created by thuongle on 1/3/16.
 */
public class ProductLookupPresenter extends BasePresenter<ScanView> {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();

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

        switch (service) {
            case Constant.KEY_AMAZON_SERVICE:
                Timber.d("KEY_AMAZON_SERVICE"+ code);
                mSubscription = mDataManager
                        .searchProductsInAmazon(code)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getProducts() == null || response.getProducts().isEmpty()) {
                                        mView.onEmptyProductReturn();
                                    } else {
                                        Toast.makeText(mContext, "AMAZON_SERVICE", Toast.LENGTH_SHORT).show();
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
                Timber.d("KEY_UPC_SERVICE"+ code);
                mSubscription = mDataManager
                        .getProduct(code)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(product -> {
                                    if (product.getImageUrl().isEmpty()||product == null) {
                                        mView.onEmptyProductReturn();
                                    } else {
                                        Toast.makeText(mContext, "UPC_SERVICE", Toast.LENGTH_SHORT).show();

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

            case Constant.KEY_BABR:
                Timber.d("KEY_BABR: " + code);
                mSubscription =
                        mDataManager.getProductsBABR(code)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                products -> {
                                    if (products == null || products.size()==0) {
                                        mView.onEmptyProductReturn();
                                    } else {
                                        Toast.makeText(mContext, "BABR", Toast.LENGTH_SHORT).show();

                                        mView.onRequestSuccessList(products);
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

            case Constant.KEY_SEACHUPC:
                Timber.d("KEY_SEACHUPC: " + code);
                mSubscription =
                        mDataManager.getProductSearchUpc(code)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        products -> {
                                            if (products == null || products.size()==0) {
                                                mView.onEmptyProductReturn();
                                            } else {
                                                Toast.makeText(mContext, "SEACHUPC", Toast.LENGTH_SHORT).show();

                                                mView.onRequestSuccessList(products);
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
            case Constant.KEY_UPCDATABASE:
                Timber.d("KEY_UPCDATABASE: " + code);
                mSubscription =
                        mDataManager.getProductUpcDatabase(code)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        products -> {
                                            if (products == null || products.size()==0) {
                                                mView.onEmptyProductReturn();
                                            } else {
                                                Toast.makeText(mContext, "UPCDATABASE", Toast.LENGTH_SHORT).show();

                                                mView.onRequestSuccessList(products);
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
        }
    }
}
