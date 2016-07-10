package com.whooo.babr.data.remote.amazon.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ItemAttributes", strict = false)
public class Attributes {

    @Element(name = "Manufacturer", required = false)
    public String manufacturer;

    @Element(name = "ProductGroup", required = false)
    public String productGroup;

    @Element(name = "Title", required = false)
    public String title;

    @Element(name = "UPC", required = false)
    public String upc;
}
