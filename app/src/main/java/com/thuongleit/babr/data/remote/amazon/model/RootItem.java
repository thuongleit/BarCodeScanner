package com.thuongleit.babr.data.remote.amazon.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Items", strict = false)
class RootItem {
    @ElementList(required = false, entry = "Item", inline = true, empty = true)
    private List<AmazonSearchProduct> mProducts;

    public List<AmazonSearchProduct> getProducts() {
        return mProducts;
    }

    public void setProducts(List<AmazonSearchProduct> products) {
        this.mProducts = products;
    }
}