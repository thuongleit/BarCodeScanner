package com.whooo.barscanner.mvp.presenters;

import com.whooo.barscanner.model.BarCode;
import com.whooo.barscanner.model.BarCodeNotFoundException;
import com.whooo.barscanner.mvp.views.ScanQrCodeView;
import com.whooo.barscanner.net.GetBarCodeAsyncTask;

/**
 * Created by thuongle on 1/3/16.
 */
public class ScanQrCodePresenterImpl implements ScanQrCodePresenter {
    private ScanQrCodeView mView;

    @Override
    public void attach(ScanQrCodeView view) {
        mView = view;
    }

    @Override
    public void deAttach() {

    }

    @Override
    public void executeQrCode(final String qrCode) {
        mView.showProgress();
        new GetBarCodeAsyncTask(new GetBarCodeAsyncTask.OnUpdateUICallback() {
            @Override
            public void onUpdateUI(BarCode barCode) {
                mView.hideProgress();
                if (barCode != null) {
                    mView.onExecuteFinished(barCode);
                } else {
                    String message = String.format("Number %s was incorrect or invalid, either the length or the the check digit may have been incorrect.", qrCode);
                    mView.onError(new BarCodeNotFoundException(message));
                }
            }
        }).execute("http://www.upcitemdb.com/upc/" + qrCode);
    }
}
