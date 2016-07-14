package com.whooo.babr.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Cart implements Parcelable {
    public String objectId;
    public String name;
    public String timestamp;

    public Cart() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.timestamp);
    }

    protected Cart(Parcel in) {
        this.objectId = in.readString();
        this.name = in.readString();
        this.timestamp = in.readString();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel source) {
            return new Cart(source);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };
}
