package com.whooo.babr.data.product;

import com.whooo.babr.data.remote.amazon.util.AmazonSignedRequestsHelper;

public enum ProductSource {
    SEARCH_UPC("upc", "http://www.searchupc.com/handlers/"),
    WALMART("walmart", "http://api.walmartlabs.com/v1/"),
    AMAZON("amazon", "http://" + AmazonSignedRequestsHelper.ENDPOINT + AmazonSignedRequestsHelper.REQUEST_URI),
    UPC_ITEM_DB("upcitemdb", "https://api.upcitemdb.com/"),
    UPC_DATABASE("upcdatabase", "http://api.upcdatabase.org/"),
    IN_APP("babr", "http://www.searchupc.com/handlers/");

    private final String mDisplay;
    private final String mEndpoint;

    ProductSource(String displayName, String endpoint) {
        this.mDisplay = displayName;
        mEndpoint = endpoint;
    }

    public String getDisplay() {
        return mDisplay;
    }

    public String getEndpoint() {
        return mEndpoint;
    }
}
