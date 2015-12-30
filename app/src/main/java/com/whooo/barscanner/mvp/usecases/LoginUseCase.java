package com.whooo.barscanner.mvp.usecases;

import com.parse.LogInCallback;
import com.parse.ParseUser;

/**
 * Created by thuongle on 12/30/15.
 */
public class LoginUseCase extends StandAloneUseCase<Void> {
    private final String mUsername;
    private final String mPassword;

    public LoginUseCase(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    @Override
    public Void execute(Object... parameters) {
        if (parameters.length > 1 || !(parameters[0] instanceof LogInCallback)) {
            throw new IllegalArgumentException("Parameter has to be LogInCallback instance");
        }
        ParseUser.logInInBackground(mUsername, mPassword, (LogInCallback) parameters[0]);
        return null;
    }
}
