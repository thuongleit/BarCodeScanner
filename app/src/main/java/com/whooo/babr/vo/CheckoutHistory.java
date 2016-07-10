package com.whooo.babr.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckoutHistory implements Parcelable {
    public long id;
    public String name;
    public String listId;
    public long size;

    public CheckoutHistory() {
    }

    @Override
    public String toString() {
        StringBuffer info = new StringBuffer();

        info
                .append(name).append("\n")
                .append(listId).append("\n");
        return info.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.listId);
        dest.writeLong(this.size);
    }


    public CheckoutHistory(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.listId = in.readString();
        this.size = in.readLong();
    }

    public static final Parcelable.Creator<CheckoutHistory> CREATOR = new Parcelable.Creator<CheckoutHistory>() {
        public CheckoutHistory createFromParcel(Parcel source) {
            return new CheckoutHistory(source);
        }

        public CheckoutHistory[] newArray(int size) {
            return new CheckoutHistory[size];
        }
    };
}
