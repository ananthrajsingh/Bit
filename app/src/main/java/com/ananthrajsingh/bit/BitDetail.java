package com.ananthrajsingh.bit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ananthrajsingh.bit.data.BitContract;

import static com.ananthrajsingh.bit.MainActivity.BAD_BIT_ID;
import static com.ananthrajsingh.bit.MainActivity.GOOD_BIT_ID;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToFreqTable;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToFreqTableWithDate;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;
import static com.ananthrajsingh.bit.utilities.TimeUtils.daysResourceId;
import static com.ananthrajsingh.bit.utilities.TimeUtils.getDateDifference;
import static com.ananthrajsingh.bit.utilities.TimeUtils.todaysDayOffset;
import static com.ananthrajsingh.bit.utilities.TimeUtils.weeksdaysResourceId;

public class BitDetail extends AppCompatActivity {

//    public TextView bitCountTextView;
    public Button mPlusOneButton;
    public ImageView mExpandImageView;
    public long mIdOfHabit;
    public int mMaxFrequency;
    public int mBitType;
    public String mHabitName;
    public int mPositionInRv; // Used to make this habit disappear from RV on delete
    public boolean mIsFrequencyShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_detail);


        /* getting information about which item was clicked */
        Intent intent = getIntent();
        mIdOfHabit = intent.getLongExtra(getString(R.string.item_id_extra), -1);
        mBitType = intent.getIntExtra(getString(R.string.item_type_extra), -1);
        mMaxFrequency = intent.getIntExtra(getString(R.string.max_frequency_extra), 1);
        mHabitName = intent.getStringExtra(getString(R.string.name_extra));
        mPositionInRv = intent.getIntExtra(getString(R.string.position_extra), 100);

        /* Changing the name of ActionBar for the clicked habit */
        getSupportActionBar().setTitle(mHabitName);

        mExpandImageView = (ImageView) findViewById(R.id.expand_imageView);
        mPlusOneButton = (Button) findViewById(R.id.button);
        if (mBitType == GOOD_BIT_ID){
            mPlusOneButton.setBackground(getDrawable(R.drawable.button_good_ripple));
        }
        else {
            mPlusOneButton.setBackground(getDrawable(R.drawable.button_bad_ripple));
        }

        /* This will set 0 if there is no today's row, else will show today's count */
        final Uri freqTableUri = buildUriToFreqTableWithDate(mIdOfHabit);
        setInitialCount(freqTableUri);

        mExpandImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Send to expanded activity
            }
        });

        mPlusOneButton.setOnClickListener(new View.OnClickListener() {
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
                }
                /* This will update matrix after every click */
                showDataInMonthMatrix(mIsFrequencyShown);
            }
        });
        /*
         * This will fill dots in the matrix on start up of the activity
         */
       showDataInMonthMatrix(mIsFrequencyShown);
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
        Uri uri1 = buildUriToMainTable(mIdOfHabit);
        if (id == R.id.action_delete){
//            MainActivity.removeHabitFromRv(mPositionInRv, mIdOfHabit);
//                getContentResolver().delete(uri1, null, null);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.rv_position_extra), mPositionInRv);
                intent.putExtra(getString(R.string.id_of_habit_extra), mIdOfHabit);
                startActivity(intent);
                return true;
            }
         else if (id == R.id.action_show_frequency){
            CharSequence title = item.getTitle();
            if (title.equals(getString(R.string.action_show_frequency))) {
                item.setTitle(getString(R.string.action_hide_frequency));
                mIsFrequencyShown = true;
            }
            else {
                item.setTitle(getString(R.string.action_show_frequency));
                mIsFrequencyShown = false;
            }
            showDataInMonthMatrix(mIsFrequencyShown);
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

    @SuppressLint("ResourceAsColor")
    private int getColorGradient(int frequency, int habitType) {
        int retColor = R.color.white;
        if (habitType == BAD_BIT_ID) {
            if (frequency >= mMaxFrequency){
                retColor = ContextCompat.getColor(this, R.color.red5);
            }
            else if (frequency >= (int) ((4* mMaxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.red4);
            }
            else if (frequency >= (int) ((3* mMaxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.red3);
            }
            else if (frequency >= (int) ((2* mMaxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.red2);
            }
            else if (frequency >= (int) (mMaxFrequency /5)){
                retColor = ContextCompat.getColor(this, R.color.red1);
            }
            else{
                retColor = ContextCompat.getColor(this, R.color.red1);
            }
        }
        if (habitType == GOOD_BIT_ID){
            if (frequency >= mMaxFrequency){
                retColor = ContextCompat.getColor(this, R.color.green5);
            }
            else if (frequency >= (int) ((4* mMaxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.green4);
            }
            else if (frequency >= (int) ((3* mMaxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.green3);
            }
            else if (frequency >= (int) ((2* mMaxFrequency)/5)){
                retColor = ContextCompat.getColor(this, R.color.green2);
            }
            else if (frequency >= (int) (mMaxFrequency /5)){
                retColor = ContextCompat.getColor(this, R.color.green1);
            }
            else{
                retColor = ContextCompat.getColor(this, R.color.green1);
            }
        }


        return retColor;
    }

    private void addTodaysRow(){

        Uri freqTableUri = buildUriToFreqTableWithDate(mIdOfHabit);
        Uri uri = buildUriToFreqTable(mIdOfHabit);

        /* This will insert a new row for today's entry, since we figured out that there is no today's row */
        Uri returnedUri = getContentResolver().insert(uri, null);
        Log.e("BitDetail.java", "addTodaysRow: We entered today's row, returned uri - " + returnedUri);
        if (returnedUri != null){
            int numberOfRowsAffected = getContentResolver().update(freqTableUri,
                    null,
                    null,
                    null);

        }
    }


    /**
     * First, this function arranges the weekday titles starting from today's day at index 0.
     * Then we come to the bit matrix, bit matrix is inflated by this function.
     * We are calling this in the onCreate as we want this to lay as soon as activity is opened.
     */
    private void showDataInMonthMatrix(boolean isFrequencyShown){

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
        Uri uriToFreqTable = buildUriToFreqTable(mIdOfHabit);
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
            if(index < 28 && index >= 0){
                tableTV = (TextView) findViewById(daysResourceId[index]);
                int freq = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY));
                int color = getColorGradient(freq, mBitType);
                tableTV.setBackgroundTintList(ColorStateList.valueOf(color));
                prevDate = currentDate;
                if (isFrequencyShown) {
                    tableTV.setText(Integer.toString(freq));
                /*
                 * This below statement made unfilled bits to shift down. To fix this, we added
                 * android:layout_gravity="center" to all the bit views
                 */
                    tableTV.setGravity(Gravity.CENTER);
                }
                else {
                    tableTV.setText(null);
                }
            }


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
            cursor.close();
        }
        else{
            cursor.moveToLast();
            int freq = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY));
            cursor.close();
        }
    }
}
