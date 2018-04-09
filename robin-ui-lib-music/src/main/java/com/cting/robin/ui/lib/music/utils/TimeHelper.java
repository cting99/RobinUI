package com.cting.robin.ui.lib.music.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeHelper {


    private static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;

    public static final String TIME_FORMAT = "HH:mm";
    public static final String TIME_FORMAT_ERROR = "--";

    public static final String formatDuration(long duration) {
        int hour = (int) (duration / ONE_HOUR);
        int minute = (int) (duration % ONE_HOUR / ONE_MINUTE);
        int second = (int) (duration % ONE_HOUR % ONE_MINUTE / ONE_SECOND);
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
        return String.format("%02d:%02d", minute, second);
    }

    public static String formatMusicDuration(long duration) {
        DateFormat format = new SimpleDateFormat("hh:MM");
        Date date = new Date();
        date.setTime(duration);
        return format.format(date);
    }
}


