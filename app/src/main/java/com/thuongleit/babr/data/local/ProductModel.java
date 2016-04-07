package com.thuongleit.babr.data.local;

import android.app.Application;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Insert;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.thuongleit.babr.vo.Product;
import com.thuongleit.babr.vo.ProductHistory;
import com.thuongleit.babr.vo.ProductHistory_Table;
import com.thuongleit.babr.vo.Product_Table;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import timber.log.Timber;

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
        Insert<Product> insertQuery = SQLite
                .insert(Product.class)
                .columnValues(
                        Product_Table.country.eq(product.getCountry()),
                        Product_Table.ean.eq(product.getEan()),
                        Product_Table.name.eq(product.getName()),
                        Product_Table.imageUrl.eq(product.getImageUrl()),
                        Product_Table.manufacture.eq(product.getManufacture()),
                        Product_Table.model.eq(product.getModel()),
                        Product_Table.quantity.eq(product.getQuantity()),
                        Product_Table.source.eq(product.getSource()),
                        Product_Table.listId.eq(product.getListId()),
                        Product_Table.upcA.eq(product.getUpcA()));
        Product_Table.objectId.eq(product.getObjectId());
        Timber.i(insertQuery.getQuery());
        insertQuery.execute();
    }

    public void saveProductHistory(ProductHistory productHistory) {
        Insert<ProductHistory> insertQuery = SQLite
                .insert(ProductHistory.class)
                .columnValues(
                        ProductHistory_Table.name.eq(productHistory.getName()),
                        ProductHistory_Table.listId.eq(productHistory.getListId()));
        Timber.i(insertQuery.getQuery());
        insertQuery.execute();
    }

    public void saveProductNoCheckout(String listId) {
        SQLite.update(Product.class).set((Product_Table.listId).is(listId)).where((Product_Table.listId).is("a")).execute();
    }



    public List<ProductHistory> loadProductsHistory() {
        return SQLite
                .select()
                .from(ProductHistory.class)
                .queryList();
    }


    public List<Product> loadProductsNoCheckout(String listId) {
        return SQLite
                .select()
                .from(Product.class)
                .where((Product_Table.listId).is(listId))
                .queryList();
    }

    public void deleteProduct(Product product) {

        product.delete();
    }

    public Observable<String> saveListProductNoCheckout(String listId) {
        return Observable.just(listId).doOnNext(listId1 -> {
            saveProductNoCheckout(listId1);

        });
    }
}
