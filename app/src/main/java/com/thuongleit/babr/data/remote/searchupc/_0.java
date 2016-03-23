
package com.thuongleit.babr.data.remote.searchupc;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class _0 {

    @SerializedName("productname")
    @Expose
    private String productname;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("producturl")
    @Expose
    private String producturl;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("saleprice")
    @Expose
    private String saleprice;
    @SerializedName("storename")
    @Expose
    private String storename;

    /**
     * 
     * @return
     *     The productname
     */
    public String getProductname() {
        return productname;
    }

    /**
     * 
     * @param productname
     *     The productname
     */
    public void setProductname(String productname) {
        this.productname = productname;
    }

    /**
     * 
     * @return
     *     The imageurl
     */
    public String getImageurl() {
        return imageurl;
    }

    /**
     * 
     * @param imageurl
     *     The imageurl
     */
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    /**
     * 
     * @return
     *     The producturl
     */
    public String getProducturl() {
        return producturl;
    }

    /**
     * 
     * @param producturl
     *     The producturl
     */
    public void setProducturl(String producturl) {
        this.producturl = producturl;
    }

    /**
     * 
     * @return
     *     The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * 
     * @param price
     *     The price
     */
    public void setPrice(String price) {
        this.price = price;
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
     *     The saleprice
     */
    public String getSaleprice() {
        return saleprice;
    }

    /**
     * 
     * @param saleprice
     *     The saleprice
     */
    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    /**
     * 
     * @return
     *     The storename
     */
    public String getStorename() {
        return storename;
    }

    /**
     * 
     * @param storename
     *     The storename
     */
    public void setStorename(String storename) {
        this.storename = storename;
    }

}
