package com.whooo.barscanner.mvp.presenters;

import com.whooo.barscanner.mvp.views.LoginView;

import org.brickred.socialauth.Profile;

/**
 * Created by thuongle on 12/30/15.
 */
public interface LoginPresenter extends Presenter<LoginView> {

    void login(String username, String password);

    void loginWithSocial(Profile profile, String token);
}
