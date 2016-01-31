package com.whooo.barscanner.data.local;

import android.app.Application;

import com.whooo.barscanner.vo.Product;

import javax.inject.Inject;

/**
 * Created by thuongle on 1/30/16.
 */
public class ProductModel extends BaseModel {

    @Inject
    public ProductModel(Application application) {
        super(application);
    }

    public void saveProduct(Product product){
//        SQLite
//                .insert(Product.class)
//                .columnValues
    }
}
