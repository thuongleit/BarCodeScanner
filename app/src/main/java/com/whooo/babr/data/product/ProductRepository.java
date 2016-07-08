package com.whooo.babr.data.product;

import com.whooo.babr.data.DataSource;
import com.whooo.babr.vo.Product;

import java.util.List;

import rx.Observable;

public interface ProductRepository extends DataSource {

    String PRODUCT_INSTANCE = "products";

    Observable<List<Product>> getProducts();

    Observable<Boolean> saveProducts(Product... products);
}
