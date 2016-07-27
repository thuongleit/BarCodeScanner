package com.whooo.babr.view.cart;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.whooo.babr.BR;
import com.whooo.babr.R;
import com.whooo.babr.view.binding.ConditionalDataBinder;
import com.whooo.babr.view.binding.ItemBinder;
import com.whooo.babr.vo.Cart;
import com.whooo.babr.vo.Product;

import java.util.List;

public class CartViewModel extends BaseObservable {

    public final ObservableBoolean needPending = new ObservableBoolean();
    public final ObservableBoolean loading = new ObservableBoolean();
    public final ObservableBoolean empty = new ObservableBoolean();

    public final ObservableList<Cart> data = new ObservableArrayList<>();

    public final ItemBinder<Product> itemViewBinder = new ConditionalDataBinder<Product>(BR.item, R.layout.item_cart) {
        @Override
        public boolean canHandle(Product model) {
            return true;
        }
    };

    public CartViewModel(boolean pending) {
        this.loading.set(false);
        this.empty.set(false);
        this.needPending.set(pending);
    }

    public void setData(List<Cart> data) {
        this.data.addAll(data);
        if (this.data.isEmpty()) {
            empty.set(true);
        } else {
            empty.set(false);
        }
        loading.set(false);
    }

    public void loading() {
        loading.set(true);
        empty.set(false);
        data.clear();
    }

    public void removeItem(Cart cart) {
        this.data.remove(cart);
        if (this.data.isEmpty()) {
            empty.set(true);
        }
    }
}
