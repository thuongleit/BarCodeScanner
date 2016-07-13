package com.whooo.babr.data.product;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.whooo.babr.di.ApplicationScope;
import com.whooo.babr.util.FirebaseUtils;
import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subscriptions.Subscriptions;

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
        // TODO: 7/14/16 need to load products per pages (10-20 items per page)
        return Observable
                .create(subscriber -> {
                    DatabaseReference productRef = FirebaseUtils.getProductsRef().child(FirebaseUtils.getCurrentUserId());
                    final List<Product> products = new ArrayList<>();

                    ValueEventListener eventListener = productRef.addValueEventListener(new ValueEventListener() {
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
                    subscriber.add(Subscriptions.create(() -> productRef.removeEventListener(eventListener)));
                });

    }

    @Override
    public Observable<Boolean> saveProducts(List<Product> products) {
        return Observable.create(subscriber -> {
            DatabaseReference productRef = FirebaseUtils.getProductsRef();
            // FIXME: 7/13/16 Need to investigate more in saving products
            for (Product p : products) {
                String productKey = productRef.push().getKey();
                Product product = new Product(p.name, p.country, p.manufacture, p.source, p.upc, p.ean, p.imageUrl, p.model, p.quantity, p.id, FirebaseUtils.getCurrentUserId());
                p.id = productKey;
                productRef
                        .child(FirebaseUtils.getCurrentUserId())
                        .child(p.id).setValue(product, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        FirebaseUtils.attachErrorHandler(subscriber, databaseError);
                    }
                });
            }
            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Boolean> removeProduct(Product product) {
        return Observable.create(subscriber -> {
            DatabaseReference userRef = null;
            DatabaseReference productRef = FirebaseUtils.getProductsRef();
            userRef.child(product.id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userRef.child(product.id).removeValue();
                        productRef.child(product.id).removeValue();
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
    public Observable<Boolean> saveProductsHistory(Cart history, List<Product> products) {
        return Observable.create(subscriber -> {
            DatabaseReference productsRef = FirebaseUtils.getProductsRef();
            DatabaseReference hisRef = FirebaseUtils.getCartRef();
            String newKey = hisRef.push().getKey();
            hisRef.child(newKey).setValue(history, (databaseError, databaseReference) -> {
                for (Product p : products) {
                    Product product = new Product(p.name, p.country, p.manufacture, p.source, p.upc, p.ean, p.imageUrl, p.model, p.quantity, p.id, FirebaseUtils.getCurrentUserId());
                    hisRef.child(newKey).child("products").child(p.id).setValue(product, (databaseError1, databaseReference1) -> {
                        productsRef.child(p.id).removeValue();
                    });
                }
            });
            subscriber.onNext(true);
            subscriber.onCompleted();


        });
    }
}
