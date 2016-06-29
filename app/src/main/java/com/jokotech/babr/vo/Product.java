package com.jokotech.babr.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thuongle on 11/23/15.
 */
public class Product implements Parcelable {
    public String imageUrl;
    public String name;
    public String upcA;
    public String ean;
    public String country;
    public String manufacture;
    public String model;
    public String objectId;
    public int quantity = 0;
    public String source;
    public String listId;

    public Product() {
    }

    @Override
    public String toString() {
        StringBuffer info = new StringBuffer();

        info
                .append(upcA).append("\n")
                .append(ean).append("\n")
                .append(country).append("\n")
                .append(manufacture).append("\n")
                .append(model).append("\n");
        return info.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.name);
        dest.writeString(this.upcA);
        dest.writeString(this.ean);
        dest.writeString(this.country);
        dest.writeString(this.manufacture);
        dest.writeString(this.model);
        dest.writeString(this.objectId);
        dest.writeString(this.source);
        dest.writeString(this.listId);
        dest.writeInt(this.quantity);

    }

    protected Product(Parcel in) {
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.upcA = in.readString();
        this.ean = in.readString();
        this.country = in.readString();
        this.manufacture = in.readString();
        this.model = in.readString();
        this.objectId = in.readString();
        this.source = in.readString();
        this.listId = in.readString();
        this.quantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
