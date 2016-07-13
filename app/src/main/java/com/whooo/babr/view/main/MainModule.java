package com.whooo.babr.view.main;

import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
class MainModule {

    private final MainContract.View mView;

    MainModule(MainContract.View view) {
        mView = view;
    }

    @Provides
    @PerActivity
    MainViewModel provideViewHandler() {
        return new MainViewModel();
    }

    @Provides
    @PerActivity
    public MainContract.Presenter providePresenter(ProductRepository productRespository, MainViewModel handler) {
        return new MainPresenter(mView, handler, productRespository);
    }
}
