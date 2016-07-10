package com.whooo.babr.data.exception;

/**
 * Created by thuongle on 1/30/16.
 */
public class ItemParsingErrorException extends Throwable {

    public ItemParsingErrorException() {
    }

    public ItemParsingErrorException(String detailMessage) {
        super(detailMessage);
    }
}
