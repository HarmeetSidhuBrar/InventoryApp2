package com.sidhutechpvtltd.www.inventoryapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    // Name of the database file
    public static final String DATABASE_NAME = "inventory.db";

    // version of the database
    // must be changed on change of database schema
    public static final int DATABASE_VERSION = 1;

    // Constructor
    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query which is to be executed.
        String SQL_CREATE_TABLE = "CREATE TABLE " + ProductContract.NewEntry.TABLE_NAME + " ("
                + ProductContract.NewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductContract.NewEntry.COLUMN_PRODUCT_NAME + " TEXT, "
                + ProductContract.NewEntry.COLUMN_PRICE + " INTEGER, "
                + ProductContract.NewEntry.COLUMN_QUANTITY + " INTEGER, "
                + ProductContract.NewEntry.COLUMN_SUPPLIER_NAME + " TEXT, "
                + ProductContract.NewEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT);";

        // Execute the sql statement
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // empty for now
    }
}
