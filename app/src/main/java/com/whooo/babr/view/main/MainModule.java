package com.whooo.babr.view.main;

import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private final MainContract.View mView;

    public MainModule(MainContract.View view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public MainContract.Presenter providePresenter(){
        return new MainPresenter(null, null);
    }
}
