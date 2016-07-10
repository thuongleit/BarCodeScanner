package com.whooo.babr.data.remote.amazon.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ItemSearchResponse", strict = false)
public class AmazonApisResponse {

    @Element(name = "Items", required = false)
    public RootItem rootItem;
}
