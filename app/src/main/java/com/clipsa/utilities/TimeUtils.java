package com.clipsa.utilities;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ordgen on 9/7/17.
 */

public class TimeUtils {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;


    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE) {
            return "just now";
        } else if (diff < 2 * MINUTE) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE) {
            return diff / MINUTE + " minutes ago";
        } else if (diff < 90 * MINUTE) {
            return "an hour ago";
        } else if (diff < 24 * HOUR) {
            return diff / HOUR + " hours ago";
        } else if (diff < 48 * HOUR) {
            return "yesterday";
        } else {
            DateTime dateTime = new DateTime(time, DateTimeZone.UTC);
            return  dateTime.monthOfYear().getAsShortText() + " " + dateTime.getDayOfMonth() + "," + " " + dateTime.getYear();
        }
    }

    public String convertTimeWithTimeZome(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(time);
        return (cal.get(Calendar.YEAR) + " " + (cal.get(Calendar.MONTH) + 1) + " "
                + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE));
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return df.format(c.getTime());
    }


    public static String getDateTimestamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM dd yyyy HH:mm", Locale.getDefault());
        return df.format(c.getTime());
    }

    public static String getDateTimestampForReceipt() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM-dd yyyy HH:mm", Locale.getDefault());
        return df.format(c.getTime());
    }


    public static String formatDateAndTime(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("EEEE dd MMM, yyyy", Locale.getDefault());
        return df.format(calendar.getTime());
    }

    public static String getTodayDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(c.getTime());
    }



    public static String getDateTime(int months) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, months);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return df.format(c.getTime());
    }



    public static String convertToPrettyTime(long timeInMillis){
      PrettyTime prettyTime = new PrettyTime();

        return prettyTime.format(new Date(timeInMillis));
    }


    public static String convertToPrettyTime(String date) throws ParseException {
        PrettyTime prettyTime = new PrettyTime();
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date);
        return prettyTime.format(date1);
    }





}
