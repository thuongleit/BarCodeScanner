package com.jokotech.babr.view.session.signup;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.jokotech.babr.di.ActivityScope;
import com.jokotech.babr.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by thuongle on 6/8/16.
 */

@Module
public class SignUpModule {

    private final Context mContext;
    private final SignUpContract.View mView;

    public SignUpModule(Context context, SignUpContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public Context provideContext() {
        return mContext;
    }

    @PerFragment
    @Provides
    public SignUpContract.Presenter providePresenter(FirebaseAuth auth, SignUpContract.View view) {
        return new SignUpPresenter(view, auth);
    }
}
