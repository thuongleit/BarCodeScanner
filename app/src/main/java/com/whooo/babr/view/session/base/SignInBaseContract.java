package com.whooo.babr.view.session.base;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.base.BaseView;

public interface SignInBaseContract {
    interface View extends BaseView {

        void onSignInSuccess();

        void onSignInFailed(String message);

        void showProgress(boolean show);

        void setButtonSignInAnonymusEnable(boolean enabled);

        void setButtonFbEnable(boolean enabled);

        void setButtonGoogleEnable(boolean enabled);
    }

    interface Presenter extends BasePresenter {
        void signInWithFacebook(@NonNull AccessToken fbToken);

        void signInWithGoogle(@NonNull GoogleSignInResult googleSignIn);

        void signInAnonymous();
    }
}
