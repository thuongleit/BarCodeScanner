
package com.whooo.babr.data.remote.walmartlabs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageEntity {

    @SerializedName("thumbnailImage")
    @Expose
    private String thumbnailImage;
    @SerializedName("mediumImage")
    @Expose
    private String mediumImage;
    @SerializedName("largeImage")
    @Expose
    private String largeImage;
    @SerializedName("entityType")
    @Expose
    private String entityType;

    /**
     * 
     * @return
     *     The thumbnailImage
     */
    public String getThumbnailImage() {
        return thumbnailImage;
    }

    /**
     * 
     * @param thumbnailImage
     *     The thumbnailImage
     */
    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    /**
     * 
     * @return
     *     The mediumImage
     */
    public String getMediumImage() {
        return mediumImage;
    }

    /**
     * 
     * @param mediumImage
     *     The mediumImage
     */
    public void setMediumImage(String mediumImage) {
        this.mediumImage = mediumImage;
    }

    /**
     * 
     * @return
     *     The largeImage
     */
    public String getLargeImage() {
        return largeImage;
    }

    /**
     * 
     * @param largeImage
     *     The largeImage
     */
    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    /**
     * 
     * @return
     *     The entityType
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * 
     * @param entityType
     *     The entityType
     */
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

}
