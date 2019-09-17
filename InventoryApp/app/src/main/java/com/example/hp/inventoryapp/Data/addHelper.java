package com.example.hp.inventoryapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.hp.inventoryapp.Data.AddContract.Inventory;

public class addHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;
    public addHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + Inventory.TABLE_NAME + " ("
                + Inventory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Inventory.COLUMN_PRODUCT + " TEXT NOT NULL, "
                + Inventory.COLUMN_PRICE + " LONG NOT NULL, "
                + Inventory.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + Inventory.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL , "
                + Inventory.COLUMN_SUPPLIER_PHONE + " INTEGER );";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
        Log.d("successfull message", "created table of db");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Inventory.TABLE_NAME);
        }
}

