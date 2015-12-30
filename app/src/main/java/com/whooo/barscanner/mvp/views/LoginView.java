package com.whooo.barscanner.mvp.views;

import com.parse.ParseUser;

/**
 * Created by thuongle on 12/30/15.
 */
public interface LoginView extends ErrorableView {

    void onLoginSuccess(ParseUser user);

}
