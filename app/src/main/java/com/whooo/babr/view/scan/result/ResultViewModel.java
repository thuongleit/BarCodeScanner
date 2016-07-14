package com.whooo.babr.view.scan.result;

import android.databinding.ObservableArrayList;

import com.whooo.babr.BR;
import com.whooo.babr.R;
import com.whooo.babr.view.binding.ConditionalDataBinder;
import com.whooo.babr.view.binding.ItemBinder;
import com.whooo.babr.vo.Product;

import java.util.List;

public class ResultViewModel {

    public final ObservableArrayList<Product> data = new ObservableArrayList<>();
    public final ItemBinder<Product> itemViewBinder = new ConditionalDataBinder<Product>(BR.item, R.layout.item_product) {
        @Override
        public boolean canHandle(Product model) {
            return true;
        }
    };

    public ResultViewModel(List<Product> products) {
        this.data.addAll(products);
    }
//
//    public ItemTouchHandler<Product> itemTouchHandler() {
//        return new ItemTouchHandler<Product>() {
//            @Override
//            public void onItemMove(int position, Product product) {
//
//            }
//
//            @Override
//            public void onItemDismiss(int position, Product product) {
//                try {
//                    final Product clone = (Product) product.clone();
//
//                    mView.addPendingRemove(position, clone);
//                } catch (CloneNotSupportedException e) {
//                    // TODO: 7/14/16 handle unexpected exception
//                    return;
//                }
//                mViewModel.removeItem(product);
//            }
//        };
//    }


    public void setData(List<Product> products) {
        data.addAll(products);
    }

    public void removeItem(Product product) {
        data.remove(product);
    }

    public void addItem(int position, Product product) {
        data.add(position, product);
    }
}

