package com.whooo.babr.view.session.splash;

import com.google.firebase.auth.FirebaseAuth;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashModule {

    private final SplashContract.View mView;

    public SplashModule(SplashContract.View view) {
        mView = view;
    }

    @PerActivity
    @Provides
    SplashContract.Presenter providePresenter(FirebaseAuth auth) {
        return new SplashPresenter(mView, auth);
    }
}
