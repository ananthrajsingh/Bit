package com.ananthrajsingh.bit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ananth on 4/4/2018.
 * This class will create a database.
 * onCreate is run when database is initially created.
 * onUpgrade is called when version number of the database is incremented
 */

public class BitDbHelper extends SQLiteOpenHelper {

  //This is the name of database. This should be descriptive and should have extension .db
    public static final String DATABASE_NAME = "bit.db";

  //This will be incremented when the schema of database changes.
  // On incrementing, onUpgrade is called.
  //Database version is stored in sqlite database file itself.
    public static final int DATABASE_VERSION = 1;


    public BitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * The constructor checks whether a database with DATABASE_NAME exists.
     * If not, this method is called.
     *
     * We are using this to create our Main table.
     * Note that Tables of type 2 are not created here.
     * This is because we don't still know that how many and of what name the tables are needed.
     * They will be added dynamically using getWritableDatabase()
     *
     * @param db The Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    // TODO 4 - Create Main table
    }


    /**
     * When an object of this class is created, constructor is called.
     * Constructor checks if the database with DATABASE_NAME exists.
     * If it exists, DATABASE_VERSION is checked, which is stored in database itself.
     * If provided database has version number smaller than the on passed in constructor, this method is called.
     *
     * Currently, we will drop the current Main table and create new.
     * Though this is not what we want. We will try to add functionality so that user data is not lost.
     *
     * @param db The current database to modify
     * @param oldVersion Version stored in database
     * @param newVersion Version passed in the constructor
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 5 - write upgrading code
    }
}
