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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.inventoryapp.Data.AddContract.Inventory;

public class ViewDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private TextView mProductName;
    private TextView mProductPrice;
    private TextView mProductQuantity;
    private TextView mSupplierName;
    private TextView mSupplierPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mProductName = findViewById(R.id.editproduct);
        mProductPrice = findViewById(R.id.editprice);
        mProductQuantity = findViewById(R.id.editquantity);
        mSupplierName = findViewById(R.id.supplier_edit);
        mSupplierPhoneNumber = findViewById(R.id.editphone);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        if (mCurrentProductUri == null) {
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        Log.d("message", "onCreate ViewDetails file");

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
                mCurrentProductUri,
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
            final int idColumnIndex = cursor.getColumnIndex(Inventory._ID);
            int nameColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_PRODUCT);
            int priceColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_SUPPLIER_PHONE);

            String productName = cursor.getString(nameColumnIndex);
            final int productPrice = cursor.getInt(priceColumnIndex);
            final int productQuantity = cursor.getInt(quantityColumnIndex);
            String productSupplierName = cursor.getString(supplierNameColumnIndex);
            final int productSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);
            mProductName.setText(productName);
            mProductPrice.setText(Long.toString(productPrice));
            mProductQuantity.setText(Integer.toString(productQuantity));
            mSupplierPhoneNumber.setText(Integer.toString(productSupplierPhone));
            mSupplierName.setText(productSupplierName);

            Button productIncreaseButtonFeature = findViewById(R.id.increase_button1);
            productIncreaseButtonFeature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseCountOfProduct(idColumnIndex, productQuantity);
                }
            });
            Button productDecreaseButtonFeature = findViewById(R.id.decrease_button1);
            productDecreaseButtonFeature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseCountOfProduct(idColumnIndex, productQuantity);
                }
            });


            Button callButton = findViewById(R.id.call_button1);
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = String.valueOf(productSupplierPhone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            });
            Button productDeleteButtonFeature = findViewById(R.id.delete);
            productDeleteButtonFeature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });

        }
    }
    public void decreaseCountOfProduct(int productID, int productQuantity) {
        productQuantity -=1;
        if (productQuantity >= 0) {
            updateProductFeature(productQuantity);
            Toast.makeText(this, getString(R.string.quantity_changed), Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");
        } else {
            Toast.makeText(this, getString(R.string.quantity_finish_msg), Toast.LENGTH_SHORT).show();
        }
    }

    public void increaseCountOfProduct(int productID, int productQuantity) {
        productQuantity+= 1;
        if (productQuantity >= 0) {
            updateProductFeature(productQuantity);
            Toast.makeText(this, getString(R.string.quantity_changed), Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , increaseCount has been called.");
        }
    }


    private void updateProductFeature(int productQuantity) {
        Log.d("message", "updateProduct at ViewDetails");

        if (mCurrentProductUri == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(Inventory.COLUMN_QUANTITY, productQuantity);

        if (mCurrentProductUri == null) {
            Uri newUri = getContentResolver().insert(Inventory.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insertion_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insertion_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.updation_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.updation_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteProductFeature() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.deletion_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.deletion_successfull),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Deletion_confirmation);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProductFeature();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
