package com.whooo.barscanner.view.scan;

import com.whooo.barscanner.view.base.ErrorableView;
import com.whooo.barscanner.vo.Product;

/**
 * Created by thuongle on 1/3/16.
 */
public interface ScanQrCodeView extends ErrorableView {

    void onExecuteFinished(Product product);

    void showProgress(boolean show);
}
