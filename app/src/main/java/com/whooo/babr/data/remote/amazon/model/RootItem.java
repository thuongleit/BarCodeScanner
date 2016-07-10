package com.whooo.babr.data.remote.amazon.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "Items", strict = false)
public class RootItem {
    @ElementList(required = false, entry = "Item", inline = true, empty = true)
    public List<AmazonProduct> products = new ArrayList<>();
}