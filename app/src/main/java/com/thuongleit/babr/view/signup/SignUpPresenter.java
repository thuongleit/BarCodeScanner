package com.thuongleit.babr.view.signup;

import com.parse.ParseUser;
import com.thuongleit.babr.data.DataManager;
import com.thuongleit.babr.view.base.BasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by thuongle on 12/30/15.
 */
public class SignUpPresenter extends BasePresenter<SignUpView> {
    private final DataManager mDataManager;

    @Inject
    public SignUpPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void signUp(final String username, String email, String password) {
        checkViewAttached();
        mView.showProgress(true);

        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setEmail(email);
        parseUser.setPassword(password);

        parseUser.signUpInBackground(e -> {
            mView.showProgress(false);
            if (e == null) {
                Timber.i("User %s sign up successfully", username);
                //the user is logged in
                mView.onSignUpSuccess();
            } else {
                Timber.i(e, "User %s sign up failed", username);
                mView.onSignUpFailed(e.getMessage());
            }
        });
    }
}
