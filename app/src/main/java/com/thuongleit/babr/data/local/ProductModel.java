package com.thuongleit.babr.data.local;

import android.app.Application;

import com.raizlabs.android.dbflow.sql.language.Insert;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thuongleit.babr.vo.UpcProduct;
import com.thuongleit.babr.vo.UpcProduct_Table;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    public void saveProduct(UpcProduct upcProduct) {
        Insert<UpcProduct> insertQuery = SQLite
                .insert(UpcProduct.class)
                .columnValues(
                        UpcProduct_Table.country.eq(upcProduct.getCountry()),
                        UpcProduct_Table.ean.eq(upcProduct.getEan()),
                        UpcProduct_Table.image.eq(upcProduct.getImage()),
                        UpcProduct_Table.manufacture.eq(upcProduct.getManufacture()),
                        UpcProduct_Table.model.eq(upcProduct.getModel()),
                        UpcProduct_Table.quantity.eq(upcProduct.getQuantity()),
                        UpcProduct_Table.upcA.eq(upcProduct.getUpcA()));
        Timber.i(insertQuery.getQuery());
        insertQuery.execute();
    }

    public List<UpcProduct> loadProducts() {
        return SQLite
                .select()
                .from(UpcProduct.class)
                .queryList();
    }
}
