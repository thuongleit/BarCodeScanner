package com.whooo.babr.view.scan.camera;

import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class CameraModule {

    private final CameraContract.View mView;

    public CameraModule(CameraContract.View view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public CameraContract.Presenter providePresenter(){
        return new CameraPresenter(null);
    }
}
