package com.jokotech.babr.view.session.splash;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

public class SplashPresenter implements SplashContract.Presenter {

    private final SplashContract.View mView;
    private final FirebaseAuth mAuth;
    private final FirebaseAuth.AuthStateListener mAuthStateListener;
    private Subscription mSubscription;

    public SplashPresenter(@NonNull SplashContract.View view, @NonNull FirebaseAuth auth) {
        this.mView = checkNotNull(view);
        this.mAuth = checkNotNull(auth);

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Timber.d("onAuthStateChanged:signed_in: %s" + user.getUid());
                mView.onUserHasSignedIn();
            } else {
                // User is signed out
                Timber.d("onAuthStateChanged:signed_out");
                mView.onUserNotSignedIn();
            }
        };
    }

    @Override
    public void subscribe() {
        mSubscription = Observable
                .timer(1, TimeUnit.SECONDS)
                .subscribe(a -> {
                            mAuth.addAuthStateListener(mAuthStateListener);
                        },
                        e -> {
                            Timber.e(e, "Unable restoring session");
                            mView.inAppError(e);
                        });
    }

    @Override
    public void unsubscribe() {
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
