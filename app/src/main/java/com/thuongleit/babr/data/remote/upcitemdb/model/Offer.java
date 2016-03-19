
package com.thuongleit.babr.data.remote.upcitemdb.model;

import java.util.HashMap;
import java.util.Map;

public class Offer {

    private String merchant;
    private String domain;
    private String title;
    private String currency;
    private String listPrice;
    private Double price;
    private String shipping;
    private String condition;
    private String availability;
    private String link;
    private Integer updatedT;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The merchant
     */
    public String getMerchant() {
        return merchant;
    }

    /**
     * @param merchant The merchant
     */
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    /**
     * @return The domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain The domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return The listPrice
     */
    public String getListPrice() {
        return listPrice;
    }

    /**
     * @param listPrice The list_price
     */
    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    /**
     * @return The price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return The shipping
     */
    public String getShipping() {
        return shipping;
    }

    /**
     * @param shipping The shipping
     */
    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    /**
     * @return The condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition The condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * @return The availability
     */
    public String getAvailability() {
        return availability;
    }

    /**
     * @param availability The availability
     */
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    /**
     * @return The link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return The updatedT
     */
    public Integer getUpdatedT() {
        return updatedT;
    }

    /**
     * @param updatedT The updated_t
     */
    public void setUpdatedT(Integer updatedT) {
        this.updatedT = updatedT;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
