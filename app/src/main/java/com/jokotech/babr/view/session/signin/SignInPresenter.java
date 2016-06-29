package com.jokotech.babr.view.session.signin;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jokotech.babr.view.session.base.User;

import timber.log.Timber;

/**
 * Created by thuongle on 12/30/15.
 */
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
                //// TODO: 6/18/16 remove or not??
                mView.onSignInFailed("");
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
    public void signIn(User user) {
        mView.showProgress(true);
        mView.setSignInBtnEnable(false);
        mAuth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mView.onSignInSuccess();
                    } else {
                        Timber.e(task.getException(), "Failed in creating new user: %s", user.email);
                        mView.onSignInFailed(task.getException().getMessage());
                    }
                    mView.showProgress(false);
                    mView.setSignInBtnEnable(true);
                });
    }
}
