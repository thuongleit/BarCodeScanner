
package com.whooo.babr.data.remote.walmartlabs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftOptions {

    @SerializedName("allowGiftWrap")
    @Expose
    private Boolean allowGiftWrap;
    @SerializedName("allowGiftMessage")
    @Expose
    private Boolean allowGiftMessage;
    @SerializedName("allowGiftReceipt")
    @Expose
    private Boolean allowGiftReceipt;

    /**
     * 
     * @return
     *     The allowGiftWrap
     */
    public Boolean getAllowGiftWrap() {
        return allowGiftWrap;
    }

    /**
     * 
     * @param allowGiftWrap
     *     The allowGiftWrap
     */
    public void setAllowGiftWrap(Boolean allowGiftWrap) {
        this.allowGiftWrap = allowGiftWrap;
    }

    /**
     * 
     * @return
     *     The allowGiftMessage
     */
    public Boolean getAllowGiftMessage() {
        return allowGiftMessage;
    }

    /**
     * 
     * @param allowGiftMessage
     *     The allowGiftMessage
     */
    public void setAllowGiftMessage(Boolean allowGiftMessage) {
        this.allowGiftMessage = allowGiftMessage;
    }

    /**
     * 
     * @return
     *     The allowGiftReceipt
     */
    public Boolean getAllowGiftReceipt() {
        return allowGiftReceipt;
    }

    /**
     * 
     * @param allowGiftReceipt
     *     The allowGiftReceipt
     */
    public void setAllowGiftReceipt(Boolean allowGiftReceipt) {
        this.allowGiftReceipt = allowGiftReceipt;
    }

}
