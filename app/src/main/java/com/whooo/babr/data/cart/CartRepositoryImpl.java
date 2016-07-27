package com.whooo.babr.data.cart;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.whooo.babr.util.AppUtils;
import com.whooo.babr.util.FirebaseUtils;
import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.subscriptions.Subscriptions;

public class CartRepositoryImpl implements CartRepository {

    public CartRepositoryImpl() {
    }

    @Override
    public Observable<List<Cart>> getCarts() {
        return Observable
                .create(subscriber -> {
                    DatabaseReference cartRef = FirebaseUtils.getCartRef();
                    final List<Cart> historyCarts = new ArrayList<>();

                    ValueEventListener eventListener = cartRef
                            .orderByChild("userId")
                            .equalTo(FirebaseUtils.getCurrentUserId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Cart cart = snapshot.getValue(Cart.class);
                                        historyCarts.add(cart);
                                    }
                                    subscriber.onNext(historyCarts);
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    FirebaseUtils.attachErrorHandler(subscriber, databaseError);
                                }
                            });
                    //when the subscription is cancelled, remove the listener
                    subscriber.add(Subscriptions.create(() -> cartRef.removeEventListener(eventListener)));
                });
    }

    @Override
    public Observable<List<Product>> getProductsInCart(String cartId) {
        return Observable.create(subscriber -> {
            DatabaseReference hisProductRef = FirebaseUtils.getHistoryProductsRef(cartId);
            final List<Product> products = new ArrayList<>();

            ValueEventListener eventListener = hisProductRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        products.add(product);
                    }
                    subscriber.onNext(products);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    FirebaseUtils.attachErrorHandler(subscriber, databaseError);
                }
            });
            //when the subscription is cancelled, remove the listener
            subscriber.add(Subscriptions.create(() -> hisProductRef.removeEventListener(eventListener)));
        });
    }

    @Override
    public Observable<Boolean> deleteCart(@NonNull String cartId) {
        return Observable
                .create(subscriber -> {
                    DatabaseReference cartRef = FirebaseUtils.getCartRef();

                    cartRef.child(cartId).removeValue((databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            subscriber.onNext(Boolean.TRUE);
                            subscriber.onCompleted();
                        } else {
                            FirebaseUtils.attachErrorHandler(subscriber, databaseError);
                        }
                    });
                });
    }

    @Override
    public Observable<Boolean> checkoutCartInPending(@NotNull Cart cart) {
        return Observable
                .create(subscriber -> {
                    DatabaseReference cartRef = FirebaseUtils.getCartRef();

                    Map<String, Object> updateValues = new HashMap<>();
                    updateValues.put("pending", false);
                    updateValues.put("timestamp", AppUtils.generateTimeStamp());

                    cartRef.child(cart.objectId).updateChildren(updateValues, (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            subscriber.onNext(Boolean.TRUE);
                            subscriber.onCompleted();
                        } else {
                            FirebaseUtils.attachErrorHandler(subscriber, databaseError);
                        }
                    });
                });
    }
}