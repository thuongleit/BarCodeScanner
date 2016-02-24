package com.thuongleit.babr.view.scan;

import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.data.DataManager;
import com.thuongleit.babr.view.base.BasePresenter;
import com.thuongleit.babr.vo.UpcProduct;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by thuongle on 1/3/16.
 */
public class ProductLookupPresenter extends BasePresenter<ScanView> {

    private final DataManager mDataManager;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public ProductLookupPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void execute(final String code, String service) {
        checkViewAttached();
        mView.showProgress(true);

        Observable<List<UpcProduct>> listObservable = null;
        switch (service) {
            case Constant.KEY_AMAZON_SERVICE:
                listObservable = mDataManager.searchProductsInAmazon(code);
                break;
            case Constant.KEY_UPC_SERVICE:
                mDataManager.getProduct(code);
                break;
//            .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<UpcProduct>() {
//                        @Override
//                        public void onCompleted() {
//                            mView.showProgress(false);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            mView.showProgress(false);
//                            mView.showGeneralError(e.getMessage());
//                        }
//
//                        @Override
//                        public void onNext(UpcProduct upcProduct) {
//                            mView.onExecuteFinished(upcProduct);
//                        }
//                    });
        }

        listObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
