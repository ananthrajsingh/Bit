package com.ananthrajsingh.bit.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ananth on 4/4/2018.
 * This class will store all the Paths, Table and Column names for two Tables.
 * We are creating 2 tables.
 *
 * TABLE 1 has columns BIT_NAME, BIT_TODAY, BIT_MAX
 *
 * TABLE 2 has column BIT_COUNT
 *
 * Every row of table 1 will have its separate table 2
 *
 */

public class BitContract {
/*
   * The "Content Authority" is name fot entire content provider,
   * similar to a domain name and it's website.
   * A safe string to use is the package name for the app,
   * as it is guaranteed that it will be unique on Play Store
 */
    public static final String CONTENT_AUTHORITY = "com.ananthrajsingh.bit";

   /*
    * Applications will use this URI to contact content provider for our Bit app
    */
   public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  /*
    * We are going to use 2 types of tables,
    * One is Main table of which, we will define a path.
    * For table of type two, we are going to have tables of different name for each habit,
    * therefore no constants will be defined as table name will be defined when user inputs habits.
   */
    public static final String PATH_MAIN_TABLE = "main";

  /*
   Inner class that defines the columns of Main table
   */
    public static final class MainTableEntry implements BaseColumns {

    // CONTENT_URI_MAIN is used to query Main table from content provider
      public static final Uri CONTENT_URI_MAIN = BASE_CONTENT_URI.buildUpon()
                                                .appendPath(PATH_MAIN_TABLE)
                                                .build();

    //This column will store Strings which the user will enter.
      public static final String COLUMN_NAME = "name";

    //This column stores today's frequency of habit.
    //Main activity will show habits, those will change color according to today's frequency.
    //This column will store int.
      public static final String COLUMN_TODAYS_BIT_COUNT = "todays_bit";

    //This column also stores int value of maximum ever frequency of that habit.
    //This will help decide the color of daily bits.
    //At maximum frequency, bits will be darker, with decreasing order of darkness.
      public static final String COLUMN_MAX_BIT_COUNT = "max_bit";

  }

    /*

         * This is inner class for second kind of table.
         *
         * Content uri is not defined here.
         * The reason being, we dont know the table name until user enters Habit's name.
         * We will handle this dynamically.
         *
         * Since all those tables will have same columnar structure,
         * we can define column names.
    */
    public static final class FrequencTableEntry implements BaseColumns {

      //This will store the frequency of bit of that day
      //in the form of int
        public static final String COLUMN_FREQUENCY = "frequency";

    }

}
