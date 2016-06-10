package com.jokotech.babr.view.scan;

import android.os.Parcelable;

import com.jokotech.babr.vo.Product;

import java.util.List;

/**
 * Created by thuongle on 1/3/16.
 */
public interface ScanView {

    void showProgress(boolean show);

    void playRingtone();

    void onRequestSuccess(Parcelable parcelable);

    void onRequestSuccessList(List<Product> parcelables);
}
