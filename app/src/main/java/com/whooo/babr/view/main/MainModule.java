package com.whooo.babr.view.main;

import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private final MainContract.View mView;

    public MainModule(MainContract.View view) {
        mView = view;
    }

    @Provides
    @PerActivity
    public MainContract.Presenter providePresenter(ProductRepository productRespository) {
        return new MainPresenter(mView, productRespository);
    }
}
