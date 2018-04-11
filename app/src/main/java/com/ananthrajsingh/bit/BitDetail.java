package com.ananthrajsingh.bit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ananthrajsingh.bit.data.BitContract;

import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;

public class BitDetail extends AppCompatActivity {

    public Long idOfHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_detail);
        Intent intent = getIntent();
        idOfHabit = intent.getLongExtra(getString(R.string.item_id_extra), 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Uri uri = buildUriToMainTable();
        String[] selectionArgs = {idOfHabit.toString()};
        if (id == R.id.action_delete){
            getContentResolver().delete(uri, BitContract.MainTableEntry._ID + " = ?", selectionArgs);
        }
        return true;
    }
}
