package com.whooo.barscanner.view.main;

import com.whooo.barscanner.view.base.ErrorableView;
import com.whooo.barscanner.vo.Product;

import java.util.List;

/**
 * Created by thuongle on 1/31/16.
 */
public interface MainView extends ErrorableView {
    void showProgress(boolean show);

    void showProducts(List<Product> products);
}
