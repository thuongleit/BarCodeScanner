package com.whooo.babr.view.main;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.repacked.antlr.v4.runtime.misc.Nullable;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.util.FirebaseUtils;
import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void getProducts() {
        mProductRepository.getProducts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(products -> {
                    if (products != null && products.size() > 0) {
                        mView.onLoadProductsSuccess(products);
                    }
                });
    }

    @Override
    public void saveProducts(List<Product> products) {
        mProductRepository.saveProducts(products).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(isSuccess -> {
                            if (isSuccess) {
                                mView.onSaveProductsSuccess();
                            }
                        }, Throwable::printStackTrace
                        , () -> {

                        });
    }

    @Override
    public void saveProductsHistory(CheckoutHistory history, List<Product> products) {
        mProductRepository.saveProductsHistory(history, products).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(isSuccess -> {
                            if (isSuccess) {
                                mView.onSaveProductsSuccess();
                            }
                        }, Throwable::printStackTrace
                        , () -> {

                        });
    }


    @Override
    public void removeProducts(Product product) {
        mProductRepository.removeProduct(product).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(isSuccess -> {
                            if (isSuccess) {
                                mView.onRemoveProductsSuccess();
                            }
                        }, Throwable::printStackTrace
                        , () -> {

                        });
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
