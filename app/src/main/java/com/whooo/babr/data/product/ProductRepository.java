package com.whooo.babr.data.product;

import android.support.annotation.NonNull;

import com.whooo.babr.data.DataSource;
import com.whooo.babr.vo.Product;

import java.util.List;

import rx.Observable;

public interface ProductRepository extends DataSource {

    String CODE_NAME = "nodes";

    Observable<List<Product>> searchProducts(@NonNull String code);

    Observable<List<Product>> getProducts();

    Observable<Boolean> saveProducts(Product... products);
}
