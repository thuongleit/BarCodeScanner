package com.whooo.babr.view.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.whooo.babr.BR;
import com.whooo.babr.R;
import com.whooo.babr.view.binding.ConditionalDataBinder;
import com.whooo.babr.view.binding.ItemBinder;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends BaseObservable {

    private boolean isNetworkError = false;
    private boolean isLoading = false;
    private boolean isEmpty = false;

    @NonNull
    private List<Product> data;
    @NonNull
    private ItemBinder<Product> itemViewBinder;

    public MainViewModel() {
        this.data = new ArrayList<>();

        itemViewBinder = new ConditionalDataBinder<Product>(BR.item, R.layout.item_product) {
            @Override
            public boolean canHandle(Product model) {
                return true;
            }
        };
    }

    @Bindable
    public boolean isNetworkError() {
        return isNetworkError;
    }

    public void setNetworkError() {
        data.clear();
        isEmpty = true;
        isNetworkError = true;
        isLoading = false;

        notifyChanged();
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading() {
        data.clear();
        isEmpty = true;
        isLoading = true;
        isNetworkError = false;

        notifyChanged();
    }

    @Bindable
    public boolean isEmpty() {
        return isEmpty;
    }

    @Bindable
    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> products) {
        isLoading = false;
        isNetworkError = false;
        isEmpty = (products == null || products.isEmpty());

        data.addAll(products);

        notifyChanged();
    }

    @Bindable
    public ItemBinder<Product> getItemViewBinder() {
        return itemViewBinder;
    }

    private void notifyChanged() {
        notifyPropertyChanged(BR.data);
        notifyPropertyChanged(BR.loading);
        notifyPropertyChanged(BR.networkError);
        notifyPropertyChanged(BR.empty);
    }
}

