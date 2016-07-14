package com.whooo.babr.view.cart;

import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ThongLe on 7/12/2016.
 */
@Module
public class HistoryModule {

    private final HistoryContract.View mView;

    public HistoryModule(HistoryContract.View view) {
        mView = view;
    }

    @Provides
    @PerActivity
    public HistoryContract.Presenter providePresenter(ProductRepository productRespository) {
        return new HistoryPresenter(mView, productRespository);
    }
}
