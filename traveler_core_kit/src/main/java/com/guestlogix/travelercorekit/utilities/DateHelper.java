package com.guestlogix.travelercorekit.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"; //ISO 8601
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String PRETTY_DATE_PATTERN = "dd MMM yyyy";
    private static final String TIME_PATTERN = "HH:mm";
    private static final Calendar mCalendar = Calendar.getInstance();

    public static String formatDateToISO8601(Date date) {
        return new SimpleDateFormat(DATE_TIME_PATTERN, Locale.US).format(date);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(PRETTY_DATE_PATTERN, Locale.US).format(date);
    }

    public static Date parseISO8601(String dateString) throws ParseException {
        return new SimpleDateFormat(DATE_TIME_PATTERN, Locale.US).parse(dateString);
    }

    public static Date parseDate(String dateString) throws ParseException {
        return new SimpleDateFormat(DATE_PATTERN, Locale.US).parse(dateString);
    }

    public static String formatTime(Date date) {
        return new SimpleDateFormat(TIME_PATTERN, Locale.US).format(date);
    }

    public static String formatTime(Long rowItem) {
        if (null == rowItem)
            return null;
        mCalendar.set(Calendar.HOUR_OF_DAY, rowItem.intValue() / 60);
        mCalendar.set(Calendar.MINUTE, rowItem.intValue() % 60);

        return DateHelper.formatTime(mCalendar.getTime());
    }
}
