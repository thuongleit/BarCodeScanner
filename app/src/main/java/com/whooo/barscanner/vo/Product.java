package com.whooo.barscanner.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.whooo.barscanner.data.local.BarDatabase;

/**
 * Created by thuongle on 11/23/15.
 */
@Table(database = BarDatabase.class)
public class Product extends BaseModel implements Parcelable {
    @Column
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    String image;
    @Column
    String upcA;
    @Column
    String ean;
    @Column
    String country;
    @Column
    String manufacture;
    @Column
    String model;
    @Column
    int quantity = 0;

    public Product() {
    }

    protected Product(Parcel in) {
        this.id = in.readLong();
        this.image = in.readString();
        this.upcA = in.readString();
        this.ean = in.readString();
        this.country = in.readString();
        this.manufacture = in.readString();
        this.model = in.readString();
        this.quantity = in.readInt();
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
        dest.writeLong(this.id);
        dest.writeString(this.image);
        dest.writeString(this.upcA);
        dest.writeString(this.ean);
        dest.writeString(this.country);
        dest.writeString(this.manufacture);
        dest.writeString(this.model);
        dest.writeInt(this.quantity);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpcA() {
        return upcA;
    }

    public void setUpcA(String upcA) {
        this.upcA = upcA;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
