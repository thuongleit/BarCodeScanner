package com.thuongleit.babr.view.scan;

import android.os.Parcelable;

import com.thuongleit.babr.view.base.ErrorableView;

/**
 * Created by thuongle on 1/3/16.
 */
public interface ScanView extends ErrorableView {

    void showProgress(boolean show);

    void playRingtone();

    void onEmptyProductReturn();

    void onRequestSuccess(Parcelable parcelable);
}
