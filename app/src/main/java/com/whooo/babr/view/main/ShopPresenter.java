package com.whooo.babr.view.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseNetworkException;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.view.binding.ItemTouchHandler;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

class ShopPresenter implements ShopContract.Presenter {

    private ShopContract.View mView;
    @NonNull
    private final ProductRepository mProductRepository;
    private final ShopViewModel mViewModel;
    private CompositeSubscription mSubscriptions;

    ShopPresenter(@Nullable ShopContract.View view, ShopViewModel viewModel, @NonNull ProductRepository productRepository) {
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
    public void checkout(String cartName) {
        checkout(cartName, false);
    }

    @Override
    public void addToPending(String cartName) {
        checkout(cartName, true);
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
    public ShopViewModel getViewModel() {
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
                    Timber.e(e, "WTF error here?");
                    mView.showInAppError();
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

    private void checkout(String cartName, boolean askToPending) {
        mView.showStandaloneProgress(true);
        mSubscriptions.add(
                mProductRepository.checkout(cartName, mViewModel.data, askToPending)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(key -> {
                                    mView.onCheckoutSuccess(key);
                                    mView.onEmptyResponse();
                                    mViewModel.setData(new ArrayList<>());
                                }
                                , e -> {
                                    e.printStackTrace();
                                    mView.showStandaloneProgress(false);
                                }, () -> mView.showStandaloneProgress(false)));
    }
}
