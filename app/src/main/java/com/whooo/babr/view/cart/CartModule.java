package com.whooo.babr.view.cart;

import com.whooo.babr.data.cart.CartRepository;
import com.whooo.babr.di.PerFragment;

import dagger.Module;
import dagger.Provides;

@Module
class CartModule {

    private final CartContract.View mView;
    private final boolean mIsPending;

    public CartModule(CartContract.View view, boolean isPending) {
        mView = view;
        mIsPending = isPending;
    }

    @Provides
    @PerFragment
    public CartViewModel provideHistoryViewModel(){
        return new CartViewModel(mIsPending);
    }

    @Provides
    @PerFragment
    public CartContract.Presenter providePresenter(CartViewModel viewmodel, CartRepository repository) {
        return new CartPresenter(mView, viewmodel, repository);
    }
}
