package com.whooo.babr.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whooo.babr.view.session.base.User;

import rx.Subscriber;

public class FirebaseUtils {

    public static final String PATH = "/";

    @NonNull
    public static DatabaseReference getBaseDatabaseRef(DbInstance instance) {
        if (instance == DbInstance.KEY_NONE) {
            return FirebaseDatabase.getInstance().getReference();
        } else {
            return FirebaseDatabase.getInstance().getReference(instance.getKey());
        }
    }

    @Nullable
    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    @Nullable
    public static User getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }
        return new User(user.getDisplayName(), getCurrentUserId());
    }

    @Nullable
    public static DatabaseReference getCurrentUserRef() {
        String uid = getCurrentUserId();
        if (uid != null) {
            return getBaseDatabaseRef(DbInstance.KEY_USERS).child(getCurrentUserId());
        }
        return null;
    }

    @NonNull
    public static DatabaseReference getProductsRef() {
        return getBaseDatabaseRef(DbInstance.KEY_PRODUCTS);
    }

    @NonNull
    public static String getProductsPath() {
        return PATH + DbInstance.KEY_PRODUCTS.getKey() + PATH;
    }

    @NonNull
    public static DatabaseReference getUsersRef() {
        return getBaseDatabaseRef(DbInstance.KEY_USERS);
    }

    @NonNull
    public static String getUsersPath() {
        return PATH + DbInstance.KEY_USERS.getKey() + PATH;
    }

    @NonNull
    public static DatabaseReference getCartRef() {
        return getBaseDatabaseRef(DbInstance.KEY_CARTS);
    }

    @NonNull
    public static String getCartsPath() {
        return PATH + DbInstance.KEY_CARTS + PATH;
    }

    /**
     * This method add to subsriber the proper error according to the
     *
     * @param subscriber {@link rx.Subscriber}
     * @param dbError    {@link com.google.firebase.database.DatabaseError}
     * @param <T>        generic subscriber
     */
    public static <T> void attachErrorHandler(Subscriber<T> subscriber, DatabaseError dbError) {
        switch (dbError.getCode()) {
            case DatabaseError.DISCONNECTED:
            case DatabaseError.EXPIRED_TOKEN:
            case DatabaseError.NETWORK_ERROR:
                subscriber.onError(new FirebaseNetworkException(dbError.getMessage()));
                break;
            default:
                subscriber.onError(dbError.toException());
        }
    }

    public enum DbInstance {
        KEY_USERS("users"),
        KEY_PRODUCTS("products"),
        KEY_CARTS("carts"),
        KEY_NONE("all");

        private String key;

        DbInstance(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
