package com.ananthrajsingh.bit.data;

import android.content.ContentProvider;
import android.content.UriMatcher;
import android.net.Uri;

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

//    public static final int CODE_FREQUENCY = 200;
//    //Whole frequency table

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

//        /* This URI is content://com.ananthrajsingh.bit/*                                      */
//        bitUriMatcher.addURI(authority, "/*" , CODE_FREQUENCY);

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
}
