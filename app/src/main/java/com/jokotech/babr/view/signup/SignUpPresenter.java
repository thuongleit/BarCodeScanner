package com.jokotech.babr.view.signup;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import timber.log.Timber;

/**
 * Created by thuongle on 12/30/15.
 */
public class SignUpPresenter implements SignUpContract.Presenter {

    @NonNull
    private final FirebaseAuth mAuth;
    @NonNull
    private final SignUpContract.View mView;

    public SignUpPresenter(SignUpContract.View view, FirebaseAuth auth) {
        this.mView = view;
        this.mAuth = auth;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void createUser(User user) {
        mAuth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mView.onCreateUserSuccess();
            } else {
                Timber.e(task.getException(), "Failed in creating new user: %s", user.email);
                mView.onInAppError(task.getException());
            }
        });
    }
}