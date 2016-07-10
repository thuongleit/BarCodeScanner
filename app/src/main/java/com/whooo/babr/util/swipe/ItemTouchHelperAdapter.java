package com.whooo.babr.util.swipe;

/**
 * Created by ThongLe on 7/10/2016.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
