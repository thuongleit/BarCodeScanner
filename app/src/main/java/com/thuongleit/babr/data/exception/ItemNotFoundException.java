package com.thuongleit.babr.data.exception;

/**
 * Created by thuongle on 1/30/16.
 */
public class ItemNotFoundException extends Throwable {
    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String detailMessage) {
        super(detailMessage);
    }
}

