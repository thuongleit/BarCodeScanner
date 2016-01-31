package com.whooo.barscanner.data.local;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by thuongle on 1/30/16.
 */
@Database(name = BarDatabase.DATABASE_NAME, version = BarDatabase.DATABASE_VERSION)
public class BarDatabase {

    public static final String DATABASE_NAME = "bardb";

    public static final int DATABASE_VERSION = 1;
}
