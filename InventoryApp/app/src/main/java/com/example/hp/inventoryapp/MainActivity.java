package com.example.hp.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.inventoryapp.Data.AddContract.Inventory;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 0;
    AppCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button add = findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdditionActivity.class);
                startActivity(intent);
            }
        });
        ListView mainListView = findViewById(R.id.list);
        TextView emptyView = findViewById(R.id.text_view);
        mainListView.setEmptyView(emptyView);
        mCursorAdapter = new AppCursorAdapter(this, null);
        mainListView.setAdapter(mCursorAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Intent intent = new Intent(MainActivity.this, ViewDetails.class);
                Uri currentProductUri = ContentUris.withAppendedId(Inventory.CONTENT_URI, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
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
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    public void productSaleQuantity(int productID, int quantity_of_product) {
        quantity_of_product += -1;
        if (quantity_of_product >= 0) {
            ContentValues values = new ContentValues();
            values.put(Inventory.COLUMN_QUANTITY, quantity_of_product);
            Uri updateUri = ContentUris.withAppendedId(Inventory.CONTENT_URI, productID);
            int rowsChanged = getContentResolver().update(updateUri, values, null, null);
            Toast.makeText(this, "Quantity Changed", Toast.LENGTH_SHORT).show();

            Log.d("Log msg", "rowsAffected " + rowsChanged + " - productID " + productID + " - quantity " + quantity_of_product + " , quantity decreased by calling decrease count.");
        } else {
            Toast.makeText(this, "Product finished!!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_rows:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.to_delete);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllRows();
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

    private void deleteAllRows() {
        int rowsDeleted = getContentResolver().delete(Inventory.CONTENT_URI, null, null);
        Toast.makeText(this, rowsDeleted + " " + getString(R.string.delete_all), Toast.LENGTH_SHORT).show();

    }
}
