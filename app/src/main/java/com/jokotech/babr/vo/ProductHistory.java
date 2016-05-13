package com.jokotech.babr.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.jokotech.babr.data.local.BarDatabase;

/**
 * Created by ThongLe on 3/31/2016.
 */
@Table(database = BarDatabase.class)
public class ProductHistory extends BaseModel implements Parcelable {
    @Column
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    String name;
    @Column
    String listId;
    @Column
    long size;

    public ProductHistory() {
    }

    @Override
    public String toString() {
        StringBuffer info = new StringBuffer();

        info
                .append(name).append("\n")
                .append(listId).append("\n");
        return info.toString();
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public ProductHistory(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.listId = in.readString();
        this.size=in.readLong();
    }

    public static final Parcelable.Creator<ProductHistory> CREATOR = new Parcelable.Creator<ProductHistory>() {
        public ProductHistory createFromParcel(Parcel source) {
            return new ProductHistory(source);
        }

        public ProductHistory[] newArray(int size) {
            return new ProductHistory[size];
        }
    };
}
