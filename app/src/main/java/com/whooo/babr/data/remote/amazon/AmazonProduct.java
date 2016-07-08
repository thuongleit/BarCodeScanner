package com.whooo.babr.data.remote.amazon;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Item", strict = false)
class AmazonProduct {

    @Element(name = "ASIN")
    String asinNumber;

    @Element(name = "DetailPageURL")
    String detailPageURL;
}
