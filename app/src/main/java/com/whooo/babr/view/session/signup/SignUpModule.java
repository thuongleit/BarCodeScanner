package com.whooo.babr.view.session.signup;

import com.google.firebase.auth.FirebaseAuth;
import com.whooo.babr.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class SignUpModule {

    private final SignUpContract.View mView;

    public SignUpModule(SignUpContract.View view) {
        this.mView = view;
    }

    @PerActivity
    @Provides
    public SignUpContract.Presenter providePresenter(FirebaseAuth auth) {
        return new SignUpPresenter(mView, auth);
    }
}
