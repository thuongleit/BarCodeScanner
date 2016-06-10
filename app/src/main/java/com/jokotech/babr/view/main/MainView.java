package com.jokotech.babr.view.main;

import com.jokotech.babr.vo.Product;

import java.util.List;

/**
 * Created by thuongle on 1/31/16.
 */
public interface MainView extends ErrorableView {
    void showProgress(boolean show);

    void showProducts(List<Product> products);

    void showEmptyView();
}
