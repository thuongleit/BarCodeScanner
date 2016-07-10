
package com.whooo.babr.data.remote.upcitemdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = ItemDeserializer.class)
class Item {

    public List<Child> nodes;

    public Item() {
    }

    public Item(List<Child> nodes) {
        this.nodes = nodes;
    }

    class Child {
        @JsonProperty
        String ean;

        @JsonProperty
        String upc;

        @JsonProperty
        String title;

        @JsonProperty
        String description;

        @JsonProperty
        String brand;

        @JsonProperty
        String model;

        @JsonProperty
        String dimension;

        @JsonProperty
        String weight;

        @JsonProperty
        String currency;

        @JsonProperty
        List<String> images = new ArrayList<String>();

        public Child() {
        }
    }
}

