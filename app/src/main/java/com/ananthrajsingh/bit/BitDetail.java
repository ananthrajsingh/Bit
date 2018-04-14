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

import com.ananthrajsingh.bit.data.BitContract;

import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToFreqTable;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToFreqTableWithDate;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;

public class BitDetail extends AppCompatActivity {

//    public TextView bitCountTextView;
    public Button plusOneButton;
    public TextView bitCountTextView;
    public long idOfHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_detail);
        Intent intent = getIntent();
        idOfHabit = intent.getLongExtra(getString(R.string.item_id_extra), -1);
        final TextView bitCountTextView = (TextView) findViewById(R.id.textView_temp_plusone);
        bitCountTextView.setText("-1");
        plusOneButton = (Button) findViewById(R.id.button);

        plusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri freqTableUri = buildUriToFreqTableWithDate(idOfHabit);
                Log.e("BitDetail.java" ,"Uri to frequency table - " + freqTableUri);
                Cursor cursor = getContentResolver().query(freqTableUri,
                        null,
                        null,
                        null,
                        null);
                cursor.moveToLast();
                Log.e("BitDetail.java" , "Cursor of id " + idOfHabit + " has count " +cursor.getCount()
                        + " and position is " + cursor.getPosition());

                if (cursor.getCount() == 0){
                    cursor.close();
                    Uri uri = buildUriToFreqTable(idOfHabit);
                    Uri returnedUri = getContentResolver().insert(uri, null);
                    Log.e("BitDetail.java", "returnedUri after making new row for today- "+ returnedUri);
                    if (returnedUri != null){
                        int numberOfRowsAffected = getContentResolver().update(freqTableUri,
                                null,
                                null,
                                null);
                        Log.e("BitDetail.java", "number of rows updated on incrementation just after creation - "
                                + numberOfRowsAffected);
                        bitCountTextView.setText("1");

                    }

                }
                else{
                    cursor.moveToLast();
                    Log.e("BitDetail.java", "cursor position " + cursor.getPosition() );

                    int freq = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY));
                    Log.e("BitDetail.java" , "todays freq of this habit is - " + freq);
                    bitCountTextView.setText(Integer.toString(freq));
                    int updatedInt = getContentResolver().update(freqTableUri, null, null, null);

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


    private Cursor getCursorForFreqTable(Uri uri){
        return getContentResolver().query(uri,
                null,
                null,
                null,
                null);
    }
}
