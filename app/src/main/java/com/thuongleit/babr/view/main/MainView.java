package com.thuongleit.babr.view.main;

import com.thuongleit.babr.view.base.ErrorableView;
import com.thuongleit.babr.vo.UpcProduct;

import java.util.List;

/**
 * Created by thuongle on 1/31/16.
 */
public interface MainView extends ErrorableView {
    void showProgress(boolean show);

    void showProducts(List<UpcProduct> upcProducts);

    void showEmptyView();
}
