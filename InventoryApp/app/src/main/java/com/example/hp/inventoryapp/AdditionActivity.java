package com.example.hp.inventoryapp;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.inventoryapp.Data.AddContract.Inventory;

public class AdditionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductQuantity;
    private EditText mSupplierName;
    private EditText mSupplierPhoneNumber;

    private boolean mProductHasChanged = false;

    private View.OnTouchListener clickTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            Log.d("message", "onTouch");

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Log.d("message", "onCreate");

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.add));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.saved));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        mProductName = findViewById(R.id.product);
        mProductPrice = findViewById(R.id.edit_price);
        mProductQuantity = findViewById(R.id.edit_quantity);
        mSupplierName = findViewById(R.id.edit_supplier);
        mSupplierPhoneNumber = findViewById(R.id.phone_number);

        mProductName.setOnTouchListener(clickTouchListener);
        mProductPrice.setOnTouchListener(clickTouchListener);
        mProductQuantity.setOnTouchListener(clickTouchListener);
        mSupplierName.setOnTouchListener(clickTouchListener);
        mSupplierPhoneNumber.setOnTouchListener(clickTouchListener);

    }

    private void saveProduct() {
        String productNameString = mProductName.getText().toString().trim();
        String productSuppliername = mSupplierName.getText().toString().trim();
        String productPriceString = mProductPrice.getText().toString().trim();
        String productQuantityString = mProductQuantity.getText().toString().trim();
        String productSupplierPhoneNumberString = mSupplierPhoneNumber.getText().toString().trim();
        if (mCurrentProductUri == null) {
            if (TextUtils.isEmpty(productNameString)) {
                Toast.makeText(this, getString(R.string.Name_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPriceString)) {
                Toast.makeText(this, getString(R.string.price_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSuppliername)) {
                Toast.makeText(this, getString(R.string.supplier_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(Inventory.COLUMN_PRODUCT, productNameString);
            values.put(Inventory.COLUMN_PRICE, productPriceString);
            values.put(Inventory.COLUMN_QUANTITY, productQuantityString);
            values.put(Inventory.COLUMN_SUPPLIER_NAME, productSuppliername);
            values.put(Inventory.COLUMN_SUPPLIER_PHONE, productSupplierPhoneNumberString);

            Uri newUri = getContentResolver().insert(Inventory.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insertion_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insertion_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {

            if (TextUtils.isEmpty(productNameString)) {
                Toast.makeText(this, getString(R.string.Name_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPriceString)) {
                Toast.makeText(this, getString(R.string.price_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSuppliername)) {
                Toast.makeText(this, getString(R.string.supplier_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(Inventory.COLUMN_PRODUCT, productNameString);
            values.put(Inventory.COLUMN_PRICE, productPriceString);
            values.put(Inventory.COLUMN_QUANTITY, productQuantityString);
            values.put(Inventory.COLUMN_SUPPLIER_NAME, productSuppliername);
            values.put(Inventory.COLUMN_SUPPLIER_PHONE, productSupplierPhoneNumberString);


            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.updation_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.updation_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        Log.d("message", "open Addition Activity");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(AdditionActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(AdditionActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                Inventory._ID,
                Inventory.COLUMN_PRODUCT,
                Inventory.COLUMN_PRICE,
                Inventory.COLUMN_QUANTITY,
                Inventory.COLUMN_SUPPLIER_NAME,
                Inventory.COLUMN_SUPPLIER_PHONE
        };

        return new CursorLoader(this,
                Inventory.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_PRODUCT);
            int priceColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_SUPPLIER_PHONE);

            String currentName = cursor.getString(nameColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            String currentSupplierName = cursor.getString(supplierNameColumnIndex);
            int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            mProductName.setText(currentName);
            mProductPrice.setText(Long.toString(currentPrice));
            mProductQuantity.setText(Integer.toString(currentQuantity));
            mSupplierPhoneNumber.setText(Integer.toString(currentSupplierPhone));
            mSupplierName.setText(currentSupplierName);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductName.setText("");
        mProductPrice.setText("");
        mProductQuantity.setText("");
        mSupplierPhoneNumber.setText("");
        mSupplierName.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.discard_changes, discardButtonClickListener);
        builder.setNegativeButton(R.string.editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}