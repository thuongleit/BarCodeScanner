package com.whooo.barscanner.view.signup;

import com.whooo.barscanner.view.base.ErrorableView;

/**
 * Created by thuongle on 12/30/15.
 */
public interface SignUpView extends ErrorableView {

    void onSignUpSuccess();

    void onSignUpFailed(String message);

    void showProgress(boolean show);

}
