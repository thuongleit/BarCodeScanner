package com.whooo.babr.view.scan.camera;

import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class CameraModule {

    private final CameraContract.View mView;

    public CameraModule(CameraContract.View view) {
        mView = view;
    }

    @Provides
    @PerActivity
    public CameraContract.Presenter providePresenter(ProductRepository productRepository) {
        return new CameraPresenter(mView, productRepository);
    }
}
