package com.example.barscanner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.barscanner.model.BarCode;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by thuongle on 11/24/15.
 */
public class SQLHelper extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, BarCode.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, BarCode.class, true);
            TableUtils.createTable(connectionSource, BarCode.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao getBarCodeDao() {
        try {
            return getDao(BarCode.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
