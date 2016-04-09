package com.jokotech.babr.view.signin;

import com.parse.ParseUser;
import com.jokotech.babr.view.base.ErrorableView;

/**
 * Created by thuongle on 12/30/15.
 */
public interface SignInView extends ErrorableView {

    void onSignInSuccess(ParseUser user);

    void onSignInFailed(String message);

    void showProgress(boolean show);

}
