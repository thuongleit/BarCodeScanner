package com.whooo.babr.view.cart.detail;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.whooo.babr.BR;
import com.whooo.babr.R;
import com.whooo.babr.view.binding.ConditionalDataBinder;
import com.whooo.babr.view.binding.ItemBinder;
import com.whooo.babr.vo.Product;

import java.util.List;

public class DetailViewModel extends BaseObservable {

    public final ObservableBoolean loading = new ObservableBoolean();
    public final ObservableBoolean empty = new ObservableBoolean();

    public final ObservableArrayList<Product> data = new ObservableArrayList<>();

    public final ItemBinder<Product> itemViewBinder = new ConditionalDataBinder<Product>(BR.item, R.layout.item_product) {
        @Override
        public boolean canHandle(Product model) {
            return true;
        }
    };

    public DetailViewModel() {
        loading.set(false);
        empty.set(false);
    }

    public void loading() {
        loading.set(true);
        empty.set(false);
        data.clear();
    }

    public void setData(List<Product> products) {
        data.addAll(products);
        if (data.isEmpty()) {
            empty.set(true);
        } else {
            empty.set(false);
        }
        loading.set(false);
    }
}
