
package com.whooo.babr.data.remote.walmartlabs.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Walmartlabs {

    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<Item>();

    @SerializedName("errors")
    @Expose
    private List<Error> errors = new ArrayList<Error>();

    /**
     * 
     * @return
     *     The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
