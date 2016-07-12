package com.whooo.babr.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product implements Parcelable, Comparable {
    public String name;
    public String country;
    public String manufacture;
    public String source;
    public String upcA;
    public String ean;
    public String imageUrl;
    public String model;
    public int quantity = 0;
    public String objectId;
    public String listId;
    public String userId;

    public Product(String name, String country, String manufacture, String source, String upcA, String ean, String imageUrl, String model, int quantity, String objectId, String userId) {
        this.name = name;
        this.country = country;
        this.manufacture = manufacture;
        this.source = source;
        this.upcA = upcA;
        this.ean = ean;
        this.imageUrl = imageUrl;
        this.model = model;
        this.quantity = quantity;
        this.userId = userId;
        this.objectId = objectId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.country);
        dest.writeString(this.manufacture);
        dest.writeString(this.source);
        dest.writeString(this.upcA);
        dest.writeString(this.ean);
        dest.writeString(this.imageUrl);
        dest.writeString(this.model);
        dest.writeInt(this.quantity);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.name = in.readString();
        this.country = in.readString();
        this.manufacture = in.readString();
        this.source = in.readString();
        this.upcA = in.readString();
        this.ean = in.readString();
        this.imageUrl = in.readString();
        this.model = in.readString();
        this.quantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int compareTo(Object another) {
        if (another instanceof Product) {
            Product that = (Product) another;
            return this.name.compareTo(that.name);
        }
        return 0;
    }
}
