package com.whooo.babr.util;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whooo.babr.view.session.base.User;

import rx.Subscriber;

public class FirebaseUtils {

    public static DatabaseReference getBaseDatabaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static User getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return new User(user.getDisplayName(), getCurrentUserId());
    }

    public static DatabaseReference getCurrentUserRef() {
        String uid = getCurrentUserId();
        if (uid != null) {
            return getBaseDatabaseRef().child("user").child(getCurrentUserId());
        }
        return null;
    }

    public static DatabaseReference getProductsRef() {
        return getBaseDatabaseRef().child("products").child(getCurrentUserId());
    }

    public static String getProductsPath() {
        return "products/";
    }

    public static DatabaseReference getUserProductsRef() {
        return getBaseDatabaseRef().child("users").child(getCurrentUserId()).child("products");
    }

    public static DatabaseReference getUsersRef() {
        return getBaseDatabaseRef().child("users");
    }

    public static String getUsersPath() {
        return "users/";
    }

    public static DatabaseReference getHistoryRef() {
        return getBaseDatabaseRef().child("history").child(getCurrentUserId());
    }

//    public static DatabaseReference getHistoryProductsRef() {
//        return getBaseDatabaseRef().child("history").child(getCurrentUserId()).;
//    }

    public static String getHistoryPath() {
        return "history/";
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
}
