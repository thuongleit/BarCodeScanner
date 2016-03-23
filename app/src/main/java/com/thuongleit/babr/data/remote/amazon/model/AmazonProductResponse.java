package com.thuongleit.babr.data.remote.amazon.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by thuongle on 2/24/16.
 */
@Root(name = "ItemSearchResponse", strict = false)
public class AmazonProductResponse implements Parcelable {

    @Element(name = "Items", required = false)
    RootItem mRootItem;

    public RootItem getRootItem() {
        return mRootItem;
    }

    public void setRootItem(RootItem rootItem) {
        this.mRootItem = rootItem;
    }

    public List<AmazonSearchProduct> getProducts() {
        return mRootItem.getProducts();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mRootItem, flags);
    }

    public AmazonProductResponse() {
    }

    protected AmazonProductResponse(Parcel in) {
        this.mRootItem = in.readParcelable(RootItem.class.getClassLoader());
    }

    public static final Creator<AmazonProductResponse> CREATOR = new Creator<AmazonProductResponse>() {
        public AmazonProductResponse createFromParcel(Parcel source) {
            return new AmazonProductResponse(source);
        }

        public AmazonProductResponse[] newArray(int size) {
            return new AmazonProductResponse[size];
        }
    };
}
