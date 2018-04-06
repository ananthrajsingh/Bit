package com.ananthrajsingh.bit.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ananth on 4/6/2018.
 * This class will have helper methods to solve date and time related
 * problems.
 */

public class TimeUtils {

    public static String getTodaysDate(){

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat formatType = new SimpleDateFormat("MM/dd/yyyy");
        return formatType.format(calendar.getTime());
    }

}
