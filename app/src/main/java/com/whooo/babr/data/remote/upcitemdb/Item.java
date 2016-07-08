
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

        public Child(String ean, String upc, String title, String description, String brand, String model, String dimension, String weight, String currency, List<String> images) {
            this.ean = ean;
            this.upc = upc;
            this.title = title;
            this.description = description;
            this.brand = brand;
            this.model = model;
            this.dimension = dimension;
            this.weight = weight;
            this.currency = currency;
            this.images = images;
        }
    }
}

