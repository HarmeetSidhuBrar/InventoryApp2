package com.example.harmeetsingh.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harmeetsingh.inventoryapp.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {

    ProductCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView productNameTextView = view.findViewById(R.id.product_name);
        TextView productPriceTextView = view.findViewById(R.id.price);
        TextView productQuantityTextView = view.findViewById(R.id.quantity);

        Button sellButton = view.findViewById(R.id.button_sell);

        String productName = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.NewEntry.COLUMN_PRODUCT_NAME));
        String productPrice = "$ " + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.NewEntry.COLUMN_PRICE)));
        final String productQuantity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.NewEntry.COLUMN_QUANTITY)));
        final int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.NewEntry._ID));

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newUri = ContentUris.withAppendedId(ProductContract.NewEntry.CONTENT_URI, rowId);
                if (Integer.parseInt(productQuantity) > 1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ProductContract.NewEntry.COLUMN_QUANTITY, Integer.parseInt(productQuantity) - 1);
                    context.getContentResolver().update(newUri, contentValues, null, null);
                } else {
                    context.getContentResolver().delete(newUri, null, null);
                    Toast.makeText(context, R.string.last_product_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button orderButton = view.findViewById(R.id.button_order);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_DIAL));
            }
        });

        productNameTextView.setText(productName);
        productPriceTextView.setText(String.format("Price : Rs. %s", String.valueOf(productPrice)));
        productQuantityTextView.setText(String.format("Available : %s", String.valueOf(productQuantity)));
    }
}
