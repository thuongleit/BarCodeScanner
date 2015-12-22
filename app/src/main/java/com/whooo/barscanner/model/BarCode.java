package com.whooo.barscanner.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.parceler.Parcel;

/**
 * Created by thuongle on 11/23/15.
 */
@Parcel
@DatabaseTable
public class BarCode {
    @DatabaseField(generatedId = true) public long id;
    @DatabaseField public String image;
    @DatabaseField public String upcA;
    @DatabaseField public String ean;
    @DatabaseField public String country;
    @DatabaseField public String manufacture;
    @DatabaseField public String model;
    @DatabaseField public int quantity;

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
}
