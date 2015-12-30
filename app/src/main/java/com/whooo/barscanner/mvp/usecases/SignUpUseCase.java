package com.whooo.barscanner.mvp.usecases;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by thuongle on 12/30/15.
 */
public class SignUpUseCase extends StandAloneUseCase<Void> {

    private final String username;
    private final String email;
    private final String password;

    public SignUpUseCase(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Void execute(Object... parameters) {
        if (parameters.length > 1 || !(parameters[0] instanceof SignUpCallback)) {
            throw new IllegalArgumentException("Parameter has to be LogInCallback instance");
        }

        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setEmail(email);
        parseUser.setPassword(password);

        parseUser.signUpInBackground((SignUpCallback) parameters[0]);

        return null;
    }
}
