package com.ananthrajsingh.bit.utilities;

import android.util.Log;

import com.ananthrajsingh.bit.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ananth on 4/6/2018.
 * This class will have helper methods to solve date and time related
 * problems.
 */

public class TimeUtils {

    public static final int[] daysResourceId = {
              R.id.bitTV_00, R.id.bitTV_01, R.id.bitTV_02, R.id.bitTV_03, R.id.bitTV_04, R.id.bitTV_05, R.id.bitTV_06
            , R.id.bitTV_10, R.id.bitTV_11, R.id.bitTV_12, R.id.bitTV_13, R.id.bitTV_14, R.id.bitTV_15, R.id.bitTV_16
            , R.id.bitTV_20, R.id.bitTV_21, R.id.bitTV_22, R.id.bitTV_23, R.id.bitTV_24, R.id.bitTV_25, R.id.bitTV_26
            , R.id.bitTV_30, R.id.bitTV_31, R.id.bitTV_32, R.id.bitTV_33, R.id.bitTV_34, R.id.bitTV_35, R.id.bitTV_36
    };

    public static final int[] weeksdaysResourceId ={
            R.id.daysTv1, R.id.daysTv2, R.id.daysTv3, R.id.daysTv4, R.id.daysTv5, R.id.daysTv6, R.id.daysTv7
    };
    public static final int[] colorGreen = {
            R.color.green1, R.color.green2, R.color.green3,
            R.color.green4, R.color.green5
    };

    public static final int[] colorRed = {
            R.color.red1, R.color.red2, R.color.red3,
            R.color.red4, R.color.red5
    };

    public static String getTodaysDate(){

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat formatType = new SimpleDateFormat("MM-dd-yyyy");
        return formatType.format(calendar.getTime());
    }
    /**
     * This will calculate the int value for today's day of week. For example, 0 for Sunday, 2 for
     * Tuesday
     * @return today's day in week starting from Sunday
     */
    public static int todaysDayOffset(){
        Calendar calendar = Calendar.getInstance();
        return ( calendar.get(Calendar.DAY_OF_WEEK) - 1 );
    }

    public static int getDateDifference(String currentDate, String prevDate){
        /*
        We know that date format is in the form MM-dd-yyyy
         */
        Log.e("TimeUtils.java", "we are in getDateDifference");
        String todaysDate = getTodaysDate();
        int todaysDay  = Integer.parseInt(todaysDate.substring(3, 5));
        int currentDay = Integer.parseInt(currentDate.substring(3, 5));
        int prevDay = Integer.parseInt(prevDate.substring(3, 5));

        int todaysMonth = Integer.parseInt(todaysDate.substring(0,2));
        int currentMonth = Integer.parseInt(currentDate.substring(0, 2));
        int prevMonth = Integer.parseInt(prevDate.substring(0, 2));

        int differenceOffset = 0;

        Log.e("BitDetail.java", "getDateDifference : currentDate - "+currentDate+" prevDate - "+prevDate+" todaysDate - "+todaysDate);
        Log.e("BitDetail.java", "getDateDifference : currentMonth - "+currentMonth+" prevMonth - "+prevMonth+" todaysMonth - "+todaysMonth);

        //This is opposite because we are filling circle table starting from bottom of frequency table

        if (todaysMonth != currentMonth){
            Log.e("TimeUtils.java", " We are in if (todaysMonth != currentMonth) todaysMonth - " + todaysMonth +
                    " currentMonth - " + currentMonth);
            differenceOffset = getNumberOfDaysInMonth(currentMonth);

        }
        else if (currentMonth != prevMonth){
            Log.e("TimeUtils.java", " We are in if (currentMonth != prevMonth) currentMonth - " + currentMonth +
                    " prevMonth - " + prevMonth);
            differenceOffset = getNumberOfDaysInMonth(prevMonth);
        }
        int difference = prevDay - currentDay;
        Log.e("TimeUtils", "difference = prevDay - currentDay + differenceOffset -- "+ difference +" = "
                +prevDay+" - "+currentDay+" + "+differenceOffset);
        if (difference == 0){

            /*
             * This case will apply for the first circle to draw, currentDate and prevDate will be same
             * In this case we'll check difference between today's date andthe last entry in frequency
             * table. If this case is not handled, dot filling will start from today's date, even if
             * there are no entry for today or from weeks.
             */

            difference = todaysDay - currentDay + differenceOffset;

        }
        Log.e("TimeUtils.java", "returned difference - " + difference);
        return difference;
    }

    public static int getNumberOfDaysInMonth(int month){
        int days;
        if (month == 2){
            days = 28;
        }
        else if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12){
            days = 31;
        }
        else {
            days = 30;
        }
        return days;
    }

}
