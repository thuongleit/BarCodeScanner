package com.whooo.babr.view.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collection;

public class RecyclerViewBinding {
    private static final int KEY_ITEMS = -123;
    private static final int KEY_CLICK_HANDLER = -124;
    private static final int KEY_LONG_CLICK_HANDLER = -125;
    private static final int KEY_ITEM_TOUCH_HANDLER = -126;
    private static final int KEY_ENABLE_SWIPE = -127;

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = "items", requireAll = true)
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            recyclerView.setTag(KEY_ITEMS, items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = "itemViewBinder", requireAll = false)
    public static <T> void setItemViewBinder(RecyclerView recyclerView, ItemBinder<T> itemViewMapper) {
        Collection<T> items = (Collection<T>) recyclerView.getTag(KEY_ITEMS);
        ClickHandler<T> clickHandler = (ClickHandler<T>) recyclerView.getTag(KEY_CLICK_HANDLER);
        LongClickHandler<T> longClickHandler = (LongClickHandler<T>) recyclerView.getTag(KEY_LONG_CLICK_HANDLER);
        ItemTouchHandler<T> itemTouchHandler = (ItemTouchHandler<T>) recyclerView.getTag(KEY_ITEM_TOUCH_HANDLER);
        Boolean enableSwipe = (Boolean) recyclerView.getTag(KEY_ENABLE_SWIPE);

        BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(itemViewMapper, items);
        if (clickHandler != null) {
            adapter.setClickHandler(clickHandler);
        }
        if (longClickHandler != null) {
            adapter.setLongClickHandler(longClickHandler);
        }
        if (itemTouchHandler != null) {
            adapter.setTouchHandler(itemTouchHandler);
        }

        recyclerView.setAdapter(adapter);

        if (enableSwipe != null && enableSwipe) {
            ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter, recyclerView.getContext());
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = "clickHandler", requireAll = false)
    public static <T> void setHandler(RecyclerView recyclerView, ClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setClickHandler(handler);
        } else {
            recyclerView.setTag(KEY_CLICK_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = "longClickHandler", requireAll = false)
    public static <T> void setHandler(RecyclerView recyclerView, LongClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setLongClickHandler(handler);
        } else {
            recyclerView.setTag(KEY_LONG_CLICK_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = "touchHandler", requireAll = false)
    public static <T> void setHandler(RecyclerView recyclerView, ItemTouchHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setTouchHandler(handler);
        } else {
            recyclerView.setTag(KEY_ITEM_TOUCH_HANDLER, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = "enableSwipe", requireAll = false)
    public static <T> void setHandler(RecyclerView recyclerView, Boolean enableSwipe) {
        if (enableSwipe) {
            recyclerView.setTag(KEY_ENABLE_SWIPE, enableSwipe);
        }
    }
}