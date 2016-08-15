package com.whooo.babr.data.remote;

import android.support.annotation.NonNull;

import com.whooo.babr.data.cart.CartRepository;
import com.whooo.babr.data.product.ProductSource;
import com.whooo.babr.vo.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class InAppService implements ParseService{

    private final CartRepository mCartRepository;

    @Inject
    public InAppService(CartRepository cartRepository) {
        mCartRepository = cartRepository;
    }

    @Override
    public Observable<List<Product>> searchProductsByCode(@NonNull String cartId) {
        return mCartRepository
                .getProductsInCart(cartId)
                .flatMap(Observable::from)
                .doOnNext(product -> product.source = ProductSource.IN_APP.getDisplay())
                .toList();
    }
}
