
package com.whooo.babr.data.remote.walmartlabs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

class Item {

    @JsonProperty("items")
    List<ChildNode> items = new ArrayList<>();
}
