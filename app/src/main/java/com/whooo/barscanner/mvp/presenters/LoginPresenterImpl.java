package com.whooo.barscanner.mvp.presenters;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.whooo.barscanner.mvp.usecases.LoginUseCase;
import com.whooo.barscanner.mvp.usecases.LoginWithSocialUseCase;
import com.whooo.barscanner.mvp.views.LoginView;

import org.brickred.socialauth.Profile;

/**
 * Created by thuongle on 12/30/15.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mView;
    private LoginUseCase mLoginUseCase;
    private LoginWithSocialUseCase mLoginWithSocialUseCase;

    public LoginPresenterImpl() {
    }

    @Override
    public void attach(LoginView view) {
        mView = view;
    }

    @Override
    public void deAttach() {

    }

    @Override
    public void login(String username, String password) {
        mLoginUseCase = new LoginUseCase(username, password);
        mView.showProgress();

        mLoginUseCase.execute(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                mView.hideProgress();

                if (e == null) {
                    mView.onLoginSuccess(user);
                } else {
                    mView.onError(e);
                }
            }
        });
    }

    @Override
    public void loginWithSocial(Profile profile, String token) {
        mLoginWithSocialUseCase = new LoginWithSocialUseCase(profile, token);
        mView.showProgress();

        LogInCallback logInCallback = new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                mView.hideProgress();

                if (e == null) {
                    mView.onLoginSuccess(user);
                } else {
                    mView.onError(e);
                }
            }
        };

        SignUpCallback signUpCallback = new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mView.onLoginSuccess(null);
                } else {
                    mView.onError(e);
                }
            }
        };

        mLoginWithSocialUseCase.execute(logInCallback, signUpCallback);
    }
}
