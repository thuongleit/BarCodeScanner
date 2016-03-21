package com.thuongleit.babr.data.remote;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.vo.Product;

import java.util.List;

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

    public void saveProduct(Product product) {
        //save to parse
        ParseObject parseProduct = new ParseObject(Constant.PARSE_PRODUCTS);
        parseProduct.put("userId", ParseUser.getCurrentUser().getObjectId());
        if (product.getImageUrl() != null) {
            parseProduct.put("image", product.getImageUrl());
        }
        if (product.getUpcA() != null) {
            parseProduct.put("upcA", product.getUpcA());
        }
        if (product.getEan() != null) {
            parseProduct.put("ean", product.getEan());
        }
        if (product.getCountry() != null) {
            parseProduct.put("country", product.getCountry());
        }
        if (product.getManufacture() != null) {
            parseProduct.put("manufacture", product.getManufacture());
        }
        if (product.getModel() != null) {
            parseProduct.put("model", product.getModel());
        }
        if (product.getName() != null) {
            parseProduct.put("name", product.getName());
        }
        if (product.getSource() != null) {
            parseProduct.put("source", product.getName());
        }
        parseProduct.put("quantity", product.getQuantity());

        parseProduct.saveInBackground();
    }

    public void deleteProduct(String query) {
        ParseQuery<ParseObject> queryStm = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId())
                .whereEqualTo("objectId",query);
        queryStm.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                try {
                    object.delete();
                    object.saveInBackground();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public Observable<Product> getProducts() {
        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {
                query.findInBackground((objects, e) -> {
                    if (e == null && objects != null)
                        for (ParseObject object : objects) {
                            String image = object.getString("image");
                            String upcA = object.getString("upcA");
                            String ean = object.getString("ean");
                            String country = object.getString("country");
                            String manufacture = object.getString("manufacture");
                            String model = object.getString("model");
                            String name = object.getString("name");
                            String source = object.getString("source");
                            String objectId = object.getObjectId();
                            Number quantity = object.getNumber("quantity");

                            Product product = new Product();
                            product.setImageUrl(image);
                            product.setUpcA(upcA);
                            product.setEan(ean);
                            product.setCountry(country);
                            product.setManufacture(manufacture);
                            product.setName(name);
                            product.setObjectId(objectId);
                            product.setModel(model);

                            subscriber.onNext(product);
                        }
                    subscriber.onCompleted();
                });
            }
        });
    }



    public Observable<Product> getProductBABR(String id){
        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", id);
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {
                query.findInBackground((objects, e) -> {
                    if (e == null && objects != null)
                        for (ParseObject object : objects) {
                            String image = object.getString("image");
                            String upcA = object.getString("upcA");
                            String ean = object.getString("ean");
                            String country = object.getString("country");
                            String manufacture = object.getString("manufacture");
                            String model = object.getString("model");
                            String name = object.getString("name");
                            String source = object.getString("source");
                            String objectId = object.getObjectId();
                            Number quantity = object.getNumber("quantity");

                            Product product = new Product();
                            product.setImageUrl(image);
                            product.setUpcA(upcA);
                            product.setEan(ean);
                            product.setCountry(country);
                            product.setManufacture(manufacture);
                            product.setName(name);
                            product.setObjectId(objectId);
                            product.setModel(model);

                            subscriber.onNext(product);
                        }
                    subscriber.onCompleted();
                });
            }
        });
    }


    public Observable<List<Product>> saveListProduct(List<Product> productList){
        return Observable.just(productList).doOnNext(productList1 -> {
           for (Product product:productList1){
             saveProduct(product);
           }
        });
    }
}
