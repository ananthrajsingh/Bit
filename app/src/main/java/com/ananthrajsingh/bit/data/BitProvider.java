package com.ananthrajsingh.bit.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.ananthrajsingh.bit.utilities.FrequencyUtils.addRowToFrequencyTable;
import static com.ananthrajsingh.bit.utilities.FrequencyUtils.getTableNameFromUriWithDate;
import static com.ananthrajsingh.bit.utilities.FrequencyUtils.makeFrequencyTable;

/**
 * Created by Ananth on 4/5/2018.
 *
 * This class will serve as Content Provider of Bit's data.
 * Content Provider helps in encapsulation of data.
 * It doesn't reveals thee underlying structure of database.
 * It provides scalability and consistency too.
 *
 * Our content provider will have following jobs
 *  -query all data of main table
 *  -query a single habit with provided _ID
 *  -insert a new habit
 *  -delete a habit
 *
 *  Note that each time a new row(habit) is added to the Main table,
 *  a new table is added Frequency, which is unique to that row.
 *  Since this new Frequency table's name must be unique,
 *  we will use _ID of row in Main table to provide it a unique name.
 */

public class BitProvider extends ContentProvider {

  /*
   * These codes will be used to match URIs with the data they are looking for.
   * We will take advantage of UriMatcher class. This class makes matching much easier
   * than matching regular expressions. Also, we are in no mood to reinvent the wheel.
   */
    public static final int CODE_MAIN = 100;
    //Whole Main table
    public static final int CODE_MAIN_WITH_ID = 101;
    //Main table's row with provided _ID

    public static final int CODE_FREQUENCY = 200;
    //Whole frequency table

    public static final int CODE_FREQUENCY_WITH_DATE = 201;

  /*
   *    This is the private Uri Matcher used by this ContentProvider. We declare this
   *    as static because no matter how many instances of this class are formed, single
   *    UriMatcher will do. There is no need to execute this logic again and again for each
   *    instance.
   */
    private static UriMatcher sUriMatcher = buildUriMatcher();

    private BitDbHelper mOpenHelper;

    /**
     * This will build an UriMatcher. This UriMatcher will match each URI with either
     * CODE_MAIN, CODE_MAIN_WITH_ID, or CODE_FREQUENCY.
     *
     * This significantly reduces our to identify URIs, just tell is which code to be
     * matcher with which URI, and it does everything automatically.
     *
     * @return UriMatcher for our this ContentProvider
     */
    public static UriMatcher buildUriMatcher(){

        /*
         * The constructor of UriMatcher makes a root node of URI tree. NO_MATCH is the code
         * to match the root URI. This variable is declared as final because we don't want this
         * variable to point at an other object.
         */
        final UriMatcher bitUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BitContract.CONTENT_AUTHORITY;

        /* Whenever we find a match, the match() will return the respective code associated with
         * that that URI. We have to specify that code here while adding URI
         */

        /* This URI is content://com.ananthrajsingh.bit/main                                  */
        bitUriMatcher.addURI(authority, BitContract.PATH_MAIN_TABLE, CODE_MAIN );

        /* This URI is content://com.ananthrajsingh.bit/main/#
        *  The URI will look something like this content://com.ananthrajsingh.bit/main/23
        *  meaning, show the habit with _ID = 23 .
        *  */
        bitUriMatcher.addURI(authority, BitContract.PATH_MAIN_TABLE + "/#" , CODE_MAIN_WITH_ID);


        /* This URI is content://com.ananthrajsingh.bit/*                                      */
        bitUriMatcher.addURI(authority, "/*" , CODE_FREQUENCY);

        bitUriMatcher.addURI(authority, "/*/*", CODE_FREQUENCY_WITH_DATE);

         /*
        * **************************************************************************************
         * BEWARE!
         * Don't change the order of addURI() calls.
         * Notice that "content://com.ananthrajsingh.bit/main" qualifies both for
         * CODE_MAIN and CODE_FREQUENCY. So, if a URI is "content://com.ananthrajsingh.bit/*"
         * then, first CODE_MAIN must be checked, if it doesn't match for CODE_MAIN
         * it means that we are concerned about some frequency table.
         ****************************************************************************************
         * */

        return bitUriMatcher;

    }

    /**
     * onCreate is used to initialize the registered Content Provider. This method is called
     * on the main thread at the application launch time. Therefore it should be taken care that
     * no lengthy operations are carried in this.
     *
     * Deferred initialization keeps application startup fast, avoids unnecessary work if the
     * provider turns out not to be needed, and stops database errors (such as a full disk) from
     * halting application launch.
     *
     * @return true if Content Provider is successfully initialized, else false
     */
    @Override
    public boolean onCreate() {

        /*
         * BitDbHelper's constructor is lightweight, therefore we are safe to perform this
         * initialization.
         */
        mOpenHelper = new BitDbHelper(getContext());
        return true;
    }

     /*
    ******************************************************************************************************
    *  1.) We will create a new Frequency table in insert(..) as soon as new habit is inserted
    *
    *
    *  2.) Now, we have to increment a bit for a habit. We will query in query(..) whether today's
    *  date exists or not.
    *   Two cases arise-
    *      A.) Row for that day doesn't exists
    *      B.) Row for that day exists
    *
    *
    *  3.) For  (A) we will create new row in that table with today's date and set frequency to 1.
    *  This will happen in insert(..) itself.
    *  For  (B) we will update the frequency (incrementing) in update(..) method.
    * ****************************************************************************************************
     */


    /**
     * This function will bw used to insert a new row into the Main table. As a new row is
     * added to Main, a corresponding single column table is added to keep track of that Habit.
     * We will insert rows one by one.
     *
     * @param uri the uri where the
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        long returnId;
        Uri returnUri = null;
        boolean isCreated = false;
        Log.e("BitProvider.java", "We are in insert function");

        switch (match){

            /*
             * CODE_MAIN means we are inserting new habit. We will add a new row to Main
             * table using insertHabit. We will create a corresponding Frequency table
             * using makeFrequencyTable.
             */
            case CODE_MAIN:

                returnId = insertHabit(uri, values); //COMPLETED 6 Add this function
                returnUri = uri.buildUpon().
                        appendPath(Long.toString(returnId)).
                        build();

                isCreated = makeFrequencyTable(returnId, mOpenHelper); //COMPLETED 7 Add this function to FrequencyUtils
                if (!isCreated){
                    return null;
                }
                break;
            /*
             * We are here at CODE_FREQUENCY because there was no row for today in Frequency table.
             * Now here we will create one using helper methods. The frequency of that habit is to
             * be set to 1.
             */
            case CODE_FREQUENCY:
                returnUri = addRowToFrequencyTable(uri, mOpenHelper); // COMPLETED 8 Add this function to FrequencyUtils
                break;

            default:
                throw new UnsupportedOperationException("The uri for insertion turned out to be INVALID");
        }

        return returnUri;

    }



    private long insertHabit(Uri uri, ContentValues values){


        //Check whether habit has a name or not
        String name = values.getAsString(BitContract.MainTableEntry.COLUMN_NAME);
        if (name == null){
            throw new IllegalArgumentException("Name of habit cannot be empty. URI - " + uri);
        }

        /* get writable database since we need to write in database */
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();

        /* add values to database */
        long id = database.insert(BitContract.MainTableEntry.TABLE_NAME, null, values);

        if (id == -1){
            Log.e("BitProvider error", "Failed to add row into Main table for uri - " + uri);
            return id;
        }

        return id;
    }

    /**
     * We are going to use this method in 3 cases-
     *  1.) Show all habits in recycler view
     *  2.) Show a detailed view of a particular habit
     *  3.) Check whether today's row exists
     *  UPDATE
     *  4.) Get whole of Frequency table for detailed view
     *
     *  For (3), Uri will have the table's name whose Frequency table is to be checked for today's row.
     *  We will query its corresponding Frequency table, two cases arise-
     *      A) Row is found, then in update(..) we will increment bit
     *      B) Row is not found, then in insert(..) we will add new row
     *
     * @param uri this will tell which table and row to deal with
     * @param projection columns we are interested in, null here
     * @param selection null again
     * @param selectionArgs you guessed it, null. Actually, uri itself is sufficient
     * @param sortOrder no order yet, so yup, null.
     * @return cursor pointing to desired row(s)
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case CODE_MAIN:
                cursor = mOpenHelper.getReadableDatabase().query(
                        BitContract.MainTableEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                        );
                break;
            case CODE_MAIN_WITH_ID:
                String id = uri.getLastPathSegment();
                String[] selectionArguments = {id};
                cursor = mOpenHelper.getReadableDatabase().query(
                        BitContract.MainTableEntry.TABLE_NAME,
                        projection,
                        BitContract.MainTableEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                        );
                break;
            case CODE_FREQUENCY_WITH_DATE:
                String dateOfHabitBit = uri.getLastPathSegment();

                // This is the format of date "MM-dd-yyyy"
                String[] selectionArgumentsDate = {dateOfHabitBit};

                String tableName = getTableNameFromUriWithDate(uri);

                cursor = mOpenHelper.getReadableDatabase().query(
                        tableName,
                        projection,
                        BitContract.FrequencTableEntry.COLUMN_DATE + " = ? ",
                        selectionArgumentsDate,
                        null,
                        null,
                        sortOrder
                );
                break;
            /*
             * We'll be needing whole of frequency table in cursor when we display detailed view
             * of that habit. This is added later, how the hell I missed this case!
             */
            case CODE_FREQUENCY:
                String frequencyTableName = uri.getLastPathSegment();
                cursor = mOpenHelper.getReadableDatabase().query(frequencyTableName,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Query cannot be completed, cuz unknown Uri - " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    /**
     * In update(..), we will increment the bit for a particular habit. Here we will assume that
     * today's entry exists. Currently, updating the name of habit is not allowed for the sake
     * of simplicity of our application.
     *
     * @param uri uri to update table
     * @param values not used here
     * @param selection this will be frequency column
     * @param selectionArgs this will be extracted from uri, so useless again
     * @return the number of rows updated
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int numOfRowsUpdated = 0;
        String tableName = getTableNameFromUriWithDate(uri);
        String date = uri.getLastPathSegment();
        String[] selectionArguments = {date};
            /*
             * Below case will have to get current value of frequency in order to inrement the value
             * by one. Therefore, we will call query(..) method to get the current count of frequency.
             * This uri will get into CODE_FREQUENCY_WITH_DATE case. Once we get the current value
             * then we will build a content values object and update
             * the row.
             */
        Cursor cursor = query(uri, null, null, null, null);
        /*
         * cursor will consist of single row, therefore we'll move it to position 0
         */
        cursor.moveToPosition(0);
            /* Extract current frequency */
        int oldFrequency = cursor.getInt(cursor.getColumnIndex(BitContract.FrequencTableEntry.COLUMN_FREQUENCY));
        long id = cursor.getLong(cursor.getColumnIndex(BitContract.FrequencTableEntry._ID));
        cursor.close();
        int newFrequency = oldFrequency + 1 ;

            /*Build a new ContentValues object to update the row we are dealing with */

        ContentValues contentValues = new ContentValues();
        contentValues.put(BitContract.FrequencTableEntry._ID, id);
        contentValues.put(BitContract.FrequencTableEntry.COLUMN_DATE, date);
        contentValues.put(BitContract.FrequencTableEntry.COLUMN_FREQUENCY, newFrequency);


        switch (sUriMatcher.match(uri)){


            case CODE_FREQUENCY_WITH_DATE:
                numOfRowsUpdated = mOpenHelper.getWritableDatabase().update(
                        tableName,
                        contentValues,
                        BitContract.FrequencTableEntry.COLUMN_DATE + " = ? ",
                        selectionArguments
                );
                break;
            default:
                throw new IllegalArgumentException("Invalid uri sent to Provider :( - " + uri);

        }
        return numOfRowsUpdated;
    }

    /**
     * Habits can be deleted. This method will delete a row from main table. One more thimg,
     * we will also delete the corresponding frequency table from the database
     *
     * @param uri which row is to be deleted
     * @param selection column name
     * @param selectionArgs if these value found in said column name, delete it
     * @return number of rows deleted
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numberOfRowsDeleted = 0;

        String id = uri.getLastPathSegment();
        String[] selectionArguments = {id};

        switch (sUriMatcher.match(uri)){

            case CODE_MAIN_WITH_ID :
                numberOfRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        BitContract.MainTableEntry.TABLE_NAME,
                        BitContract.MainTableEntry._ID + " = ? ",
                        selectionArguments
                );
                break;
            default:
                throw new IllegalArgumentException("Invalid uri provided to provider - " + uri);
        }
        return numberOfRowsDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new IllegalArgumentException("WE ARE NOT IMPLEMENTING THIS METHOD");
    }
}