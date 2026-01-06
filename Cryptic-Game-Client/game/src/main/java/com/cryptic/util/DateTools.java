package com.cryptic.util;

import com.cryptic.cache.graphics.SimpleImage;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTools {
    public static SimpleImage sceneMinimapSprite;
    public static Calendar system_calendar;
    public static Calendar server_calendar;
    public static final String[] DAYS_OF_THE_WEEK = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public static final String[][] MONTH_NAMES_ENGLISH_GERMAN = new String[][]{{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}, {"Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez"}, {"jan", "fév", "mars", "avr", "mai", "juin", "juil", "août", "sept", "oct", "nov", "déc"}, {"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"}, {"jan", "feb", "mrt", "apr", "mei", "jun", "jul", "aug", "sep", "okt", "nov", "dec"}, {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}, {"ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic"}};

    static {
        system_calendar = Calendar.getInstance();
        server_calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    }

    public DateTools() throws Throwable {
        throw new Error();
    }

    public static void set_system_calendar_time(long time) {
        system_calendar.setTime(new Date(time));
    }
    public static void set_server_calendar_time(long time) {
        server_calendar.setTime(new Date(time));
    }
    public static String format_date(long ms) {
        set_server_calendar_time(ms);
        int weekday = server_calendar.get(Calendar.DAY_OF_WEEK);
        int monthday = server_calendar.get(Calendar.DAY_OF_MONTH);
        int month = server_calendar.get(Calendar.MONTH);
        int year = server_calendar.get(Calendar.YEAR);
        int hour = server_calendar.get(Calendar.HOUR_OF_DAY);
        int minute = server_calendar.get(Calendar.MINUTE);
        int second = server_calendar.get(Calendar.SECOND);
        return DAYS_OF_THE_WEEK[weekday - 1] + ", " + monthday / 10 + monthday % 10 + "-" + MONTH_NAMES_ENGLISH_GERMAN[0][month] + "-" + year + " " + hour / 10 + hour % 10 + ":" + minute / 10 + minute % 10 + ":" + second / 10 + second % 10 + " GMT";
    }

}