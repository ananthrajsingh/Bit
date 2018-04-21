package com.ananthrajsingh.bit;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ananthrajsingh.bit.data.BitContract;

import static com.ananthrajsingh.bit.MainActivity.BAD_BIT_ID;
import static com.ananthrajsingh.bit.MainActivity.GOOD_BIT_ID;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToFreqTable;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToFreqTableWithDate;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;
import static com.ananthrajsingh.bit.utilities.TimeUtils.colorGreen;
import static com.ananthrajsingh.bit.utilities.TimeUtils.colorRed;
import static com.ananthrajsingh.bit.utilities.TimeUtils.daysResourceId;

public class BitDetail extends AppCompatActivity {

//    public TextView bitCountTextView;
    public Button plusOneButton;
    public TextView bitCountTextView;
    public long idOfHabit;
    public int bitType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_detail);


        /* getting information about which item was clicked */
        Intent intent = getIntent();
        idOfHabit = intent.getLongExtra(getString(R.string.item_id_extra), -1);
        bitType = intent.getIntExtra(getString(R.string.item_type_extra), -1);


        /*Reference to the table of clicked item */
        Uri uriToFreqTable = buildUriToFreqTable(idOfHabit);
        final TextView bitCountTextView = (TextView) findViewById(R.id.textView_temp_plusone);
        bitCountTextView.setText("-1");
        plusOneButton = (Button) findViewById(R.id.button);

        plusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri freqTableUri = buildUriToFreqTableWithDate(idOfHabit);
                Cursor cursor = getContentResolver().query(freqTableUri,
                        null,
                        null,
                        null,
                        null);
                cursor.moveToLast();

                /* Is there today's row? */
                if (cursor.getCount() == 0){
                    cursor.close();
                    addTodaysRow();
                }
                /*Yes, there is row, so just increment the count */
                else{
                    int updatedInt = getContentResolver().update(freqTableUri, null, null, null);
                    cursor.moveToLast();
                    int freq = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY)) + 1;
                    bitCountTextView.setText(Integer.toString(freq));

                }
            }
        });

        /*
        ---------------------------------------------------------------------------------------------------------------
        adding data to table
         */
        TextView tableTV;
        Cursor cursor = getCursorForFreqTable(uriToFreqTable);
        int i = cursor.getCount();
        int index = 0;
        while(i > 0){
            i--;
            cursor.moveToPosition(i);
            tableTV = (TextView) findViewById(daysResourceId[index++]);
            int freq = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY));
            int color = getColorGradient(freq, bitType);
            tableTV.setBackgroundTintList(ColorStateList.valueOf(color));
            tableTV.setText(Integer.toString(freq));

        }

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

    private int getColorGradient(int frequency, int habitType) {
        int retColor = R.color.white;
        if (habitType == BAD_BIT_ID) {
            if (frequency == 0) {
                retColor = R.color.white;
            } else if (frequency > 0 && frequency <= 1) {
                retColor = colorRed[0];
            } else if (frequency > 1 && frequency <= 3) {
                retColor = colorRed[1];
            } else if (frequency > 3 && frequency <= 7) {
                retColor = colorRed[2];
            } else if (frequency > 7 && frequency <= 13) {
                retColor = colorRed[3];
            } else {
                retColor = colorRed[4];
            }
        }

        if (habitType == GOOD_BIT_ID) {
            if (frequency == 0) {
                retColor = R.color.white;
            } else if (frequency > 0 && frequency <= 1) {
                retColor = colorGreen[0];
            } else if (frequency > 1 && frequency <= 3) {
                retColor = colorGreen[1];
            } else if (frequency > 3 && frequency <= 7) {
                retColor = colorGreen[2];
            } else if (frequency > 7 && frequency <= 13) {
                retColor = colorGreen[3];
            } else {
                retColor = colorGreen[4];
            }
        }
        return retColor;
    }

    private void addTodaysRow(){

        Uri freqTableUri = buildUriToFreqTableWithDate(idOfHabit);
        Uri uri = buildUriToFreqTable(idOfHabit);

                    /* This will insert a new row for today's entry, since we figured out that there is no today's row */
        Uri returnedUri = getContentResolver().insert(uri, null);
        if (returnedUri != null){
            int numberOfRowsAffected = getContentResolver().update(freqTableUri,
                    null,
                    null,
                    null);

            bitCountTextView.setText("1");

        }
    }
}
