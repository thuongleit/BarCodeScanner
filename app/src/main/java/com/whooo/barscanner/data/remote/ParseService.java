package com.whooo.barscanner.data.remote;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.whooo.barscanner.config.Constant;
import com.whooo.barscanner.vo.Product;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by thuongle on 1/31/16.
 */
@Singleton
public class ParseService {

    @Inject
    public ParseService() {
    }

    public void saveProduct(Product product) {
        //save to parse
        ParseObject parseProduct = new ParseObject(Constant.PARSE_PRODUCTS);
        parseProduct.add("userId", ParseUser.getCurrentUser().getObjectId());
        parseProduct.add("image", product.getImage());
        parseProduct.add("upcA", product.getUpcA());
        parseProduct.add("ean", product.getEan());
        parseProduct.add("country", product.getCountry());
        parseProduct.add("manufacture", product.getManufacture());
        parseProduct.add("model", product.getModel());
        parseProduct.add("quantity", product.getQuantity());

        parseProduct.saveInBackground();
    }
}
