package com.thuongleit.babr.view.scan;

import com.thuongleit.babr.view.base.ErrorableView;
import com.thuongleit.babr.vo.UpcProduct;

/**
 * Created by thuongle on 1/3/16.
 */
public interface ScanView extends ErrorableView {

    void onExecuteFinished(UpcProduct upcProduct);

    void showProgress(boolean show);
}
