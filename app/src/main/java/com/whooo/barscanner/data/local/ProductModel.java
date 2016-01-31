package com.whooo.barscanner.data.local;

import android.app.Application;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.whooo.barscanner.vo.Product;
import com.whooo.barscanner.vo.Product_Table;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by thuongle on 1/30/16.
 */
@Singleton
public class ProductModel extends BaseModel {

    @Inject
    public ProductModel(Application application) {
        super(application);
    }

    public void saveProduct(Product product) {
        SQLite
                .insert(Product.class)
                .columnValues(
                        Product_Table.id.eq(product.getId()),
                        Product_Table.country.eq(product.getCountry()),
                        Product_Table.ean.eq(product.getEan()),
                        Product_Table.image.eq(product.getImage()),
                        Product_Table.manufacture.eq(product.getManufacture()),
                        Product_Table.model.eq(product.getModel()),
                        Product_Table.quantity.eq(product.getQuantity()),
                        Product_Table.upcA.eq(product.getUpcA()));
    }

    public List<Product> loadProducts() {
        return SQLite
                .select()
                .from(Product.class)
                .queryList();
    }
}
