package com.ananthrajsingh.bit.utilities;

import android.content.ContentValues;
import android.net.Uri;

import com.ananthrajsingh.bit.data.BitContract;

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
//        return BitContract.BASE_CONTENT_URI.buildUpon().appendPath(BitContract.PATH_MAIN_TABLE).build();
        Uri returnUri = Uri.withAppendedPath(BitContract.BASE_CONTENT_URI, BitContract.PATH_MAIN_TABLE);
        return returnUri;
    }
    public static Uri buildUriToMainTable(long id){
        Uri uri = buildUriToMainTable();
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

}