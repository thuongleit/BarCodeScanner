package com.whooo.babr.data.remote.amazon;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "ItemSearchResponse", strict = false)
class AmazonApisResponse {

    @Element(name = "Items", required = false)
     RootItem rootItem;

    @Root(name = "Items", strict = false)
    class RootItem {
        @ElementList(required = false, entry = "Item", inline = true, empty = true)
        List<AmazonProduct> products = new ArrayList<>();
    }
}
