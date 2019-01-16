package com.guestlogix.travelercorekit.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String PRETTY_DATE_PATTERN = "dd MMM yyyy";
    public static final String TIME_PATTERN = "HH:mm";

    public static String getDateAsString(Date date) {
        return new SimpleDateFormat(DATE_PATTERN, Locale.US).format(date);
    }

    public static String getPrettyDateAsString(Date date) {
        return new SimpleDateFormat(PRETTY_DATE_PATTERN, Locale.US).format(date);
    }

    public static Date getDateAsObject(String dateString) throws ParseException {
        return new SimpleDateFormat(DATE_PATTERN, Locale.US).parse(dateString);
    }

    public static String getTimeAsString(Date date) {
        return new SimpleDateFormat(TIME_PATTERN, Locale.US).format(date);
    }

}
