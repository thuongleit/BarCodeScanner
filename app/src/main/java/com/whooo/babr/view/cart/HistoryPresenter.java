package com.whooo.babr.view.cart;

import android.support.annotation.NonNull;

import com.whooo.babr.data.product.ProductRepository;

/**
 * Created by ThongLe on 7/12/2016.
 */

public class HistoryPresenter implements HistoryContract.Presenter {

    private HistoryContract.View mView;
    @NonNull
    private final ProductRepository mProductRepository;
    private Object products;

    public HistoryPresenter( HistoryContract.View view, @NonNull ProductRepository productRepository) {
        mView = view;
        mProductRepository = productRepository;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onDestroy() {
        mView = null;
    }

}
