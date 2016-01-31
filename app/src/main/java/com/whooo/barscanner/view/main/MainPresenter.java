package com.whooo.barscanner.view.main;

import com.parse.ParseUser;
import com.whooo.barscanner.data.DataManager;
import com.whooo.barscanner.view.base.BasePresenter;
import com.whooo.barscanner.vo.Product;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by thuongle on 1/31/16.
 */
public class MainPresenter extends BasePresenter<MainView> {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void getProducts() {
        checkViewAttached();
        mView.showProgress(true);
        mSubscription = mDataManager.getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {
                        mView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showProgress(false);
                        mView.showGeneralError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        mView.showProducts(products);
                    }
                });
        if (ParseUser.getCurrentUser() == null) {
//            try {
//                sqlHelper = new SQLHelper(this);
//                products = sqlHelper.getBarCodeDao().queryForAll();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            RecyclerView.Adapter adapter = new BarViewRecyclerAdapter(MainActivity.this, products);
//            mRecyclerView.setAdapter(adapter);

        } else {
//            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Products").whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> objects, ParseException e) {
//                    if (e == null && objects != null)
//                        for (ParseObject object : objects) {
//                            String image = object.getString("image");
//                            String upcA = object.getString("upcA");
//                            String ean = object.getString("ean");
//                            String country = object.getString("country");
//                            String manufacture = object.getString("manufacture");
//                            String model = object.getString("model");
//                            Number quantity = object.getNumber("quantity");
//
//                            Product product = new Product();
//                            product.image = image;
//                            product.upcA = upcA;
//                            product.ean = ean;
//                            product.country = country;
//                            product.manufacture = manufacture;
//                            product.model = model;
//
//                            products.add(product);
//                        }
//                    RecyclerView.Adapter adapter = new BarViewRecyclerAdapter(MainActivity.this, products);
//                    mRecyclerView.setAdapter(adapter);
        }
//            });
//        }
    }
}
