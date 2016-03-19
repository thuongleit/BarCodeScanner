
package com.thuongleit.babr.data.remote.upcitemdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {

    private String ean;
    private String title;
    private String upc;
    private String description;
    private String brand;
    private String model;
    private String dimension;
    private String weight;
    private String currency;
    private Double lowestRecordedPrice;
    private List<String> images = new ArrayList<String>();
    private List<Offer> offers = new ArrayList<Offer>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The ean
     */
    public String getEan() {
        return ean;
    }

    /**
     * 
     * @param ean
     *     The ean
     */
    public void setEan(String ean) {
        this.ean = ean;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The upc
     */
    public String getUpc() {
        return upc;
    }

    /**
     * 
     * @param upc
     *     The upc
     */
    public void setUpc(String upc) {
        this.upc = upc;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 
     * @param brand
     *     The brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 
     * @return
     *     The model
     */
    public String getModel() {
        return model;
    }

    /**
     * 
     * @param model
     *     The model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 
     * @return
     *     The dimension
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * 
     * @param dimension
     *     The dimension
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    /**
     * 
     * @return
     *     The weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * 
     * @param weight
     *     The weight
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }

    /**
     * 
     * @return
     *     The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 
     * @param currency
     *     The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 
     * @return
     *     The lowestRecordedPrice
     */
    public Double getLowestRecordedPrice() {
        return lowestRecordedPrice;
    }

    /**
     * 
     * @param lowestRecordedPrice
     *     The lowest_recorded_price
     */
    public void setLowestRecordedPrice(Double lowestRecordedPrice) {
        this.lowestRecordedPrice = lowestRecordedPrice;
    }

    /**
     * 
     * @return
     *     The images
     */
    public List<String> getImages() {
        return images;
    }

    /**
     * 
     * @param images
     *     The images
     */
    public void setImages(List<String> images) {
        this.images = images;
    }

    /**
     * 
     * @return
     *     The offers
     */
    public List<Offer> getOffers() {
        return offers;
    }

    /**
     * 
     * @param offers
     *     The offers
     */
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
