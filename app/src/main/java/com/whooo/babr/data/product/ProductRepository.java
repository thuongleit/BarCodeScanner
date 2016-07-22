package com.whooo.babr.data.product;

import android.support.annotation.NonNull;

import com.whooo.babr.data.DataSource;
import com.whooo.babr.vo.Product;

import java.util.List;

import rx.Observable;

public interface ProductRepository extends DataSource {

    Observable<List<Product>> searchProducts(@NonNull String code);

    Observable<List<Product>> getProducts();

    Observable<List<Product>> saveProducts(List<Product> products);

    Observable<Boolean> removeProduct(Product product);

    Observable<String> checkout(String cart, List<Product> products, boolean askToPending);

}
