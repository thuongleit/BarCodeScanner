package com.whooo.barscanner.mvp.presenters;

import com.whooo.barscanner.mvp.views.ScanQrCodeView;

/**
 * Created by thuongle on 1/3/16.
 */
public interface ScanQrCodePresenter extends Presenter<ScanQrCodeView> {

    void executeQrCode(String qrCode);
}
