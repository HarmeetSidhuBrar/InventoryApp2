package com.sidhupvttechltd.www.inventoryapp2;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sidhupvttechltd.www.inventoryapp2.data.ProductContract;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    FloatingActionButton fab;
    ListView listView;
    private static final int PRODUCT_LOADER = 0;
    ProductCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener((View v) -> {
            Intent in = new Intent(getApplicationContext(), EditorActivity.class);
            startActivity(in);
        });

        listView = findViewById(R.id.list_view);
        View emptyView = findViewById(R.id.emptyView);
        // Set empty view, if the list has no contents
        listView.setEmptyView(emptyView);
        adapter = new ProductCursorAdapter(this, null);
        listView.setAdapter(adapter);
        // start loader in background thread
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent in = new Intent(getApplicationContext(), EditorActivity.class);
                // Launch editor activity on item click
                Uri currentUri = ContentUris.withAppendedId(ProductContract.NewEntry.CONTENT_URI, id);
                // Pass the data to know which item was clicked
                in.setData(currentUri);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all :
                getContentResolver().delete(ProductContract.NewEntry.CONTENT_URI, null, null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Read the data which is necessary
        // so, using projection
        String[] projection = {
                ProductContract.NewEntry._ID,
                ProductContract.NewEntry.COLUMN_PRODUCT_NAME,
                ProductContract.NewEntry.COLUMN_PRICE,
                ProductContract.NewEntry.COLUMN_QUANTITY};
        return new CursorLoader(this,
                ProductContract.NewEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}