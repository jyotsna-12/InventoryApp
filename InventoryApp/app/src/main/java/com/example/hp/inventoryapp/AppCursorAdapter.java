package com.example.hp.inventoryapp;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.hp.inventoryapp.Data.AddContract.Inventory;


public class AppCursorAdapter extends CursorAdapter {

    AppCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.activity_viewing_page, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        Log.d("Position " + cursor.getPosition() + ":", " bindView() called.");
        TextView productNameText = view.findViewById(R.id.product_name);
        TextView productPriceText = view.findViewById(R.id.product_price);
        TextView productQuantityText= view.findViewById(R.id.product_quantity);
        Button saleButtonText = view.findViewById(R.id.button_for_sale_feature);

        final int columnIdIndex = cursor.getColumnIndex(Inventory._ID);
        int productNameColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_PRODUCT);
        int productPriceColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_PRICE);
        int productQuantityColumnIndex = cursor.getColumnIndex(Inventory.COLUMN_QUANTITY);

        final String productID = cursor.getString(columnIdIndex);
        String productName = cursor.getString(productNameColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);
        final String productQuantity = cursor.getString(productQuantityColumnIndex);
        saleButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity Activity = (MainActivity) context;
                Activity.productSaleQuantity(Integer.valueOf(productID), Integer.valueOf(productQuantity));
            }
        });
        Button buttonForEditFeature = view.findViewById(R.id.edit_button);
        buttonForEditFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ViewDetails.class);
                Uri currentProductUri = ContentUris.withAppendedId(Inventory.CONTENT_URI, Long.parseLong(productID));
                intent.setData(currentProductUri);
                context.startActivity(intent);
            }
        });
        productNameText.setText(productID + ")" +" " + productName);
        productPriceText.setText(context.getString(R.string.price) + " : "+" "+ productPrice + " " + context.getString(R.string.currency));
        productQuantityText.setText(context.getString(R.string.quantity) + " : " + productQuantity);
        }
}
