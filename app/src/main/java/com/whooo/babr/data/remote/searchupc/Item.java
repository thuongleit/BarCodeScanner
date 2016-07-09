
package com.whooo.babr.data.remote.searchupc;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = ItemDeserializer.class)
public class Item {

    public List<Child> nodes;

    public Item() {
    }

    public Item(List<Child> nodes) {
        this.nodes = nodes;
    }

    public class Child {

        @JsonProperty("productname")
        String productName;

        @JsonProperty("imageurl")
        String imageUrl;

        @JsonProperty("producturl")
        String productUrl;

        @JsonProperty("price")
        String price;

        @JsonProperty("currency")
        String currency;

        @JsonProperty("saleprice")
        String salePrice;

        @JsonProperty("storename")
        String storeName;

        public Child() {
        }
    }
}
