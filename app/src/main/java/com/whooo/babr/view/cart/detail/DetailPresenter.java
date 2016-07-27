package com.whooo.babr.view.cart.detail;

import com.whooo.babr.data.cart.CartRepository;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class DetailPresenter implements DetailContract.Presenter {
    private String mCartId;
    private DetailContract.View mView;
    private DetailViewModel mViewModel;
    private CartRepository mCartRepository;
    private CompositeSubscription mSubscriptions;

    public DetailPresenter(String cartId, DetailContract.View view, DetailViewModel model, CartRepository cartRepository) {
        mCartId = cartId;
        mView = view;
        mViewModel = model;
        mCartRepository = cartRepository;

        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        getProducts(mCartId);
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
    public DetailViewModel getViewModel() {
        return mViewModel;
    }

    private void getProducts(String cartId) {
        mViewModel.loading();
        mSubscriptions.add(mCartRepository
                .getProductsInCart(cartId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> {
                            Timber.d("Get Products in cart %s success with size %s", cartId, products.size());
                            mViewModel.setData(products);
                        },
                        e -> {
                            Timber.e(e, "Get Products for cart %s failed", cartId);
                            if (e instanceof IOException) {
                                mView.showNetworkError();
                            } else {
                                mView.showInAppError();
                            }
                        }));
    }
}
