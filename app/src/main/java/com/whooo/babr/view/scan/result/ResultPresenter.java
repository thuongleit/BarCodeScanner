package com.whooo.babr.view.scan.result;

import com.google.firebase.FirebaseNetworkException;
import com.whooo.babr.data.product.ProductRepository;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class ResultPresenter implements ResultContract.Presenter {
    private ResultContract.View mView;
    private final ResultViewModel mViewModel;
    private final ProductRepository mProductRespository;
    private CompositeSubscription mSubscriptions;

    public ResultPresenter(ResultContract.View view, ResultViewModel viewModel, ProductRepository productRespository) {
        this.mView = view;
        this.mViewModel = viewModel;
        this.mProductRespository = productRespository;

        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }

    @Override
    public void onDestroy() {
        mSubscriptions = null;
        mView = null;
    }

    @Override
    public ResultViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void saveProducts() {
        mSubscriptions.add(
                mProductRespository
                        .saveProducts(mViewModel.data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(result -> {
                            mView.onSaveSuccess(result);
                        }, e -> {
                            if (e instanceof FirebaseNetworkException) {
                                mView.showNetworkError();
                            } else {
                                mView.requestFailed(e.getMessage());
                            }
                        }));
    }
}
