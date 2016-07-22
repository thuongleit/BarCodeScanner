package com.whooo.babr.view.cart;

import android.support.annotation.NonNull;

import com.whooo.babr.data.cart.CartRepository;
import com.whooo.babr.view.binding.ClickHandler;
import com.whooo.babr.vo.Cart;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class CartPresenter implements CartContract.Presenter {

    private CartContract.View mView;
    private CartViewModel mViewModel;
    @NonNull
    private final CartRepository mCartRepository;
    private CompositeSubscription mSubscriptions;

    public CartPresenter(CartContract.View view, CartViewModel viewmodel, @NonNull CartRepository cartRepository) {
        mView = view;
        mViewModel = viewmodel;
        mCartRepository = cartRepository;

        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        getCarts();
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
        if (mSubscriptions != null) {
            mSubscriptions = null;
        }
    }

    private void getCarts() {
        Timber.d("Getting Carts...");
        mViewModel.loading();
        mSubscriptions.add(mCartRepository
                .getCarts()
                .flatMap(Observable::from)
                .filter(cart -> mViewModel.needPending.get() == cart.pending)
                .toSortedList((cart1, cart2) -> cart1.timestamp.compareTo(cart2.timestamp))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(carts -> {
                            Timber.d("Get Carts success with cart size %d", carts.size());
                            mViewModel.setData(carts);
                        }, e -> {
                            Timber.e(e, "Error in getting carts...");

                            if (e instanceof IOException) {
                                mView.showNetworkError();
                            } else {
                                mView.showInAppError();
                            }
                        }
                ));
    }

    @Override
    public CartViewModel getViewModel() {
        return mViewModel;
    }

    public ClickHandler<Cart> clickHandler = cart -> mView.startDetailActivity(cart);
}
