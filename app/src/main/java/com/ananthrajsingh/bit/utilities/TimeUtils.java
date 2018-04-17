package com.ananthrajsingh.bit.utilities;

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

    public static final int[] colorGreen = {
            R.color.green1, R.color.green2, R.color.green3, R.color.green4, R.color.green5
    };

    public static final int[] colorRed = {
            R.color.red1, R.color.red2, R.color.red3, R.color.red4, R.color.red5
    };

    public static String getTodaysDate(){

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat formatType = new SimpleDateFormat("MM-dd-yyyy");
        return formatType.format(calendar.getTime());
    }

}
