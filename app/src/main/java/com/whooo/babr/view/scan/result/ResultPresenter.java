package com.whooo.babr.view.scan.result;

import com.whooo.babr.data.product.ProductRepository;

public class ResultPresenter implements ResultContract.Presenter {

    private final ResultContract.View mView;
    private final ProductRepository mProductRepository;

    public ResultPresenter(ResultContract.View view, ProductRepository productRepository) {
        mView = view;
        mProductRepository = productRepository;
    }

    @Override
    public void subscribe() {
//        mView.showProcess(true);
//        mSubscription = mDataManager
//                .parseProductFromAmazon(detailPageURL)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        product -> mView.onParseSuccess(product),
//                        e -> {
//                            if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
//                                mView.onNetworkFailed();
//                                mView.showProcess(false);
//                            } else {
//                                mView.onGeneralFailed(e.getMessage());
//                                mView.showProcess(false);
//                            }
//                        });
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getProducts(String detailPageURL) {

    }
}
