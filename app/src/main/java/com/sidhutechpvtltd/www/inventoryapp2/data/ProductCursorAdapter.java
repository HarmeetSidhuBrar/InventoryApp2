package com.sidhutechpvtltd.www.inventoryapp2.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sidhutechpvtltd.www.inventoryapp2.R;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        TextView productNameView = view.findViewById(R.id.product_name);
        TextView productPriceView = view.findViewById(R.id.price);
        final TextView productQuantityView = view.findViewById(R.id.quantity);

        int rowID = cursor.getColumnIndex( ProductContract.NewEntry._ID );
        int nameColumnIndex = cursor.getColumnIndex( ProductContract.NewEntry.COLUMN_PRODUCT_NAME );
        int priceColumnIndex = cursor.getColumnIndex( ProductContract.NewEntry.COLUMN_PRICE );
        final int quantityColumnIndex = cursor.getColumnIndex( ProductContract.NewEntry.COLUMN_QUANTITY );

        final int id = cursor.getInt(rowID);
        String productName = cursor.getString(nameColumnIndex);
        int productPrice = cursor.getInt(priceColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);

         productNameView.setText(productName);
        productPriceView.setText(String.format("Price : Rs. %s", String.valueOf(productPrice)));
        productQuantityView.setText(String.format("Available : %s", String.valueOf(productQuantity)));

        Button button = view.findViewById(R.id.button_sell);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductDbHelper dbHelper = new ProductDbHelper(context);
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.NewEntry.COLUMN_QUANTITY));
                Uri currentUri = ContentUris.withAppendedId(ProductContract.NewEntry.CONTENT_URI, id);
                if(quantity > 1){
                    int mQuantitySold = quantity - 1;
                    ContentValues values = new ContentValues();
                    String selection = ProductContract.NewEntry._ID + "=?";
                    String[] selectionArgs = new String[]{String.valueOf(id)};
                    values.put(ProductContract.NewEntry.COLUMN_QUANTITY, mQuantitySold);
                    int rowsAffected = database.update(ProductContract.NewEntry.TABLE_NAME, values, selection, selectionArgs);
                    if (rowsAffected != -1) {
                        productQuantityView.setText(String.format( "Available : %s",Integer.toString(mQuantitySold)));
                    }}
                else{
                    context.getContentResolver().delete(currentUri, null, null);
                    Toast.makeText(context, R.string.last_product_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
