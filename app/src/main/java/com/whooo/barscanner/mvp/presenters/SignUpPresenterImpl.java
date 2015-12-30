package com.whooo.barscanner.mvp.presenters;

import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.whooo.barscanner.mvp.usecases.SignUpUseCase;
import com.whooo.barscanner.mvp.views.SignUpView;

/**
 * Created by thuongle on 12/30/15.
 */
public class SignUpPresenterImpl implements SignUpPresenter {
    private SignUpView mView;

    public SignUpPresenterImpl() {
    }

    @Override
    public void signUp(final String username, String email, String password) {
        SignUpUseCase signUpUseCase = new SignUpUseCase(username, email, password);
        mView.showProgress();

        SignUpCallback signUpCallback = new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                mView.hideProgress();
                if (e == null) {
                    //the user is logged in
                    mView.onSignUpSuccess();
                } else {
                    mView.onError(e);
                }
            }
        };
        signUpUseCase.execute(signUpCallback);
    }

    @Override
    public void attach(SignUpView view) {
        mView = view;
    }

    @Override
    public void deAttach() {

    }
}
