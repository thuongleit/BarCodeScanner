
package com.whooo.babr.data.remote.walmartlabs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

class Item {

    @JsonProperty("items")
    List<ChildNode> items = new ArrayList<>();

    class ChildNode {

        @JsonProperty("itemId")
        long itemId;

        @JsonProperty("name")
        String name;

        @JsonProperty("salePrice")
        Double salePrice;

        @JsonProperty("upc")
        String upc;

        @JsonProperty("shortDescription")
        String description;

        @JsonProperty("brandName")
        String brandName;

        @JsonProperty("thumbnailImage")
        String thumbnailImage;

        @JsonProperty("mediumImage")
        String mediumImage;

        @JsonProperty("largeImage")
        String largeImage;

        @JsonProperty("productTrackingUrl")
        String productTrackingUrl;

        @JsonProperty("productUrl")
        String productUrl;
    }
}
