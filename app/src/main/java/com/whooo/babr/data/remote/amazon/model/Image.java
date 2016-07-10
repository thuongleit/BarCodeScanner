package com.whooo.babr.data.remote.amazon.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "LargeImage", strict = false)
public class Image {

    @Element(name = "URL")
    public String url;
}