package com.whooo.babr.data.remote.amazon.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Item", strict = false)
public class AmazonProduct {

    @Element(name = "ASIN", required = false)
    public String asinNumber;

    @Element(name = "DetailPageURL", required = false)
    public String detailPageURL;

    @Element(name = "LargeImage", required = false)
    public Image image;

    @Element(name = "ItemAttributes", required = false)
    public Attributes attributes;
}