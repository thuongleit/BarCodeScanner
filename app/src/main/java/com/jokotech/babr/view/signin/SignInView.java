package com.jokotech.babr.view.signin;

import com.parse.ParseUser;
import com.jokotech.babr.view.base.ErrorableView;

/**
 * Created by thuongle on 12/30/15.
 */
interface SignInView extends ErrorableView {

    void onActionSuccess(ParseUser user);

    void onActionFailed(String message);

    void showProgress(boolean show);
}
