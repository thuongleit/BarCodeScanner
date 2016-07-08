package com.whooo.babr.data.product;

public enum ProductSource {
    UPC("upc"),
    WALMART("walmart"),
    AMAZON("amazon"),
    UPC_ITEM_DB("upcitemdb"),
    UPC_DATABASE("upcdatabase"),
    IN_APP("babr");

    private final String mDisplay;

    ProductSource(String displayName) {
        this.mDisplay = displayName;
    }

    public String getDisplay() {
        return mDisplay;
    }
}
