package com.whooo.babr.view.session.signin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import timber.log.Timber;

public class SignInPresenter implements SignInContract.Presenter {

    @NonNull
    private SignInContract.View mView;
    @NonNull
    private final FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignInPresenter(SignInContract.View view, FirebaseAuth auth) {
        mView = view;
        mAuth = auth;

        // [START auth_state_listener]
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Timber.d("onAuthStateChanged:signed_in:" + user.getUid());
                mView.onSignInSuccess();
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
    public void performSignIn(@Nullable String email, @Nullable String password) {
        if (mView.validateInput(email, password)) {
            mView.showProgress(true);
            mView.setSignInBtnEnable(false);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            mView.onSignInSuccess();
                        } else {
                            Timber.e(task.getException(), "Failed in creating new user: %s", email);
                            mView.onSignInFailed(task.getException().getMessage());
                        }
                        mView.showProgress(false);
                        mView.setSignInBtnEnable(true);
                    });
        }
    }

    @Override
    public void askForgotPassword(@Nullable String email) {

    }
}
