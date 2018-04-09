package com.ananthrajsingh.bit.utilities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.ananthrajsingh.bit.data.BitContract;
import com.ananthrajsingh.bit.data.BitDbHelper;

import static com.ananthrajsingh.bit.utilities.TimeUtils.getTodaysDate;

/**
 * Created by Ananth on 4/5/2018.
 * We will write helper methods for Frequency table here.
 */

public class FrequencyUtils {

//    public static final String

    /**
     * This is called from insert(..) of our content provider. This will make a corresponding
     * Frequency table for each row entry.
     *
     * @param idToAppend used to create a unique table name
     * @param databaseHelper database helper class to get hold of database
     * @return true to signify that this function ran successfully
     */
    public static boolean  makeFrequencyTable(long idToAppend, BitDbHelper databaseHelper){

        int idInInt = (int) idToAppend;
        String idString = Integer.toString(idInInt);
        String FREQUENCY_TABLE_STATEMENT = "CREATE TABLE " +
                BitContract.FrequencTableEntry.TABLE_BASE_NAME + idString + " (" +
                BitContract.FrequencTableEntry._ID + " INTEGER PRIMARY KEY,  " +
                BitContract.FrequencTableEntry.COLUMN_FREQUENCY + " INTEGER, " +
                BitContract.FrequencTableEntry.COLUMN_DATE +  "TEXT)";

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.execSQL(FREQUENCY_TABLE_STATEMENT);
        return true;
    }

    /**
     * This will calculate today's date and create new row into the provided frequency table.
     * This new row will have today's date in date column and 1 in frequency column.
     *
     * @param uri will give us the name of Frequency table
     * @return uri returned by insert funtion of database
     */
    public static Uri addRowToFrequencyTable(Uri uri, BitDbHelper databaseHelper){
        String todaysDate = getTodaysDate();
        ContentValues values = new ContentValues();
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        values.put(BitContract.FrequencTableEntry.COLUMN_FREQUENCY, 1);
        values.put(BitContract.FrequencTableEntry.COLUMN_DATE, todaysDate);

        String tableName = getTableNameFromUri(uri);

        long id = database.insert(tableName, null, values);
        /* This is expected to return something like content://com.ananthrajsingh/bit/12 */
        return uri.buildUpon().
                appendPath(Long.toString(id)).
                build();
    }

    public static String getTableNameFromUri(Uri uri){

        return uri.getLastPathSegment();
    }
    public static String getTableNameFromUriWithDate(Uri uri){
        /*
         * This will return the path segment at provided index. For example
         * content://com.ananthrajsingh.bit/frequency_22/21
         * get(0) will return frequency_22
         * get(1) will return 21
         */
        return uri.getPathSegments().get(0);
    }
}
