package com.whooo.babr.view.main;

import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
class ShopModule {

    private final ShopContract.View mView;

    ShopModule(ShopContract.View view) {
        mView = view;
    }

    @Provides
    @PerActivity
    ShopViewModel provideViewHandler() {
        return new ShopViewModel();
    }

    @Provides
    @PerActivity
    public ShopContract.Presenter providePresenter(ProductRepository productRespository, ShopViewModel handler) {
        return new ShopPresenter(mView, handler, productRespository);
    }
}
