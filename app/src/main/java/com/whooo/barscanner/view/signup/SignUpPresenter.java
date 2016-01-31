package com.whooo.barscanner.view.signup;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.whooo.barscanner.data.DataManager;
import com.whooo.barscanner.view.base.BasePresenter;

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

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                mView.showProgress(false);
                if (e == null) {
                    Timber.i("User %s sign up successfully", username);
                    //the user is logged in
                    mView.onSignUpSuccess();
                } else {
                    Timber.i(e, "User %s sign up failed", username);
                    mView.onSignUpFailed(e.getMessage());
                }
            }
        });
    }
}
