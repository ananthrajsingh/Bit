package com.ananthrajsingh.bit.utilities;

import android.content.ContentValues;
import android.net.Uri;

import com.ananthrajsingh.bit.data.BitContract;

import java.util.Calendar;

import static com.ananthrajsingh.bit.utilities.TimeUtils.getTodaysDate;

/**
 * Created by Ananth on 4/9/2018.
 */

public class DatabaseUtils {
    /**
     * This function is called by the onClick on FAB of insert activity.
     *
     * @param name     name of habit to be inserted
     * @param type     good od bad
     * @param max_freq maximum frequency, used in color production
     * @return a content values object with all data to insert in the row
     */
    public static ContentValues makeContentValuesToInsert(String name, int type, int max_freq) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BitContract.MainTableEntry.COLUMN_NAME, name);
        contentValues.put(BitContract.MainTableEntry.COLUMN_BIT_TYPE, type);
        /* We have to enter zero since this column is marked as NOT NULL */
        contentValues.put(BitContract.MainTableEntry.COLUMN_TODAYS_BIT_COUNT, 0);
        contentValues.put(BitContract.MainTableEntry.COLUMN_MAX_BIT_COUNT, max_freq);
        return contentValues;
    }

    /**
     * This will provide Uri to insert data into the database wherever we need one.
     *
     * @return uri to main table
     */
    public static Uri buildUriToMainTable() {
        return Uri.withAppendedPath(BitContract.BASE_CONTENT_URI, BitContract.PATH_MAIN_TABLE);
    }

    /**
     * We may want a Uri to main table with _ID attached. And guess what? Yes we do!
     * We need this for deleting an entry, so here we are, overloading functions.
     *
     * @param id this will be attached to content://com.ananthrajsingh.bit/main/
     * @return Uri with passed id attached to main table uri
     */
    public static Uri buildUriToMainTable(long id){
        Uri uri = buildUriToMainTable();
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    /**
     * Helper method to build Uri in order to do increment and creation task on an entry in
     * frequency table. Evidently, we'll be only giving option to update current day's bit.
     * therefore we are dealing with getTodaysDate()
     * @param id this will help take us to right frequency table.
     * @return function name is self-explanatory about what we are returning.
     */
    public static Uri buildUriToFreqTableWithDate(long id){
        Uri returnUri = Uri.withAppendedPath(BitContract.BASE_CONTENT_URI,
                BitContract.FrequencTableEntry.TABLE_BASE_NAME + id);
        returnUri = Uri.withAppendedPath(returnUri, getTodaysDate());

        return returnUri;
    }

    /**
     * When we have to get all rows of a particular frequency table, we'll need this function to
     * build Uri.
     * @param id this will help determining which frequency table we are dealing with
     * @return Uri to frequency table with provided id
     */
    public static Uri buildUriToFreqTable(long id){
         return Uri.withAppendedPath(BitContract.BASE_CONTENT_URI,
                BitContract.FrequencTableEntry.TABLE_BASE_NAME + id);
    }



}