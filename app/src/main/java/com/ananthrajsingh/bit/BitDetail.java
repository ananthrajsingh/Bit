package com.ananthrajsingh.bit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToFreqTable;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToFreqTableWithDate;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;

public class BitDetail extends AppCompatActivity {

    public TextView bitCountTextView;
    public Button plusOneButton;
    public long idOfHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_detail);
        Intent intent = getIntent();
        idOfHabit = intent.getLongExtra(getString(R.string.item_id_extra), -1);
        bitCountTextView = (TextView) findViewById(R.id.textView_temp_plusone);
        plusOneButton = (Button) findViewById(R.id.button);

        plusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri freqTableUri = buildUriToFreqTableWithDate(idOfHabit);
                Log.e("BitDetail, Button click" ,"uri with date as returned by utility fn - " + freqTableUri);
                Cursor cursor = getContentResolver().query(freqTableUri,
                        null,
                        null,
                        null,
                        null);
                if (cursor == null){
                    Uri uri = buildUriToFreqTable(idOfHabit);
                    Uri returnedUri = getContentResolver().insert(uri, null);
                    Log.e("BitDetail.java" , "uri after date row insertion - " + returnedUri);
                    if (returnedUri != null){
                        int numberOfRowsAffected = getContentResolver().update(freqTableUri,
                                null,
                                null,
                                null);
                        Log.e("BitDetail.java", "number of rows updated - " + numberOfRowsAffected);
                    }
                    cursor.close();
                }
            }
        });

    }

    /**
     * This method is called to initialize options menu. This inflates a xml file defined
     * in menu directory.
     *
     * @param menu the menu in which weplace our items
     * @return return true to display the menu. If we return false, menu will not be
     * displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         * MenuInflater is the class that instantiates xml into Menu objects.
         */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        //Since we want to display our menu
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item the menu item that was selected by the user
     * @return true if the item click is handled here, else false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Uri uri1 = buildUriToMainTable(idOfHabit);
        if (id == R.id.action_delete){

                getContentResolver().delete(uri1, null, null);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        //DON'T return true here
        /*
         * This is important, you cannot simply pass true here. There might be more options
         * (especially in tablet versions where every fragment may have its own options) that
         * are not consumed here by us. Returning super.onOptionsItemSelected(..) checks for
         * other fragmnts until the action is consumed.
         */
        return super.onOptionsItemSelected(item);
    }
}
