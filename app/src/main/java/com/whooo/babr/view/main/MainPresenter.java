package com.whooo.babr.view.main;

import android.support.annotation.NonNull;

import com.google.repacked.antlr.v4.runtime.misc.Nullable;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    @NonNull
    private final ProductRepository mProductRepository;
    private final MainViewModel mViewModel;
    private CompositeSubscription mSubscriptions;

    MainPresenter(@Nullable MainContract.View view, MainViewModel viewModel, @NonNull ProductRepository productRepository) {
        mView = view;
        mProductRepository = productRepository;
        this.mViewModel = viewModel;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        getProducts();
    }

    @Override
    public void unsubscribe() {
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
        mSubscriptions = null;
    }

    @Override
    public void getProducts() {
        unsubscribe();
        mViewModel.setIsLoading();
//        mSubscriptions.add(
//                mProductRepository
//                        .getProducts()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .unsubscribeOn(Schedulers.io())
//                        .subscribe(mViewModel::setData,
//                                e -> {
//                                    if (e instanceof FirebaseNetworkException) {
//                                        mViewModel.setNetworkError();
//                                    } else {
//                                        mView.requestFailed(e.getMessage());
//                                    }
//                                }));
    }

    @Override
    public void saveProducts(List<Product> products) {
        mSubscriptions.add(
                mProductRepository
                        .saveProducts(products)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(isSuccess -> {
                                    if (isSuccess) {
                                        mView.onSaveProductsSuccess();
                                    }
                                }, Throwable::printStackTrace
                                , () -> {

                                }));
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

    @Override
    public MainViewModel getViewModel() {
        return mViewModel;
    }
}
