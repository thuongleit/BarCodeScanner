package com.whooo.babr.data.product;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.whooo.babr.vo.Product;

import java.util.List;

import rx.Observable;

public class FirebaseProductRepository implements ProductRepository {

    @NonNull
    private final DatabaseReference mDbRef;
    @NonNull
    private final SearchService mSearchService;

    public FirebaseProductRepository(@NonNull DatabaseReference dbRef,
                                     @NonNull SearchService searchService) {
        mDbRef = dbRef;
        mSearchService = searchService;
    }

    @Override
    public Observable<List<Product>> searchProducts(@NonNull String code) {

//        return Observable.merge(
//                mSearchService.searchProducts(ProductSource.SEARCH_UPC, code),
//                mSearchService.searchProducts(ProductSource.AMAZON, code),
//                mSearchService.searchProducts(ProductSource.IN_APP, code),
//                mSearchService.searchProducts(ProductSource.UPC_DATABASE, code)
//                mSearchService.searchProducts(ProductSource.UPC_ITEM_DB, code),
//                mSearchService.searchProducts(ProductSource.WALMART, code)
//        );

        return mSearchService.searchProducts(ProductSource.AMAZON, code);
    }

    @Override
    public Observable<List<Product>> getProducts() {
        return Observable.create(subscriber -> {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Products products = dataSnapshot.getValue(Products.class);
                    if (products != null) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(products.products);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });
    }

    @Override
    public Observable<Boolean> saveProducts(Product... products) {
        return null;
    }

    private class Products {
        List<Product> products;
    }
}
