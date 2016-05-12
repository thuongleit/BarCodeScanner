package com.jokotech.babr.view.scan;

import com.jokotech.babr.view.base.ErrorableView;
import com.jokotech.babr.vo.Product;

/**
 * Created by thuongle on 2/25/16.
 */
public interface ParsingView extends ErrorableView {

    void onParseSuccess(Product product);
    void showProcess(boolean show);
}
