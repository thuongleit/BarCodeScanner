package com.whooo.babr.view.session.base;

import com.google.firebase.auth.FirebaseAuth;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
class SignInBaseModule {

    private final SignInBaseContract.View mView;

    public SignInBaseModule(SignInBaseContract.View view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public SignInBaseContract.Presenter providePresenter(FirebaseAuth auth){
        return new SignInBasePresenter(mView, auth);
    }
}
