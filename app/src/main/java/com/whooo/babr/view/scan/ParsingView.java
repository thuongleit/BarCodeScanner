package com.whooo.babr.view.scan;

import com.whooo.babr.vo.Product;

/**
 * Created by thuongle on 2/25/16.
 */
public interface ParsingView {

    void onParseSuccess(Product product);
    void showProcess(boolean show);
}
