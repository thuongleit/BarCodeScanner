package com.thuongleit.babr.data.remote.amazon.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by thuongle on 2/24/16.
 */
@Root(name = "ItemSearchResponse", strict = false)
public class AmazonProductResponse {

    @Element(name = "Items", required = false)
    RootItem mRootItem;

    public RootItem getRootItem() {
        return mRootItem;
    }

    public void setRootItem(RootItem rootItem) {
        this.mRootItem = rootItem;
    }

    public List<AmazonSearchProduct> getProducts() {
        return mRootItem.getProducts();
    }
}
