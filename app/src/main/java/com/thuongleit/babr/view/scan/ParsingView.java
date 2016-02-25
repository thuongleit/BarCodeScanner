package com.thuongleit.babr.view.scan;

import com.thuongleit.babr.view.base.ErrorableView;
import com.thuongleit.babr.vo.Product;

/**
 * Created by thuongle on 2/25/16.
 */
public interface ParsingView extends ErrorableView {

    void onParseSuccess(Product product);
}
