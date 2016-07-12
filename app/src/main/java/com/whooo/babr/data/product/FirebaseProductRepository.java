package com.whooo.babr.data.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.whooo.babr.di.ApplicationScope;
import com.whooo.babr.util.FirebaseUtils;
import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

public class FirebaseProductRepository implements ProductRepository {

    @NonNull
    private final DatabaseReference mDbRef;
    @NonNull
    private final SearchService mSearchService;

    @Inject
    @ApplicationScope
    Context mContext;

    public FirebaseProductRepository(@NonNull DatabaseReference dbRef,
                                     @NonNull SearchService searchService) {
        mDbRef = dbRef;
        mSearchService = searchService;
    }

    @Override
    public Observable<List<Product>> searchProducts(@NonNull String code) {

        return Observable.merge(
                mSearchService.searchProducts(ProductSource.SEARCH_UPC, code),
                mSearchService.searchProducts(ProductSource.AMAZON, code),
//                mSearchService.searchProducts(ProductSource.IN_APP, code),
                mSearchService.searchProducts(ProductSource.UPC_DATABASE, code),
                mSearchService.searchProducts(ProductSource.UPC_ITEM_DB, code),
                mSearchService.searchProducts(ProductSource.WALMART, code)
        );
    }

    @Override
    public Observable<List<Product>> getProducts() {
        return Observable.create(subscriber -> {
            DatabaseReference productRef = FirebaseUtils.getProductsRef();
            final List<Product> products = new ArrayList<>();

            ValueEventListener productsEvent = new ValueEventListener() {
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
                }
            };
            productRef.addValueEventListener(productsEvent);
        });
    }

    @Override
    public Observable<Boolean> saveProducts(List<Product> products) {
        return Observable.create(subscriber -> {
            DatabaseReference userRef = FirebaseUtils.getUserProductsRef();
            DatabaseReference productRef = FirebaseUtils.getProductsRef();

            for (Product p : products) {
                Product product = new Product(p.name, p.country, p.manufacture, p.source, p.upcA, p.ean, p.imageUrl, p.model, p.quantity,p.objectId, FirebaseUtils.getCurrentUserId());
                userRef.child(p.objectId).setValue(true, (databaseError, databaseReference) -> {
                    productRef.child(p.objectId).setValue(product, (databaseError1, databaseReference1) -> {

                    });

                });
            }
            subscriber.onNext(true);
            subscriber.onCompleted();

        });
    }

    @Override
    public Observable<Boolean> removeProduct(Product product) {
        return Observable.create(subscriber -> {
            DatabaseReference userRef = FirebaseUtils.getUserProductsRef();
            DatabaseReference productRef = FirebaseUtils.getProductsRef();
            userRef.child(product.objectId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userRef.child(product.objectId).removeValue();
                        productRef.child(product.objectId).removeValue();
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });

    }

    @Override
    public Observable<Boolean> saveProductsHistory(CheckoutHistory history, List<Product> products) {
        return Observable.create(subscriber -> {
            DatabaseReference productsRef = FirebaseUtils.getProductsRef();
            DatabaseReference hisRef = FirebaseUtils.getHistoryRef();
            String newKey = hisRef.push().getKey();
            hisRef.child(newKey).setValue(history, (databaseError, databaseReference) -> {
                for (Product p : products) {
                    Product product = new Product(p.name, p.country, p.manufacture, p.source, p.upcA, p.ean, p.imageUrl, p.model, p.quantity,p.objectId, FirebaseUtils.getCurrentUserId());
                    hisRef.child(newKey).child("products").child(p.objectId).setValue(product, (databaseError1, databaseReference1) -> {
                        productsRef.child(p.objectId).removeValue();
                    });
                }
            });
            subscriber.onNext(true);
            subscriber.onCompleted();


        });
    }
}
