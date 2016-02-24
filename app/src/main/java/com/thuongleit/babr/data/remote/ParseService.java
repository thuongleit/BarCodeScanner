package com.thuongleit.babr.data.remote;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.vo.UpcProduct;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by thuongle on 1/31/16.
 */
@Singleton
public class ParseService {

    @Inject
    public ParseService() {
    }

    public void saveProduct(UpcProduct upcProduct) {
        //save to parse
        ParseObject parseProduct = new ParseObject(Constant.PARSE_PRODUCTS);
        parseProduct.put("userId", ParseUser.getCurrentUser().getObjectId());
        if (upcProduct.getImage() != null) {
            parseProduct.put("image", upcProduct.getImage());
        }
        if (upcProduct.getUpcA() != null) {
            parseProduct.put("upcA", upcProduct.getUpcA());
        }
        if (upcProduct.getEan() != null) {
            parseProduct.put("ean", upcProduct.getEan());
        }
        if (upcProduct.getCountry() != null) {
            parseProduct.put("country", upcProduct.getCountry());
        }
        if (upcProduct.getManufacture() != null) {
            parseProduct.put("manufacture", upcProduct.getManufacture());
        }
        if (upcProduct.getModel() != null) {
            parseProduct.put("model", upcProduct.getModel());
        }
        parseProduct.put("quantity", upcProduct.getQuantity());

        parseProduct.saveInBackground();
    }

    public Observable<UpcProduct> getProducts() {
        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        return Observable.create(new Observable.OnSubscribe<UpcProduct>() {
            @Override
            public void call(Subscriber<? super UpcProduct> subscriber) {
                query.findInBackground((objects, e) -> {
                    if (e == null && objects != null)
                        for (ParseObject object : objects) {
                            String image = object.getString("image");
                            String upcA = object.getString("upcA");
                            String ean = object.getString("ean");
                            String country = object.getString("country");
                            String manufacture = object.getString("manufacture");
                            String model = object.getString("model");
                            Number quantity = object.getNumber("quantity");

                            UpcProduct upcProduct = new UpcProduct();
                            upcProduct.setImage(image);
                            upcProduct.setUpcA(upcA);
                            upcProduct.setEan(ean);
                            upcProduct.setCountry(country);
                            upcProduct.setManufacture(manufacture);
                            upcProduct.setModel(model);

                            subscriber.onNext(upcProduct);
                        }
                    subscriber.onCompleted();
                });
            }
        });
    }
}
