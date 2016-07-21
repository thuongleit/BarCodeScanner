package com.whooo.babr.view.main;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.whooo.babr.BR;
import com.whooo.babr.R;
import com.whooo.babr.view.binding.ConditionalDataBinder;
import com.whooo.babr.view.binding.ItemBinder;
import com.whooo.babr.vo.Product;

import java.util.List;

public class ShopViewModel {

    public final ObservableBoolean loading = new ObservableBoolean();
    public final ObservableBoolean empty = new ObservableBoolean();

    public final ObservableArrayList<Product> data = new ObservableArrayList<>();
    public final ItemBinder<Product> itemViewBinder = new ConditionalDataBinder<Product>(BR.item, R.layout.item_product) {
        @Override
        public boolean canHandle(Product model) {
            return true;
        }
    };

    public void setLoading() {
        data.clear();
        loading.set(true);
        empty.set(false);
    }

    public void setData(List<Product> products) {
        loading.set(false);
        if (products == null || products.isEmpty()) {
            data.clear();
            empty.set(true);
        } else {
            empty.set(false);
            data.addAll(products);
        }
    }

    public void removeItem(Product product) {
        data.remove(product);

        if (data.isEmpty()) {
            empty.set(true);
        }
    }

    public void addItem(int position, Product product) {
        if (product != null) {
            empty.set(false);
        }
        data.add(position, product);
    }
}

