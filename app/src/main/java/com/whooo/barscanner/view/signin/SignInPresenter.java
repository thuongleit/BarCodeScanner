package com.whooo.barscanner.view.signin;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.whooo.barscanner.data.DataManager;
import com.whooo.barscanner.view.base.BasePresenter;

import org.brickred.socialauth.Profile;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by thuongle on 12/30/15.
 */
public class SignInPresenter extends BasePresenter<SignInView> {

    private final DataManager mDataManager;

    @Inject
    public SignInPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void login(final String username, String password) {
        checkViewAttached();
        mView.showProgress(true);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                mView.showProgress(false);
                if (e == null) {
                    Timber.i("Sign in with %s successfully!", username);
                    mView.onSignInSuccess(user);
                } else {
                    Timber.i(e, "Sign in with %s failed!", username);
                    mView.onSignInFailed(e.getMessage());
                }
            }
        });
    }

    public void loginWithSocial(Profile profile, final String token) {
        checkViewAttached();
        mView.showProgress(true);

        final String username = profile.getEmail().split("@")[0];
        // FIXME: 12/30/15 need to handle FindCallback
        ParseUser.getQuery().whereEqualTo("username", username).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects != null && !objects.isEmpty()) {
                        //user has log in
                        login(username, token);
                    } else {
                        final ParseUser parseUser = new ParseUser();
                        parseUser.setUsername(username);
                        parseUser.setPassword(token);

                        parseUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Timber.i("Sign up with %s successfully!", username);
                                    mView.onSignInSuccess(parseUser);
                                } else {
                                    Timber.i(e, "Sign in with %s failed!", username);
                                    mView.onSignInFailed(e.getMessage());
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
