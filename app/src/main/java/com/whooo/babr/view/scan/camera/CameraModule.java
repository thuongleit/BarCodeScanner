package com.whooo.babr.view.scan.camera;

import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.di.PerFragment;

import dagger.Module;
import dagger.Provides;

@Module
class CameraModule {

    private final CameraContract.View mView;

    CameraModule(CameraContract.View view) {
        mView = view;
    }

    @Provides
    @PerFragment
    public CameraContract.Presenter providePresenter(ProductRepository productRepository) {
        return new CameraPresenter(mView, productRepository);
    }
}
