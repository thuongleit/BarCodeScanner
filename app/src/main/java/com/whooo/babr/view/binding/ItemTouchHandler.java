package com.whooo.babr.view.binding;

public interface ItemTouchHandler<T> {

    void onItemMove(int position, T viewModel);

    void onItemDismiss(int position, T viewModel);
}
