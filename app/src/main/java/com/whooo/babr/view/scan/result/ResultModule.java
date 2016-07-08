package com.whooo.babr.view.scan.result;

import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ResultModule {

    private final ResultContract.View mView;

    public ResultModule(ResultContract.View view) {
        mView = view;
    }

    @Provides
    @PerActivity
    public ResultContract.Presenter providePresenter(ProductRepository productRepository){
        return new ResultPresenter(mView, productRepository);
    }
}
