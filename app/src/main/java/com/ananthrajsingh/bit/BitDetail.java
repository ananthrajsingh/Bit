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
import static com.ananthrajsingh.bit.utilities.TimeUtils.daysResourceId;
import static com.ananthrajsingh.bit.utilities.TimeUtils.getTodaysDate;
import static com.ananthrajsingh.bit.utilities.TimeUtils.todaysDayOffset;
import static com.ananthrajsingh.bit.utilities.TimeUtils.weeksdaysResourceId;

public class BitDetail extends AppCompatActivity {

//    public TextView bitCountTextView;
    public Button plusOneButton;
    public TextView bitCountTextView;
    public long idOfHabit;
    public int maxFrequency;
    public int bitType;
    public String habitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_detail);


        /* getting information about which item was clicked */
        Intent intent = getIntent();
        idOfHabit = intent.getLongExtra(getString(R.string.item_id_extra), -1);
        bitType = intent.getIntExtra(getString(R.string.item_type_extra), -1);
        maxFrequency = intent.getIntExtra(getString(R.string.max_frequency_extra), 1);
        habitName = intent.getStringExtra(getString(R.string.name_extra));

        /* Changing the name of ActionBar for the clicked habit */
        getSupportActionBar().setTitle(habitName);

        bitCountTextView = (TextView) findViewById(R.id.textView_temp_plusone);
        plusOneButton = (Button) findViewById(R.id.button);
        if (bitType == GOOD_BIT_ID){
            plusOneButton.setBackground(getDrawable(R.drawable.button_plus_one_good));
        }
        else {
            plusOneButton.setBackground(getDrawable(R.drawable.button_plus_one_bad));
        }

        /* This will set 0 if there is no today's row, else will show today's count */
        final Uri freqTableUri = buildUriToFreqTableWithDate(idOfHabit);
        setInitialCount(freqTableUri);



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
                /* This will update matrix after every click */
                showDataInMonthMatrix();
            }
        });
        /*
         * This will fill dots in the matrix on start up of the activity
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
            if (frequency >= maxFrequency){
                retColor = ContextCompat.getColor(this, R.color.red5);
            }
            else if (frequency >= (int) ((4*maxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.red4);
            }
            else if (frequency >= (int) ((3*maxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.red3);
            }
            else if (frequency >= (int) ((2*maxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.red2);
            }
            else if (frequency >= (int) (maxFrequency/5)){
                retColor = ContextCompat.getColor(this, R.color.red1);
            }
            else{
                retColor = ContextCompat.getColor(this, R.color.white);
            }
        }
        if (habitType == GOOD_BIT_ID){
            if (frequency >= maxFrequency){
                retColor = ContextCompat.getColor(this, R.color.green5);
            }
            else if (frequency >= (int) ((4*maxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.green4);
            }
            else if (frequency >= (int) ((3*maxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.green3);
            }
            else if (frequency >= (int) ((2*maxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.green2);
            }
            else if (frequency >= (int) (maxFrequency/5)){
                retColor = ContextCompat.getColor(this, R.color.green1);
            }
            else{
                retColor = ContextCompat.getColor(this, R.color.white);
            }
        }


        return retColor;
    }

    private void addTodaysRow(){

        Uri freqTableUri = buildUriToFreqTableWithDate(idOfHabit);
        Uri uri = buildUriToFreqTable(idOfHabit);

        /* This will insert a new row for today's entry, since we figured out that there is no today's row */
        Uri returnedUri = getContentResolver().insert(uri, null);
        Log.e("BitDetail.java", "addTodaysRow: We entered today's row, returned uri - " + returnedUri);
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
        Log.e("BitDetail.java", "getDateDifference : currentDate - "+currentDate+" prevDate - "+prevDate+" todaysDate - "+todaysDate);
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

    /**
     * First, this function arranges the weekday titles starting from today's day at index 0.
     * Then we come to the bit matrix, bit matrix is inflated by this function.
     * We are calling this in the onCreate as we want this to lay as soon as activity is opened.
     */
    private void showDataInMonthMatrix(){

        /*
         * This will make weekday names M, Th, W etc to slide in sync with the bits.
         * Bits will slide one right everyday, so we need to slide day titile too.
         */
        String[] weekdayNames = this.getResources().getStringArray(R.array.days_array);
        int indexOfDays = 7 - (todaysDayOffset() + 1);
        for (int i = 0 ; i < 7 ; i++){
            TextView weekdayTv = (TextView) findViewById(weeksdaysResourceId[i]);
            weekdayTv.setText(weekdayNames[indexOfDays++]);
            if (indexOfDays == 7){
                indexOfDays = 0;
            }
        }

        /*Reference to the table of clicked item */
        Uri uriToFreqTable = buildUriToFreqTable(idOfHabit);
        TextView tableTV;
        String currentDate;
        String prevDate = null;
        Cursor cursor = getCursorForFreqTable(uriToFreqTable);
        int numberOfBitsLeft = cursor.getCount();
        /*
         * This if case will take care of case when new Bit is created and this activity is opened
         * for the first time. This will initialize prevDate so that it is not null
         */
        if (numberOfBitsLeft != 0) {
            cursor.moveToPosition(numberOfBitsLeft - 1);
            prevDate = cursor.getString(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_DATE));
        }

        /*
        -------------------------------------------------------------------------------------------------
         * TODO
         * THERE IS A BUG!
         * In settings when month is changed, this function assumes those dates as dates of this
         * month. That can be corrected, but that is for another day.
         *
         * String todaysDate = getTodaysDate();
         * int todaysMonth = Integer.parseInt(todaysDate.substring(0, 2));
         * int prevDateMonth = Integer.parseInt(prevDate.substring(0, 2));
         */



        int index = 0;
        /*
         * THis while loop will rum until all the entries in frequency table exhausts. This will lay
         * information to bits one by one (leaving out the ones with no data using increment in 'index'
         * Color to be shown is generated by getColorGradient(..)
         */
        while(numberOfBitsLeft > 0){

            numberOfBitsLeft--;
            cursor.moveToPosition(numberOfBitsLeft);
            currentDate = cursor.getString(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_DATE));
            index = index + getDateDifference(currentDate, prevDate);
            tableTV = (TextView) findViewById(daysResourceId[index]);
            int freq = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY));
            int color = getColorGradient(freq, bitType);
            tableTV.setBackgroundTintList(ColorStateList.valueOf(color));
            prevDate = currentDate;
//            tableTV.setText(Integer.toString(freq));

        }

    }

    /**
     * This method is helping set Bit Count to today's bit value on the startup of this activity
     * This queries the frequency table with today's date attached. If a row is not found, count is
     * set to 0, else to the value in frequency column of the returned row
     *
     * @param freqTableUri uri to frequency table with today's date attached
     */
    public void setInitialCount(Uri freqTableUri){
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
    }
}
