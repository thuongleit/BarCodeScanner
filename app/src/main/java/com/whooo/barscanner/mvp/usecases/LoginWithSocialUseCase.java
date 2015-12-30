package com.whooo.barscanner.mvp.usecases;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.brickred.socialauth.Profile;

import java.util.List;

/**
 * Created by thuongle on 12/30/15.
 */
public class LoginWithSocialUseCase extends StandAloneUseCase<Void> {
    private final Profile mProfile;
    private final String mToken;

    public LoginWithSocialUseCase(Profile profile, String token) {
        mProfile = profile;
        mToken = token;
    }

    @Override
    public Void execute(final Object... parameters) {
//        String username = mProfile.getEmail().split("@")[0];
        final String username = mProfile.getFullName().replaceAll(" ", "").trim();
        // FIXME: 12/30/15 need to handle FindCallback
        ParseUser.getQuery().whereEqualTo("username", username).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects != null && !objects.isEmpty()) {
                        //user has log in
                        ParseUser.logInInBackground(username, mToken, (LogInCallback) parameters[0]);
                    } else {
                        ParseUser parseUser = new ParseUser();
                        parseUser.setUsername(username);
//                        parseUser.setEmail(mProfile.getEmail());
                        parseUser.setPassword(mToken);

                        parseUser.signUpInBackground((SignUpCallback) parameters[1]);
                    }
                }
            }
        });
        return null;
    }
}
