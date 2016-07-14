package com.whooo.babr.view.scan.result;

import com.whooo.babr.data.product.ProductRepository;
import com.whooo.babr.di.PerActivity;
import com.whooo.babr.vo.Product;

import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
class ResultModule {

    private final List<Product> mData;
    private ResultContract.View mView;

    public ResultModule(ResultContract.View view, List<Product> products) {
        mView = view;
        mData = products;
    }

    @Provides
    @PerActivity
    ResultViewModel provideViewModel() {
        return new ResultViewModel(mData);
    }

    @Provides
    @PerActivity
    ResultContract.Presenter providePresenter(ProductRepository productRespository, ResultViewModel viewModel) {
        return new ResultPresenter(mView, viewModel, productRespository);
    }
}
