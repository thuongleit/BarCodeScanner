package com.whooo.babr.view.cart.detail;

import com.whooo.babr.data.cart.CartRepository;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module
class DetailModule {

    private DetailContract.View mView;
    private String mCartId;

    public DetailModule(DetailContract.View view, String cartId) {
        mView = view;
        mCartId = cartId;
    }

    @Provides
    @PerActivity
    DetailViewModel provideDetailViewModel(){
        return new DetailViewModel();
    }

    @Provides
    @PerActivity
    DetailContract.Presenter providePresenter(DetailViewModel viewModel, CartRepository cartRepository){
        return new DetailPresenter(mCartId, mView, viewModel, cartRepository);
    }

}
