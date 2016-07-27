package com.whooo.babr.data.cart;

import android.support.annotation.NonNull;

import com.whooo.babr.data.DataSource;
import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

import rx.Observable;

public interface CartRepository extends DataSource {

    Observable<List<Cart>> getCarts();

    Observable<List<Product>> getProductsInCart(@NonNull String cartId);

    Observable<Boolean> deleteCart(@NonNull String cartId);

    Observable<Boolean> checkoutCartInPending(@NotNull Cart cart);
}
