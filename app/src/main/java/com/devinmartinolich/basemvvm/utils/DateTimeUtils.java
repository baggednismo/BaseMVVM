package com.devinmartinolich.basemvvm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    private static final String TAG = "DateTimeUtils";

    //The date format to convert from base format.
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // The time format to convert from base format.
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());

    // This is base format of date and time
    public static final SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    // This is the picture format used in the previous version of the app
    public static final SimpleDateFormat pictureFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

    /**
     * Name: getFormattedDate
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: To get the date only by formatting from the date-time received.
     *
     * @param aDateString as date-time string to format from.
     * @return as formatted date
     */
    public static String getFormattedDate(String aDateString) {
        Date date;
        try {
            date = baseFormat.parse(aDateString);
            if (date != null)
                return dateFormat.format(date);
        } catch (ParseException e) {
            /*
             * Will get exception when the format of date-time passed in is not the same base format.
             */
            aDateString = null;
        } catch (Exception e) {
            // No need to do anything as this exception is not due to parsing the date so returning what we get
        }
        return aDateString;
    }

    /**
     * Name: getFormattedTime
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: To get the time only by formatting from the date-time received.
     *
     * @param aDateString as date-time string to format from.
     * @return as formatted time
     */
    public static String getFormattedTime(String aDateString) {
        Date date;
        try {
            date = baseFormat.parse(aDateString);
            if (date != null)
                return timeFormat.format(date);
        } catch (ParseException e) {
            /*
             * Will get exception when the format of date-time passed in is not the same base format.
             */
            aDateString = null;
        } catch (Exception e) {
            // No need to do anything as this exception is not due to parsing the date so returning what we get
        }
        return aDateString;
    }

    /**
     * Name: getFormattedDateTime
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: To get date and time both by formatting from the date-time received.
     *
     * @param aDateString as date-time string to format from.
     * @return as formatted date and time
     */
    public static String getFormattedDateTime(String aDateString) {
        Date date;
        try {
            date = baseFormat.parse(aDateString);
            if (date != null)
                return baseFormat.format(date);

        } catch (ParseException e) {
            /*
             * Will get exception when the format of date-time passed in is not the same base format.
             */
            aDateString = null;
        } catch (Exception e) {
            // No need to do anything as this exception is not due to parsing the date so returning what we get
        }
        return aDateString;
    }

    /**
     * Name: getFormattedDateTime
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: To get date and time both by formatting from the date-time received.
     *
     * @param aDateTimeStamp as date-time stamp to format from
     * @return as formatted date and time
     */
    public static String getFormattedDateTime(long aDateTimeStamp) {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(aDateTimeStamp);
        return baseFormat.format(calendar.getTime());
    }

    /**
     * Name: getCurrentDateTime
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: To return current date time
     *
     * @return String of date time.
     */
    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(c.getTime());
    }

    /**
     * Name: getCurrentDate
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: To return current date time
     *
     * @return String of date time.
     */
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return dateFormat.format(c.getTime());
    }

    /**
     * Name: getDateTimeDiffMilli
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: Provide the difference in milliseconds between two dates.
     *
     * @param earlierDateTime - date expected to be previous date
     * @param laterDateTime - date expected to be latter date
     * @return date difference in milliseconds
     */
    public static long getDateTimeDiffMilli(String earlierDateTime, String laterDateTime) {
        AppLog.d(TAG, "-> getDateTimeDiffMilli()");
        try {
            Date eDateTime = baseFormat.parse(earlierDateTime);
            Date lDateTime = baseFormat.parse(laterDateTime);

            return lDateTime.getTime() - eDateTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Name: isDateBefore
     * Created by Devin Martinolich 3/17/2020
     * Modified by
     * Purpose: Determine if a date is prior to another.
     *
     * @param earlierDateTime - date expected to be previous date
     * @param laterDateTime - date expected to be latter date
     * @return boolean of date comparison
     */
    public static Boolean isDateBefore(String earlierDateTime, String laterDateTime) {
        AppLog.d(TAG, "-> isDateBefore()");
        try {
            return dateFormat.parse(earlierDateTime).before(dateFormat.parse(laterDateTime));
        } catch (ParseException e) {
            AppLog.e(TAG, e.getMessage());
        }
        return false;
    }
}
