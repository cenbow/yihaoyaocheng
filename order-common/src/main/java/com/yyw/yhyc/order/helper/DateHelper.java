package com.yyw.yhyc.order.helper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shiyongxi on 2016/7/28.
 */
public class DateHelper {
    public static final String DATE_DIVISION = "-";
    public static final String TIME_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_DEFAULT = "yyyy-MM-dd";
    public static final String DATA_PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String TIME_PATTERN_HHMMSS = "HH:mm:ss";
    public static final int SECOND = 1000;
    public static final int MINUTE = 60000;
    public static final int HOUR = 3600000;
    public static final long DAY = 86400000L;


    public static Date now() {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        return currDate;
    }

    public static Timestamp nowTimestamp() {
        Calendar cal = Calendar.getInstance();
        return new Timestamp(cal.getTimeInMillis());
    }

    public static String nowString() {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        return formatDate(currDate);
    }

    public static String nowString(String pattern) {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        return format(currDate, pattern);
    }

    public static Date parseDate(String dateValue) {
        return parse(dateValue, "yyyy-MM-dd");
    }

    public static Date parseTime(String dateValue) {
        return parse(dateValue, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String dateValue, String pattern) {
        if(!UtilHelper.isEmpty(dateValue) && !UtilHelper.isEmpty(pattern)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            try {
                return dateFormat.parse(dateValue);
            } catch (ParseException var4) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String formatDate(Date d) {
        return format(d, "yyyy-MM-dd");
    }

    public static String formatTime(Date t) {
        return format(t, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date d, String pattern) {
        if(d == null) {
            return null;
        } else {
            SimpleDateFormat dateFromat = new SimpleDateFormat(pattern);
            return dateFromat.format(d);
        }
    }

    public static Date dateAddMonth(Date date, int month) {
        if(date == null) {
            return now();
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(2, month);
            return c.getTime();
        }
    }

    public static Date dateAddYear(Date date, int year) {
        if(date == null) {
            return now();
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(1, year);
            return c.getTime();
        }
    }

    public static Date dateAddDay(Date date, int day) {
        if(date == null) {
            return now();
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(5, day);
            return c.getTime();
        }
    }

    public static Date dateAddHour(Date date, int hour) {
        if(date == null) {
            return now();
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(10, hour);
            return c.getTime();
        }
    }

    public static long getMillis(Date date) {
        if(date == null) {
            return 0L;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        }
    }

    public static int differenceOnDays(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            long l1 = getMillis(date1);
            long l2 = getMillis(date2);
            return Integer.parseInt(Long.toString((l2 - l1) / 86400000L));
        } else {
            return 0;
        }
    }

    public static int differenceOnMonth(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            boolean difYear = false;
            boolean difMonth = false;
            int year1 = getDateYear(date1);
            int year2 = getDateYear(date2);
            int month1 = getMonthOnDate(date1);
            int month2 = getMonthOnDate(date2);
            int difYear1 = (year2 - year1) * 12;
            int difMonth1 = month2 - month1;
            return Math.abs(difYear1 + difMonth1);
        } else {
            return 0;
        }
    }

    public static int getMonthOnDate(Date date) {
        if(date == null) {
            return 0;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(2);
        }
    }

    public static int differenceOnYear(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            int year1 = getDateYear(date1);
            int year2 = getDateYear(date2);
            return Math.abs(year1 - year2);
        } else {
            return 0;
        }
    }

    public static int getDateYear(Date date) {
        if(date == null) {
            return 0;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(1);
        }
    }

    public static int getDayOfMonth(Date date) {
        if(date == null) {
            return 0;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(5);
        }
    }

    public static int compareDate(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            return cal1.compareTo(cal2);
        } else {
            return -1;
        }
    }
}
