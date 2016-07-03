
package com.whooo.babr.data.remote.upcdatabase;

public class UpcDatabase {

    private String valid;
    private String number;
    private String itemname;
    private String alias;
    private String description;
    private String avgPrice;
    private Integer rateUp;
    private Integer rateDown;

    /**
     * 
     * @return
     *     The valid
     */
    public String getValid() {
        return valid;
    }

    /**
     * 
     * @param valid
     *     The valid
     */
    public void setValid(String valid) {
        this.valid = valid;
    }

    /**
     * 
     * @return
     *     The number
     */
    public String getNumber() {
        return number;
    }

    /**
     * 
     * @param number
     *     The number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 
     * @return
     *     The itemname
     */
    public String getItemname() {
        return itemname;
    }

    /**
     * 
     * @param itemname
     *     The itemname
     */
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    /**
     * 
     * @return
     *     The alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 
     * @param alias
     *     The alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
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
     *     The avgPrice
     */
    public String getAvgPrice() {
        return avgPrice;
    }

    /**
     * 
     * @param avgPrice
     *     The avg_price
     */
    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    /**
     * 
     * @return
     *     The rateUp
     */
    public Integer getRateUp() {
        return rateUp;
    }

    /**
     * 
     * @param rateUp
     *     The rate_up
     */
    public void setRateUp(Integer rateUp) {
        this.rateUp = rateUp;
    }

    /**
     * 
     * @return
     *     The rateDown
     */
    public Integer getRateDown() {
        return rateDown;
    }

    /**
     * 
     * @param rateDown
     *     The rate_down
     */
    public void setRateDown(Integer rateDown) {
        this.rateDown = rateDown;
    }

}
