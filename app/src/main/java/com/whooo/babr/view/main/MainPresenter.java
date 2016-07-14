package com.whooo.babr.view.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseNetworkException;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.view.binding.ItemTouchHandler;
import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;

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
        mViewModel.setLoading();
        mView.removeEmptyViewIfNeeded();
        mSubscriptions.add(
                mProductRepository
                        .getProducts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(products -> {
                                    if (products.isEmpty()) {
                                        mView.onEmptyResponse();
                                    }
                                    mViewModel.setData(products);
                                },
                                e -> {
                                    mViewModel.setData(new ArrayList<>());
                                    if (e instanceof FirebaseNetworkException) {
                                        mView.showNetworkError();
                                    } else {
                                        mView.requestFailed(e.getMessage());
                                    }
                                }));
    }


    @Override
    public void checkout(Cart cart) {
        mView.showStandaloneProgress(true);
        mSubscriptions.add(
                mProductRepository.checkout(cart, mViewModel.data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(key -> {
                                    mView.onCheckoutSuccess(key);
                                    mViewModel.setData(new ArrayList<>());
                                }
                                , e -> {
                                    e.printStackTrace();
                                    mView.showStandaloneProgress(false);
                                }, () -> mView.showStandaloneProgress(false)));
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

    @Override
    public ItemTouchHandler<Product> itemTouchHandler() {
        return new ItemTouchHandler<Product>() {
            @Override
            public void onItemMove(int position, Product product) {

            }

            @Override
            public void onItemDismiss(int position, Product product) {
                try {
                    final Product clone = (Product) product.clone();

                    mView.addPendingRemove(position, clone);
                } catch (CloneNotSupportedException e) {
                    // TODO: 7/14/16 handle unexpected exception
                    return;
                }
                mViewModel.removeItem(product);
            }
        };
    }

    @Override
    public void undoRemovedProduct(int position, Product product) {
        mViewModel.addItem(position, product);
    }
}
