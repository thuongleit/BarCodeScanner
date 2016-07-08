
package com.whooo.babr.data.remote.upcdatabase;

import com.fasterxml.jackson.annotation.JsonProperty;

class Item {

    @JsonProperty
    boolean valid;

    @JsonProperty("number")
    String upc;

    @JsonProperty("itemname")
    String name;

    @JsonProperty("alias")
    String alias;

    @JsonProperty()
    String description;

    @JsonProperty("avgPrice")
    String avgPrice;

}
