package com.jokotech.babr.view.session.base;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import timber.log.Timber;

public class SignInBasePresenter implements SignInBaseContract.Presenter {

    private SignInBaseContract.View mView;
    private final FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignInBasePresenter(SignInBaseContract.View view, FirebaseAuth auth) {
        mView = view;
        mAuth = auth;

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
    public void signInWithFacebook(@NonNull AccessToken fbToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(fbToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    Timber.d("signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Timber.w("signInWithCredential", task.getException());
                        mView.onSignInFailed("Authentication failed.");
                    }
                });
    }

    @Override
    public void signInWithGoogle(@NonNull GoogleSignInResult googleSignIn) {
        if (googleSignIn.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = googleSignIn.getSignInAccount();
            Timber.d("firebaseAuthWithGoogle:" + account.getId());
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        Timber.d("signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Timber.w("signInWithCredential", task.getException());
                            mView.onSignInFailed("Authentication failed.");
                        }
                    });
        } else {
            // Google Sign In failed, update UI appropriately
            mView.onSignInFailed(googleSignIn.getStatus().getStatusMessage());
        }
    }

    @Override
    public void signInAnonymous() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    Timber.d("signInAnonymously:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Timber.w(task.getException(), "signInAnonymously");
                        mView.onSignInFailed(task.getException().getMessage());
                    }
                });
    }
}
