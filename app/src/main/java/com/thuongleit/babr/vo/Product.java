package com.thuongleit.babr.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.thuongleit.babr.data.local.BarDatabase;

/**
 * Created by thuongle on 11/23/15.
 */
@Table(database = BarDatabase.class)
public class Product extends BaseModel implements Parcelable {
    @Column
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    String imageUrl;
    @Column
    String name;
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
    String objectId;
    @Column
    int quantity = 0;
    @Column
    String source;

    boolean isChecked=false;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.imageUrl);
        dest.writeString(this.name);
        dest.writeString(this.upcA);
        dest.writeString(this.ean);
        dest.writeString(this.country);
        dest.writeString(this.manufacture);
        dest.writeString(this.model);
        dest.writeString(this.objectId);
        dest.writeString(this.source);
        dest.writeInt(this.quantity);

    }



    protected Product(Parcel in) {
        this.id = in.readLong();
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.upcA = in.readString();
        this.ean = in.readString();
        this.country = in.readString();
        this.manufacture = in.readString();
        this.model = in.readString();
        this.objectId = in.readString();
        this.source = in.readString();
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
