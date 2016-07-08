package com.whooo.babr.view.main;

import android.support.annotation.NonNull;

import com.google.repacked.antlr.v4.runtime.misc.Nullable;
import com.whooo.babr.data.product.ProductRepository;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    @NonNull
    private final ProductRepository mProductRepository;
    private Object products;

    public MainPresenter(@Nullable MainContract.View view, @NonNull ProductRepository productRepository) {
        mView = view;
        mProductRepository = productRepository;
    }

    @Override
    public void subscribe() {
        getProducts();
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    public void getProducts() {
        mProductRepository
                .getProducts();
    }

//    public void getProducts() {
//        checkViewAttached();
//        mView.showProgress(true);
//        mSubscription = mDataManager
//                .getProducts()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        nodes -> {
//                            if (nodes == null || nodes.isEmpty()) {
//                                mView.showEmptyView();
//                            } else {
//                                mView.showProducts(nodes);
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

//    public void getProductsNotCheckout(String listId) {
//        mSubscription = mDataManager
//                .getProductsCheckout(listId)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        nodes -> {
//                            if (nodes == null || nodes.isEmpty()) {
////                                mView.showEmptyView();
//                            } else {
////                                mView.showProducts(nodes);
//                            }
//                        },
//                        e -> {
////                            mView.showProgress(false);
//                            if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
////                                mView.onNetworkFailed();
//                            } else {
////                                mView.onGeneralFailed(e.getMessage());
//                            }
//                        });
////                        , () -> mView.showProgress(false));
//    }
}
