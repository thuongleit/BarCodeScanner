package com.jokotech.babr.view.signin;

import com.jokotech.babr.di.PerActivity;
import com.jokotech.babr.view.base.BasePresenter;
import com.parse.ParseUser;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by thuongle on 12/30/15.
 */
@PerActivity
class SignInPresenter extends BasePresenter<SignInView> {

    @Inject
    public SignInPresenter() {
    }

    public void login(final String email, String password) {
        checkViewAttached();
        mView.showProgress(true);

        ParseUser.logInInBackground(email, password, (user, e) -> {
            mView.showProgress(false);
            if (e == null) {
                Timber.i("Sign in with %s successfully!", email);
                mView.onActionSuccess(user);
            } else {
                Timber.e(e, "Sign in with %s failed!", email);
                mView.onActionFailed(e.getMessage());
            }
        });
    }

    public void signUp(String email, String password) {
        checkViewAttached();
        mView.showProgress(true);

        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(email);
        parseUser.setEmail(email);
        parseUser.setPassword(password);

        parseUser.signUpInBackground(e -> {
            mView.showProgress(false);
            if (e == null) {
                Timber.i("User %s sign up successfully", email);
                //the user is logged in
                mView.onActionSuccess(parseUser);
            } else {
                Timber.e(e, "User %s sign up failed", email);
                mView.onActionFailed(e.getMessage());
            }
        });
    }
}
