package com.ananthrajsingh.bit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;

public class BitDetail extends AppCompatActivity {

    public long idOfHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_detail);
        Intent intent = getIntent();
        idOfHabit = intent.getLongExtra(getString(R.string.item_id_extra), -1);

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
        Uri uri1 = buildUriToMainTable(idOfHabit);
        Log.e("BitDetail", " idOfHabit - " + idOfHabit);
//        String[] selectionArgs = {idOfHabit.toString()};
        if (id == R.id.action_delete){
                Log.e("BitDetail", "OPTION DELETE CLICKED , URI - " + uri1);
                getContentResolver().delete(uri1, null, null);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        //DON'T return true here
        return super.onOptionsItemSelected(item);
    }
}
