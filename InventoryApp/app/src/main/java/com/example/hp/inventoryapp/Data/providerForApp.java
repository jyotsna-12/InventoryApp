package com.example.hp.inventoryapp.Data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.hp.inventoryapp.Data.AddContract.Inventory;

public class providerForApp extends ContentProvider {
    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AddContract.CONTENT_AUTHORITY, AddContract.PATH_INVENTORY, PRODUCTS);
        sUriMatcher.addURI(AddContract.CONTENT_AUTHORITY, AddContract.PATH_INVENTORY + "/#", PRODUCT_ID);
    }

    private addHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new addHelper((getContext()));
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(AddContract.Inventory.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = Inventory._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Inventory.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return Inventory.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return Inventory.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI" + uri + " with match " + match);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        String nameProduct = values.getAsString(Inventory.COLUMN_PRODUCT);
        if (nameProduct == null) {
            throw new IllegalArgumentException("Product name required. Cannot be null");
        }

        Long price = values.getAsLong(Inventory.COLUMN_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Please enter correct price");
        }

        Integer quantity = values.getAsInteger(Inventory.COLUMN_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Please enter correct Quantity");
        }

        String supplierName = values.getAsString(Inventory.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Please enter name of supplier");
        }

        Integer supplierPhone = values.getAsInteger(Inventory.COLUMN_SUPPLIER_PHONE);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException("Please enter valid phone number");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(Inventory.TABLE_NAME, null, values);
        if (id == -1) {
            Log.v("message:", "Failed to insert new row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                rowsDeleted = database.delete(Inventory.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = Inventory._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(Inventory.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[]
            selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = Inventory._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(Inventory.COLUMN_PRODUCT)) {
            String nameProduct = values.getAsString(Inventory.COLUMN_PRODUCT);
            if (nameProduct == null) {
                throw new IllegalArgumentException("Product name required. Cannot be null");
            }
        }
        if (values.containsKey(Inventory.COLUMN_PRICE)) {
            Long price = values.getAsLong(Inventory.COLUMN_PRICE);
            if (price != null && price < 0) {
                throw new
                        IllegalArgumentException("Please enter correct price");
            }
        }

        if (values.containsKey(Inventory.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(Inventory.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new
                        IllegalArgumentException("Please enter correct Quantity");
            }
        }
        if (values.containsKey(Inventory.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(Inventory.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Please enter name of supplier");
            }
        }

        if (values.containsKey(Inventory.COLUMN_SUPPLIER_PHONE)) {
            Integer supplierPhone = values.getAsInteger(Inventory.COLUMN_SUPPLIER_PHONE);
            if (supplierPhone != null && supplierPhone < 0) {
                throw new
                        IllegalArgumentException("Please enter correct phone number");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(Inventory.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}

