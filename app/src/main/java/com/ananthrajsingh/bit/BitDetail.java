package com.ananthrajsingh.bit;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.todaysDayOffset;
import static com.ananthrajsingh.bit.utilities.TimeUtils.daysResourceId;
import static com.ananthrajsingh.bit.utilities.TimeUtils.getTodaysDate;

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


        bitCountTextView = (TextView) findViewById(R.id.textView_temp_plusone);
//        bitCountTextView.setText("-1");
        plusOneButton = (Button) findViewById(R.id.button);
        final Uri freqTableUri = buildUriToFreqTableWithDate(idOfHabit);
        Cursor cursor = getContentResolver().query(freqTableUri,
                null,
                null,
                null,
                null);
        if(cursor.getCount() == 0){
            bitCountTextView.setText("0");
            cursor.close();
        }
        else{
            cursor.moveToLast();
            int freq = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY));
            bitCountTextView.setText(Integer.toString(freq));
            cursor.close();
        }


        plusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
         * This will fill dots in the matrix
         */
       showDataInMonthMatrix();
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
            Log.e("BitDetail.java", "We are in BAD_BIT_ID");
            if (frequency == 0) {
                retColor = ContextCompat.getColor(this, R.color.white);
            } else if (frequency > 0 && frequency <= 1) {
                retColor = ContextCompat.getColor(this, R.color.red1);
            } else if (frequency > 1 && frequency <= 3) {
                retColor = ContextCompat.getColor(this, R.color.red2);
            } else if (frequency > 3 && frequency <= 7) {
                retColor = ContextCompat.getColor(this, R.color.red3);
            } else if (frequency > 7 && frequency <= 13) {
                retColor = ContextCompat.getColor(this, R.color.red4);
            } else {
                retColor = ContextCompat.getColor(this, R.color.red5);
            }
        }

        if (habitType == GOOD_BIT_ID) {
            Log.e("BitDetail.java", "We are in GOOD_BIT_ID");
            if (frequency == 0) {
                retColor = ContextCompat.getColor(this, R.color.white);
            } else if (frequency > 0 && frequency <= 1) {
                retColor = ContextCompat.getColor(this, R.color.green1);
            } else if (frequency > 1 && frequency <= 3) {
                retColor = ContextCompat.getColor(this, R.color.green2);
            } else if (frequency > 3 && frequency <= 7) {
                retColor = ContextCompat.getColor(this, R.color.green3);
            } else if (frequency > 7 && frequency <= 13) {
                retColor = ContextCompat.getColor(this, R.color.green4);
            } else {
                retColor = ContextCompat.getColor(this, R.color.green5);
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

    private int getDateDifference(String currentDate, String prevDate){
        /*
        We know that date format is in the form MM-dd-yyyy
         */

        String todaysDate = getTodaysDate();
        int todaysDay  = Integer.parseInt(todaysDate.substring(3, 5));
        int currentDay = Integer.parseInt(currentDate.substring(3, 5));
        int prevDay = Integer.parseInt(prevDate.substring(3, 5));
        Log.e("BitDetail.java", "currentDay " + currentDay + " , prevDay " + prevDay);
        //This is opposite because we are filling circle table starting from bottom of frequency table
        int difference = prevDay - currentDay;
        if (difference == 0){

            /*
             * This case will apply for the first circle to draw, currentDate and prevDate will be same
             * In this case we'll check difference between today's date andthe last entry in frequency
             * table. If this case is not handled, dot filling will start from today's date, even if
             * there are no entry for today or from weeks.
             */

            difference = todaysDay - currentDay;

        }
        return difference;
    }

    private void showDataInMonthMatrix(){

        /*Reference to the table of clicked item */
        Uri uriToFreqTable = buildUriToFreqTable(idOfHabit);
        TextView tableTV;
        String currentDate;
        String prevDate = null;
        Cursor cursor = getCursorForFreqTable(uriToFreqTable);
        int i = cursor.getCount();
        /*
         * This if case will take care of case when new Bit is created and this activity is opened
         * for the first time.
         */
        if (cursor.getCount() != 0) {
            cursor.moveToPosition(i - 1);
            prevDate = cursor.getString(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_DATE));
        }
        int index = todaysDayOffset();
        while(i > 0){
            i--;
            cursor.moveToPosition(i);
            tableTV = (TextView) findViewById(daysResourceId[index]);
            currentDate = cursor.getString(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_DATE));
            index = index + getDateDifference(currentDate, prevDate);
            int freq = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY));
            int color = getColorGradient(freq, bitType);
            tableTV.setBackgroundTintList(ColorStateList.valueOf(color));
            prevDate = currentDate;
//            tableTV.setText(Integer.toString(freq));

        }

    }
}
