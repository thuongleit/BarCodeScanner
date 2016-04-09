package com.jokotech.babr.data.remote.amazon.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by thuongle on 2/24/16.
 */
@Root(name = "Item", strict = false)
public class AmazonSearchProduct implements Parcelable {

    @Element(name = "ASIN")
    String asinNumber;

    @Element(name = "DetailPageURL")
    String detailPageURL;

    public AmazonSearchProduct() {
    }

    protected AmazonSearchProduct(Parcel in) {
        this.asinNumber = in.readString();
        this.detailPageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.asinNumber);
        dest.writeString(this.detailPageURL);
    }

    public String getAsinNumber() {
        return asinNumber;
    }

    public void setAsinNumber(String asinNumber) {
        this.asinNumber = asinNumber;
    }

    public String getDetailPageURL() {
        return detailPageURL;
    }

    public void setDetailPageURL(String detailPageURL) {
        this.detailPageURL = detailPageURL;
    }

    public static final Creator<AmazonSearchProduct> CREATOR = new Creator<AmazonSearchProduct>() {
        public AmazonSearchProduct createFromParcel(Parcel source) {
            return new AmazonSearchProduct(source);
        }

        public AmazonSearchProduct[] newArray(int size) {
            return new AmazonSearchProduct[size];
        }
    };
}
