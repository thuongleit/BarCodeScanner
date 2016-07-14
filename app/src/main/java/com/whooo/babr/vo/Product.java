package com.whooo.babr.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Product implements Parcelable, Comparable, Cloneable {
    public String objectId;
    public String name;
    public String country;
    public String manufacture;
    public String source;
    public String upc;
    public String ean;
    public String imageUrl;
    public String originalUrl;
    public String model;
    public int quantity = 0;
    public String cartId;
    public String userId;

    public Product() {
    }

    public Product(String name, String country, String manufacture, String source, String upc, String ean, String imageUrl, String model, int quantity, String objectId, String userId) {
        this.name = name;
        this.country = country;
        this.manufacture = manufacture;
        this.source = source;
        this.upc = upc;
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
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.country);
        dest.writeString(this.manufacture);
        dest.writeString(this.source);
        dest.writeString(this.upc);
        dest.writeString(this.ean);
        dest.writeString(this.imageUrl);
        dest.writeString(this.originalUrl);
        dest.writeString(this.model);
        dest.writeInt(this.quantity);
        dest.writeString(this.cartId);
        dest.writeString(this.userId);
    }

    protected Product(Parcel in) {
        this.objectId = in.readString();
        this.name = in.readString();
        this.country = in.readString();
        this.manufacture = in.readString();
        this.source = in.readString();
        this.upc = in.readString();
        this.ean = in.readString();
        this.imageUrl = in.readString();
        this.originalUrl = in.readString();
        this.model = in.readString();
        this.quantity = in.readInt();
        this.cartId = in.readString();
        this.userId = in.readString();
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("objectId", objectId);
        result.put("name", name);
        result.put("country", country);
        result.put("manufacture", manufacture);
        result.put("upc", upc);
        result.put("ean", ean);
        result.put("imageUrl", imageUrl);
        result.put("originalUrl", originalUrl);
        result.put("model", model);
        result.put("quantity", quantity);
        result.put("cartId", cartId);
        result.put("userId", userId);

        return result;
    }
}
