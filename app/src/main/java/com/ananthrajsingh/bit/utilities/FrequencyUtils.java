package com.ananthrajsingh.bit.utilities;

import android.database.sqlite.SQLiteDatabase;

import com.ananthrajsingh.bit.data.BitContract;
import com.ananthrajsingh.bit.data.BitDbHelper;

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
    public static boolean makeFrequencyTable(long idToAppend, BitDbHelper databaseHelper){

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
}
