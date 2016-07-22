package com.whooo.babr.data.cart;

import com.whooo.babr.data.DataSource;
import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import java.util.List;

import rx.Observable;

public interface CartRepository extends DataSource {

    Observable<List<Cart>> getCarts();

    Observable<List<Product>> getProductsInCart(String cartId);
}
