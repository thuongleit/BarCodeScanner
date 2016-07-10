package com.whooo.babr.data.exception;

/**
 * Created by thuongle on 1/3/16.
 */
public class BarCodeNotFoundException extends Exception {

    public BarCodeNotFoundException() {
    }

    public BarCodeNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public BarCodeNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BarCodeNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
