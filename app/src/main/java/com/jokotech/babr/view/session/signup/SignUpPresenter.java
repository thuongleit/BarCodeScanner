package com.jokotech.babr.view.session.signup;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jokotech.babr.view.session.base.User;

import timber.log.Timber;

public class SignUpPresenter implements SignUpContract.Presenter {

    @NonNull
    private SignUpContract.View mView;
    @NonNull
    private final FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignUpPresenter(SignUpContract.View view, FirebaseAuth auth) {
        this.mView = view;
        this.mAuth = auth;

        // [START auth_state_listener]
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Timber.d("onAuthStateChanged:signed_in:" + user.getUid());
                mView.onSignUpSuccess();
            } else {
                // User is signed out
                Timber.d("onAuthStateChanged:signed_out");
                //do nothing
            }
        };
    }

    @Override
    public void subscribe() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void unsubscribe() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
        mAuthListener = null;
    }

    @Override
    public void createUser(User user) {
        mView.showProgress(true);
        mView.setSignUpBtnEnable(false);
        mAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mView.onSignUpSuccess();
                    } else {
                        Timber.e(task.getException(), "Failed in creating new user: %s", user.email);
                        mView.onSignUpFailed(task.getException().getMessage());
                    }
                    mView.showProgress(false);
                    mView.setSignUpBtnEnable(true);
                });
    }
}