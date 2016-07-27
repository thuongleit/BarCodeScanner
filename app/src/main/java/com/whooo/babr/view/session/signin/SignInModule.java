package com.whooo.babr.view.session.signin;

import com.google.firebase.auth.FirebaseAuth;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
class SignInModule {

    private final SignInContract.View mView;

    public SignInModule(SignInContract.View view) {
        mView = view;
    }

    @PerActivity
    @Provides
    SignInContract.Presenter providePresenter(FirebaseAuth auth){
        return new SignInPresenter(mView, auth);
    }
}
