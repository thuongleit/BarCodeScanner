package com.whooo.barscanner.mvp.views;

import com.whooo.barscanner.model.BarCode;

/**
 * Created by thuongle on 1/3/16.
 */
public interface ScanQrCodeView extends ErrorableView {

    void onExecuteFinished(BarCode barCode);
}
