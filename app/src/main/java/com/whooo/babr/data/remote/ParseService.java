package com.whooo.babr.data.remote;

import android.util.Log;

import com.whooo.babr.vo.CheckoutHistory;
import com.whooo.babr.vo.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

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
//        ParseObject parseProduct = new ParseObject(Constant.PARSE_PRODUCTS);
//        parseProduct.put("userId", ParseUser.getCurrentUser().getObjectId());
//        if (product.getImageUrl() != null) {
//            parseProduct.put("image", product.getImageUrl());
//        }
//        if (product.getUpcA() != null) {
//            parseProduct.put("upcA", product.getUpcA());
//        }
//        if (product.getEan() != null) {
//            parseProduct.put("ean", product.getEan());
//        }
//        if (product.getCountry() != null) {
//            parseProduct.put("country", product.getCountry());
//        }
//        if (product.getManufacture() != null) {
//            parseProduct.put("manufacture", product.getManufacture());
//        }
//        if (product.getModel() != null) {
//            parseProduct.put("model", product.getModel());
//        }
//        if (product.getName() != null) {
//            parseProduct.put("name", product.getName());
//        }
//        if (product.getSource() != null) {
//            parseProduct.put("source", product.getSource());
//        }
//
//        parseProduct.put("listId", "a");
//
//        parseProduct.put("quantity", product.getQuantity());
//
//        parseProduct.saveInBackground();
    }

    public void saveProductHistory(CheckoutHistory product) {
        //save to parse
//        ParseObject parseProduct = new ParseObject(Constant.PARSE_NAMEHISTORY);
//        parseProduct.put("userId", ParseUser.getCurrentUser().getObjectId());
//
//        parseProduct.put("name", product.getName());
//
//        parseProduct.put("listId",product.getListId());
//        parseProduct.put("size",product.getSize());
//
//
//        parseProduct.saveInBackground();
    }

    public void saveProductNoCheckout(String listId, Product product) {
//        Log.d("passedProductNo", "đasadas");
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//        query.getInBackground(product.getObjectId(), new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//
//                if (e == null) {
//
//                    object.put("listId", listId);
//
//                    object.saveInBackground();
//                }
//            }
//        });

    }

    public void deleteProduct(String query) {
        return;
//        ParseQuery<ParseObject> queryStm = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId())
//                .whereEqualTo("objectId", query);
//        queryStm.getFirstInBackground(new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                try {
//                    object.delete();
//                    object.saveInBackground();
//                } catch (ParseException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
    }

    public Observable<Product> getProductsCheckout(String listId) {
        return null;
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId()).whereEqualTo("listId", listId);
//        return Observable.create(new Observable.OnSubscribe<Product>() {
//            @Override
//            public void call(Subscriber<? super Product> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upcA = object.getString("upcA");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            String name = object.getString("name");
//                            String objectId = object.getObjectId();
//                            String source = object.getString("source");
//                            String listId = object.getString("listId");
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.setImageUrl(image);
//                            product.setUpcA(upcA);
//                            product.setEan(ean);
//                            product.setCountry(country);
//                            product.setManufacture(manufacture);
//                            product.setName(name);
//                            product.setObjectId(objectId);
//                            product.setModel(model);
//                            product.setSource(source);
//                            product.setListId(listId);
//
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
    }

    public Observable<Product> getProductsCheckoutScan(String listId) {
        return null;
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("listId", listId);
//        return Observable.create(new Observable.OnSubscribe<Product>() {
//            @Override
//            public void call(Subscriber<? super Product> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upcA = object.getString("upcA");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            String name = object.getString("name");
//                            String objectId = object.getObjectId();
//                            String source = object.getString("source");
//                            String listId = object.getString("listId");
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.setImageUrl(image);
//                            product.setUpcA(upcA);
//                            product.setEan(ean);
//                            product.setCountry(country);
//                            product.setManufacture(manufacture);
//                            product.setName(name);
//                            product.setObjectId(objectId);
//                            product.setModel(model);
//                            product.setSource(source);
//                            product.setListId(listId);
//
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
    }


    public Observable<Product> getProducts() {
        return null;
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//        return Observable.create(new Observable.OnSubscribe<Product>() {
//            @Override
//            public void call(Subscriber<? super Product> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upcA = object.getString("upcA");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            String name = object.getString("name");
//                            String source = object.getString("source");
//                            String objectId = object.getObjectId();
//                            String listId = object.getString("listId");
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.setImageUrl(image);
//                            product.setUpcA(upcA);
//                            product.setEan(ean);
//                            product.setCountry(country);
//                            product.setManufacture(manufacture);
//                            product.setName(name);
//                            product.setObjectId(objectId);
//                            product.setModel(model);
//                            product.setSource(source);
//                            product.setListId(listId);
//
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
    }

    public Observable<CheckoutHistory> getProductsHistory() {
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_NAMEHISTORY).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//        return Observable.create(new Observable.OnSubscribe<CheckoutHistory>() {
//            @Override
//            public void call(Subscriber<? super CheckoutHistory> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//
//                            String name = object.getString("name");
//                            String listId = object.getString("listId");
//                            long size = object.getLong("size");
//
//                            CheckoutHistory product = new CheckoutHistory();
//                            product.setName(name);
//                            product.setListId(listId);
//                            product.setSize(size);
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
        return null;
    }

    public Observable<Product> getProductBABR(String id) {
        return null;
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", id);
//        return Observable.create(new Observable.OnSubscribe<Product>() {
//            @Override
//            public void call(Subscriber<? super Product> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upcA = object.getString("upcA");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            String name = object.getString("name");
//                            String source = object.getString("source");
//                            String listId = object.getString("listId");
//                            String objectId = object.getObjectId();
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.setImageUrl(image);
//                            product.setUpcA(upcA);
//                            product.setEan(ean);
//                            product.setCountry(country);
//                            product.setManufacture(manufacture);
//                            product.setName(name);
//                            product.setObjectId(objectId);
//                            product.setModel(model);
//                            product.setSource(source);
//                            product.setListId(listId);
//
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
    }


    public Observable<List<Product>> saveListProduct(List<Product> productList) {
        return Observable.just(productList).doOnNext(productList1 -> {
            for (Product product : productList1) {
                saveProduct(product);
            }
        });
    }

    public Observable<List<Product>> saveListProductNoCheckout(List<Product> productList, String listId) {
        Log.d("passedProductNo", "saveListProductNoCheckout");
        return Observable.just(productList).doOnNext(productList1 -> {
            for (Product product : productList1) {

                saveProductNoCheckout(listId, product);
            }
        });
    }
}
