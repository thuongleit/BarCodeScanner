package com.jokotech.babr.data.remote.amazon.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Items", strict = false)
class RootItem implements Parcelable{
    @ElementList(required = false, entry = "Item", inline = true, empty = true)
    private List<AmazonSearchProduct> mProducts;

    public List<AmazonSearchProduct> getProducts() {
        return mProducts;
    }

    public void setProducts(List<AmazonSearchProduct> products) {
        this.mProducts = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mProducts);
    }

    public RootItem() {
    }

    protected RootItem(Parcel in) {
        this.mProducts = in.createTypedArrayList(AmazonSearchProduct.CREATOR);
    }

    public static final Creator<RootItem> CREATOR = new Creator<RootItem>() {
        public RootItem createFromParcel(Parcel source) {
            return new RootItem(source);
        }

        public RootItem[] newArray(int size) {
            return new RootItem[size];
        }
    };
}