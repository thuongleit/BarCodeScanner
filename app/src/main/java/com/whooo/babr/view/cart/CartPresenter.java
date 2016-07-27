package com.whooo.babr.view.cart;

import android.support.annotation.NonNull;

import com.whooo.babr.BR;
import com.whooo.babr.data.cart.CartRepository;
import com.whooo.babr.view.binding.ChildItemsClickBinder;
import com.whooo.babr.view.binding.ChildItemsClickHandler;
import com.whooo.babr.view.binding.ClickHandler;
import com.whooo.babr.vo.Cart;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public ChildItemsClickBinder<Cart> childItemsClickBinder = new ChildItemsClickBinder<Cart>() {
        @Override
        public List<ChildItemsClickHandler<Cart>> childItemsClickHandlers() {
            ChildItemsClickHandler<Cart> item1 = cart -> {
                //delete function
                mView.showConfirmDialogInDelete(cart);
            };
            ChildItemsClickHandler<Cart> item2 = cart -> {
                if (mViewModel.needPending.get()) { //check out it it is in pending fragment
                    mView.showConfirmDialogInCheckout(cart);
                } else { //generate qrcode
                    mView.generateQRCode(cart);
                }
            };
            return Arrays.asList(item1, item2);
        }

        @Override
        public int getBindingVariable(Cart model) {
            return BR.childHandlers;
        }
    };

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

    @Override
    public void deleteCart(@NonNull Cart cart) {
        Timber.d("Deleting Cart %s...", cart.objectId);
        mView.showProgress(true);
        mSubscriptions.add(mCartRepository
                .deleteCart(cart.objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Timber.d("Delete cart %s success", cart.objectId);
                            mViewModel.removeItem(cart);
                            mView.showProgress(false);
                        }, e -> {
                            mView.showProgress(false);
                            Timber.e(e, "Error in deleting cart %s", cart.objectId);
                            if (e instanceof IOException) {
                                mView.showNetworkError();
                            } else {
                                mView.showInAppError();
                            }
                        }
                ));
    }

    @Override
    public void checkout(@NonNull Cart cart) {
        Timber.d("Checking out Cart %s...", cart.objectId);
        mView.showProgress(true);
        mSubscriptions.add(mCartRepository
                .checkoutCartInPending(cart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Timber.d("Checking out cart %s success", cart.objectId);
                            mViewModel.removeItem(cart);
                            mView.showProgress(false);
                        }, e -> {
                            mView.showProgress(false);
                            Timber.e(e, "Error in Checking out cart %s", cart.objectId);
                            if (e instanceof IOException) {
                                mView.showNetworkError();
                            } else {
                                mView.showInAppError();
                            }
                        }
                ));
    }
}
