package com.getmebag.bag.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by karthiktangirala on 6/6/15.
 */
public class DateUtils {

    public static final String MM_DD_YYYY = "MM/dd/yyyy";

    public static String dateToString(Date date, String format) {
        return new SimpleDateFormat(format, Locale.US)
                .format(date);
    }

    public static Date stringToDate(String dateString, String toFormat) {
        SimpleDateFormat format = new SimpleDateFormat(toFormat);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar dateToCalendar(Date date){
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        return null;
    }

    public static Date calendarToDate(Calendar calendar){
        return calendar.getTime();
    }

}
