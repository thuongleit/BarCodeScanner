package com.whooo.babr.data.remote;

import android.util.Log;

import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class ParseServiceOK {

    @Inject
    public ParseServiceOK() {
    }

    public void saveProduct(Product product) {
        //save to parse
//        ParseObject parseProduct = new ParseObject(Constant.PARSE_PRODUCTS);
//        parseProduct.put("userId", ParseUser.getCurrentUser().getObjectId());
//        if (product.getImageUrl() != null) {
//            parseProduct.put("image", product.getImageUrl());
//        }
//        if (product.getUpcA() != null) {
//            parseProduct.put("upc", product.getUpcA());
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
//        parseProduct.put("cartId", "a");
//
//        parseProduct.put("quantity", product.getQuantity());
//
//        parseProduct.saveInBackground();
    }

    public void saveProductHistory(Cart product) {
        //save to parse
//        ParseObject parseProduct = new ParseObject(Constant.PARSE_NAMEHISTORY);
//        parseProduct.put("userId", ParseUser.getCurrentUser().getObjectId());
//
//        parseProduct.put("name", product.getName());
//
//        parseProduct.put("cartId",product.getListId());
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
//                    object.put("cartId", cartId);
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
        return Observable.empty();
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId()).whereEqualTo("cartId", cartId);
//        return Observable.create(new Observable.OnSubscribe<Product>() {
//            @Override
//            public void call(Subscriber<? super Product> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upc = object.getString("upc");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            String name = object.getString("name");
//                            String objectId = object.getObjectId();
//                            String source = object.getString("source");
//                            String cartId = object.getString("cartId");
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.setImageUrl(image);
//                            product.setUpcA(upc);
//                            product.setEan(ean);
//                            product.setCountry(country);
//                            product.setManufacture(manufacture);
//                            product.setName(name);
//                            product.setObjectId(objectId);
//                            product.setModel(model);
//                            product.setSource(source);
//                            product.setListId(cartId);
//
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
    }

    public Observable<Product> getProductsCheckoutScan(String listId) {
        return Observable.empty();
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("cartId", cartId);
//        return Observable.create(new Observable.OnSubscribe<Product>() {
//            @Override
//            public void call(Subscriber<? super Product> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upc = object.getString("upc");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            String name = object.getString("name");
//                            String objectId = object.getObjectId();
//                            String source = object.getString("source");
//                            String cartId = object.getString("cartId");
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.setImageUrl(image);
//                            product.setUpcA(upc);
//                            product.setEan(ean);
//                            product.setCountry(country);
//                            product.setManufacture(manufacture);
//                            product.setName(name);
//                            product.setObjectId(objectId);
//                            product.setModel(model);
//                            product.setSource(source);
//                            product.setListId(cartId);
//
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
    }


    public Observable<Product> getProducts() {
        return Observable.empty();
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//        return Observable.create(new Observable.OnSubscribe<Product>() {
//            @Override
//            public void call(Subscriber<? super Product> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upc = object.getString("upc");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            String name = object.getString("name");
//                            String source = object.getString("source");
//                            String objectId = object.getObjectId();
//                            String cartId = object.getString("cartId");
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.setImageUrl(image);
//                            product.setUpcA(upc);
//                            product.setEan(ean);
//                            product.setCountry(country);
//                            product.setManufacture(manufacture);
//                            product.setName(name);
//                            product.setObjectId(objectId);
//                            product.setModel(model);
//                            product.setSource(source);
//                            product.setListId(cartId);
//
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
    }

    public Observable<Cart> getProductsHistory() {
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_NAMEHISTORY).whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//        return Observable.create(new Observable.OnSubscribe<Cart>() {
//            @Override
//            public void call(Subscriber<? super Cart> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//
//                            String name = object.getString("name");
//                            String cartId = object.getString("cartId");
//                            long size = object.getLong("size");
//
//                            Cart product = new Cart();
//                            product.setName(name);
//                            product.setListId(cartId);
//                            product.setSize(size);
//                            subscriber.onNext(product);
//                        }
//                    subscriber.onCompleted();
//                });
//            }
//        });
        return Observable.empty();
    }

    public Observable<Product> getProductBABR(String id) {
        return Observable.empty();
//        ParseQuery<ParseObject> query = new ParseQuery<>(Constant.PARSE_PRODUCTS).whereEqualTo("userId", objectId);
//        return Observable.create(new Observable.OnSubscribe<Product>() {
//            @Override
//            public void call(Subscriber<? super Product> subscriber) {
//                query.findInBackground((objects, e) -> {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upc = object.getString("upc");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            String name = object.getString("name");
//                            String source = object.getString("source");
//                            String cartId = object.getString("cartId");
//                            String objectId = object.getObjectId();
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.setImageUrl(image);
//                            product.setUpcA(upc);
//                            product.setEan(ean);
//                            product.setCountry(country);
//                            product.setManufacture(manufacture);
//                            product.setName(name);
//                            product.setObjectId(objectId);
//                            product.setModel(model);
//                            product.setSource(source);
//                            product.setListId(cartId);
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
