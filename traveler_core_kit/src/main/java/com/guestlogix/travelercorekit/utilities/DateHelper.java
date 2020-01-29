package com.guestlogix.travelercorekit.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {
    // Theres an issue in Java with parsing ISO 8601, so two formats need to be checked for parsing strings
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ"; //ISO 8601
    private static final String DATE_TIME_PATTERN_WITHOUT_ZONE = "yyyy-MM-dd'T'HH:mm:ss";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String PRETTY_DATE_PATTERN = "MMM dd, yyyy";
    private static final String TIME_PATTERN = "HH:mm";
    private static final Calendar calendar = Calendar.getInstance();

    public static String formatDateToISO8601(Date date) {
        return new SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault()).format(date);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(PRETTY_DATE_PATTERN, Locale.getDefault()).format(date);
    }

    public static String formatToDayNameMonthDayYear(Date date) {
        return new SimpleDateFormat("EEE, dd MMM, yyyy", Locale.getDefault()).format(date);
    }

    public static String formatToMonthDayYearTime(Date date) {
        return new SimpleDateFormat("M/d/yyyy h:mm a", Locale.getDefault()).format(date);
    }

    public static String formatToMonthDayYearSlashDelimited(Date date) {
        return new SimpleDateFormat("M/d/yyyy", Locale.getDefault()).format(date);
    }

    public static String formatToHourMinuteMeridian(Date date) {
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(date);
    }

    public static Date parseISO8601(String dateString) throws ParseException {
        try {
            return new SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault()).parse(dateString);
        } catch (ParseException e) {
            return new SimpleDateFormat(DATE_TIME_PATTERN_WITHOUT_ZONE, Locale.getDefault()).parse(dateString);
        }
    }

    public static Date parseISO8601(String dateString, TimeZone zone) throws ParseException {
        try {
            DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault());
            dateFormat.setTimeZone(zone);
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN_WITHOUT_ZONE, Locale.getDefault());
            dateFormat.setTimeZone(zone);
            return dateFormat.parse(dateString);
        }
    }

    public static Date parseDate(String dateString) throws ParseException {
        return new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(dateString);
    }

    public static String formatTime(Date date) {
        return new SimpleDateFormat(TIME_PATTERN, Locale.getDefault()).format(date);
    }

    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat(TIME_PATTERN);
    }

    public static String formatTime(Long rowItem) {
        if (null == rowItem)
            return null;
        calendar.set(Calendar.HOUR_OF_DAY, rowItem.intValue() / 60);
        calendar.set(Calendar.MINUTE, rowItem.intValue() % 60);

        return DateHelper.formatTime(calendar.getTime());
    }
}
