package com.whooo.babr.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.whooo.babr.BR;


/**
 * Created by ThongLe on 7/11/2016.
 */

public class MainViewHandlerWrapper extends BaseObservable {

    private int typeId;
    private String textViewHandler;

    public MainViewHandlerWrapper() {
    }


    public MainViewHandlerWrapper(int typeId, String text) {
        this.typeId = typeId;
        this.textViewHandler = text;
    }

    @Bindable
    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int type) {
        this.typeId = type;
        notifyPropertyChanged(BR.typeId);
    }

    @Bindable
    public String getTextViewHandler() {
        return textViewHandler;
    }

    public void setTextViewHandler(String textViewHandler) {
        this.textViewHandler = textViewHandler;
        notifyPropertyChanged(BR.textViewHandler);
    }
}
