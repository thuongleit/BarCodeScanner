package com.whooo.barscanner.mvp.presenters;

import com.whooo.barscanner.mvp.views.SignUpView;

/**
 * Created by thuongle on 12/30/15.
 */
public interface SignUpPresenter extends Presenter<SignUpView> {

    void signUp(String username, String email, String password);
}
