package com.whooo.babr.data.product;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.whooo.babr.util.AppUtils;
import com.whooo.babr.util.FirebaseUtils;
import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public class ProductRepositoryImpl implements ProductRepository {

    @NonNull
    private final SearchService mSearchService;

    public ProductRepositoryImpl(@NonNull SearchService searchService) {
        mSearchService = searchService;
    }

    @Override
    public Observable<List<Product>> searchProducts(@NonNull String code) {

        return Observable
                .mergeDelayError(
                        mSearchService.searchProducts(ProductSource.IN_APP, code),
                        mSearchService.searchProducts(ProductSource.SEARCH_UPC, code),
                        mSearchService.searchProducts(ProductSource.AMAZON, code),
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
    public Observable<List<Product>> saveProducts(String cardId, List<Product> products) {
        return Observable.create(subscriber -> {

            if (cardId == null) {
                saveProductsForCurrentUser(subscriber, products);
            } else {
                saveProductForCart(subscriber, cardId, products);
            }
        });
    }

    private void saveProductsForCurrentUser(Subscriber<? super List<Product>> subscriber, List<Product> products) {
        List<Product> result = new ArrayList<>();
        DatabaseReference productRef = FirebaseUtils.getProductsRef();
        for (Product product : products) {
            String productKey = productRef.push().getKey();
            try {
                Product newProduct = (Product) product.clone();
                newProduct.objectId = productKey;
                productRef
                        .child(FirebaseUtils.getCurrentUserId())
                        .child(newProduct.objectId)
                        .setValue(newProduct, (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                FirebaseUtils.attachErrorHandler(subscriber, databaseError);
                            }
                        });
                result.add(newProduct);
            } catch (CloneNotSupportedException e) {
                subscriber.onError(e);
            }
        }
        subscriber.onNext(result);
        subscriber.onCompleted();
    }

    private void saveProductForCart(Subscriber<? super List<Product>> subscriber, String cardId, List<Product> products) {
        List<Product> result = new ArrayList<>();

        DatabaseReference cartRef = FirebaseUtils.getCartRef(cardId);
        DatabaseReference productsRef = FirebaseUtils.getHistoryProductsRef(cardId);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cart cart = dataSnapshot.getValue(Cart.class);
                if (cart != null) {
                    cart.size += products.size();

                    Map<String, Object> values = new HashMap<>();
                    values.put("size", cart.size);
                    dataSnapshot.getRef().updateChildren(values);

                    //update products
                    for (Product product : products) {
                        String productKey = productsRef.push().getKey();
                        try {
                            Product newProduct = (Product) product.clone();
                            newProduct.objectId = productKey;

                            productsRef
                                    .child(newProduct.objectId)
                                    .setValue(newProduct, (databaseError, databaseReference) -> {
                                        if (databaseError != null) {
                                            FirebaseUtils.attachErrorHandler(subscriber, databaseError);
                                        }
                                    });
                            result.add(newProduct);
                        } catch (CloneNotSupportedException e) {
                            subscriber.onError(e);
                        }
                    }
                    subscriber.onNext(result);
                }
                subscriber.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseUtils.attachErrorHandler(subscriber, databaseError);
            }
        });
    }

    @Override
    public Observable<Boolean> removeProduct(Product product) {
        return Observable.create(subscriber -> {
            DatabaseReference productRef = FirebaseUtils.getProductsRef();
            productRef
                    .child(FirebaseUtils.getCurrentUserId())
                    .child(product.objectId)
                    .removeValue((databaseError, databaseReference) -> {
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
    public Observable<String> checkout(String cartName, List<Product> products, boolean askToPending) {

        return Observable.create(subscriber -> {
            DatabaseReference cartRef = FirebaseUtils.getCartRef();
            DatabaseReference productsRef = FirebaseUtils.getProductsRef();

            String userId = FirebaseUtils.getCurrentUserId();
            if (userId == null) {
                // TODO: 7/18/16 handle userId == null
                return;
            }
            Cart cart = new Cart();
            cart.userId = userId;
            String getKey = cartRef.push().getKey();
            cart.objectId = getKey;
            cart.name = cartName;
            cart.timestamp = AppUtils.generateTimeStamp();
            cart.size = products.size();
            cart.pending = askToPending;

            cartRef
                    .child(cart.objectId)
                    .setValue(cart, (databaseError, dbRef) -> {
                        if (databaseError != null) {
                            FirebaseUtils.attachErrorHandler(subscriber, databaseError);
                            return;
                        }

                        //update child products
                        for (Product product : products) {
                            cartRef.child(cart.objectId).child("products").child(product.objectId)
                                    .setValue(product, (databaseError1, databaseReference1) ->
                                            productsRef.child(userId).child(product.objectId).removeValue());
                        }
                        subscriber.onNext(getKey);
                        subscriber.onCompleted();
                    });
        });
    }
}
