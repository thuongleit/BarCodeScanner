package com.jokotech.babr.view.scan;

import com.jokotech.babr.vo.Product;

/**
 * Created by thuongle on 2/25/16.
 */
public interface ParsingView {

    void onParseSuccess(Product product);
    void showProcess(boolean show);
}
