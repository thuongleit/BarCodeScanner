package com.whooo.babr.view.scan.result;

import com.google.firebase.FirebaseNetworkException;
import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.view.binding.ItemTouchHandler;
import com.whooo.babr.vo.Product;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

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
    public void saveProducts(String cardId) {
        Timber.d("Save products with size %s", mViewModel.data.size());
        mView.showProgress(true);
        mSubscriptions.add(
                mProductRespository
                        .saveProducts(cardId, mViewModel.data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            Timber.d("Save products success.");
                            mView.showProgress(false);
                            mView.onSaveSuccess(result);
                        }, e -> {
                            Timber.e(e, "Save products failed.");
                            mView.showProgress(false);
                            if (e instanceof FirebaseNetworkException) {
                                mView.showNetworkError();
                            } else {
                                mView.requestFailed(e.getMessage());
                            }
                        }));
    }

    @Override
    public ItemTouchHandler<Product> itemTouchHandler() {
        return new ItemTouchHandler<Product>() {
            @Override
            public void onItemMove(int position, Product product) {

            }

            @Override
            public void onItemDismiss(int position, Product product) {
                try {
                    final Product clone = (Product) product.clone();

                    mView.addPendingRemove(position, clone);
                } catch (CloneNotSupportedException e) {
                    Timber.e(e, "WTF error here?");
                    mView.showInAppError();
                    return;
                }
                mViewModel.removeItem(product);
            }
        };
    }

    @Override
    public void undoRemovedProduct(int position, Product product) {
        mViewModel.addItem(position, product);
    }

    @Override
    public void removeProducts(Product product) {
//        mViewModel.removeItem(product);
    }
}
