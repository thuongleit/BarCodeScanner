package com.thuongleit.babr.view.scan;

import com.thuongleit.babr.view.base.ErrorableView;
import com.thuongleit.babr.vo.Product;

/**
 * Created by thuongle on 1/3/16.
 */
public interface ScanQrCodeView extends ErrorableView {

    void onExecuteFinished(Product product);

    void showProgress(boolean show);
}
