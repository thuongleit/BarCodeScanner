package com.whooo.babr.view.binding;

import java.util.List;

public interface ChildItemsClickBinder<T> {

    List<ChildItemsClickHandler<T>> childItemsClickHandlers();

    int getBindingVariable(T model);
}
