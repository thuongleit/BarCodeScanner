package com.jokotech.babr.view.session.base;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.jokotech.babr.view.base.BasePresenter;
import com.jokotech.babr.view.base.BaseView;

public interface SignInBaseContract {
    interface View extends BaseView {

        void onSignInSuccess();

        void onSignInFailed(String message);
    }

    interface Presenter extends BasePresenter {
        void signInWithFacebook(@NonNull AccessToken fbToken);

        void signInWithGoogle(@NonNull GoogleSignInResult googleSignIn);

        void signInAnonymous();
    }
}
