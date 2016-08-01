/**
 * COPYRIGHT (C) 2010 LY. ALL RIGHTS RESERVED.
 * <p/>
 * No part of this publication may be reproduced, stored in a retrieval system,
 * or transmitted, on any form or by any means, electronic, mechanical, photocopying,
 * recording, or otherwise, without the prior written permission of 3KW.
 * <p/>
 * Created By: zzqiang
 * Created On: 2013-8-5
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 **/
package com.yyw.yhyc.order.utils;


import com.yyw.yhyc.order.helper.UtilHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    public static final String DEFAULT_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取二个日期之间的随机时间
     *
     * @param startDate
     * @param endDate
     * @return 如果开始时间大于等于结束时间 则返回null
     */
    public static Date getRandomTimeBetween(Date startDate, Date endDate) {
        if (null == startDate || null == endDate) {
            return null;
        }
        Long start = startDate.getTime();
        Long end = endDate.getTime();
        if (end - start <= 0) //结束时间必须大于开始时间
        {
            return null;
        }
        int interval = (int) (end - start) + 1;
        Random rd = new Random();
        Long rdTime = rd.nextInt(interval) + start;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(rdTime);
        return cal.getTime();
    }

    /**
     * 获取二个日期之间的随机时间
     *
     * @param startCal
     * @param endCal
     * @return 如果开始时间大于等于结束时间 则返回null
     */
    public static Date getRandomTimeBetween(Calendar startCal, Calendar endCal) {
        if (startCal == null || endCal == null) {
            return null;
        }
        return getRandomTimeBetween(startCal.getTime(), endCal.getTime());
    }

    /**
     * 根据百分比 获得二个日期之间的日期数组
     *
     * @param startDate
     * @param endDate
     * @param percents  百分比数组    之和必须等于100
     * @return 得到的日期集合 元素是 百分比元素的个数减1   如果percents之和不是100 将返回null 如果开始时间大于等于结束时间 则返回null
     */
    public static List<Date> getDatesBetweenByPercent(Date startDate, Date endDate, int... percents) {
        if (percents == null || startDate == null || null == endDate) {
            return null;
        }
        int sum = 0;
        for (int i : percents) {
            sum += i;
        }
        if (sum != 100) {
            return null;
        }
        Long start = startDate.getTime();
        Long end = endDate.getTime();
        if (end - start <= 0) //结束时间必须大于开始时间
        {
            return null;
        }
        Long interval = end - start;
        int summary = 0;
        Calendar cal = Calendar.getInstance();
        List<Date> dates = new ArrayList<Date>();
        for (int j = 0; j < percents.length; j++) {
            if (j == percents.length - 1) //最后一个不进行计算
            {
                continue;
            }
            int i = percents[j];
            summary += i;
            cal.setTimeInMillis(start + ((interval * summary) / 100));
            dates.add(cal.getTime());
        }
        return dates;
    }

    /**
     * 根据百分比 获得二个日期之间的日期数组
     *
     * @param startCal
     * @param endCal
     * @param percents 百分比数组    之和必须等于100
     * @return 得到的日期集合 元素是 百分比元素的个数减1   如果percents之和不是100 将返回null 如果开始时间大于等于结束时间 则返回null
     */
    public static List<Date> getDatesBetweenByPercent(Calendar startCal, Calendar endCal, int... percents) {
        if (startCal == null || endCal == null || null == percents) {
            return null;
        }
        return getDatesBetweenByPercent(startCal.getTime(), endCal.getTime(), percents);
    }

    /**
     * 获取两个字符串时间 换算成的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Integer getDaysBetweenStartAndEnd(String startDate, String endDate) {
        Integer days = 0;
        SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!UtilHelper.isEmpty(startDate) && !UtilHelper.isEmpty(endDate)) {
            try {
                Date d1 = LONG_DATE_FORMAT.parse(startDate);
                Date d2 = LONG_DATE_FORMAT.parse(endDate);
                days = (int) ((d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return days;
    }

    /*
     * 获取当前系统格式货时间
     */
    public static String getNowDate() {
        SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dats = LONG_DATE_FORMAT.format(cal.getTime());
        return dats;
    }

    /**
     * 获取字符串转化日期对象
     *
     * @param stringDate
     */
    public static Date getDateFromString(String stringDate) {
        Date d1 = null;
        SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            d1 = LONG_DATE_FORMAT.parse(stringDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d1;
    }

    /**
     * 获取秒
     *
     * @param stringDate
     * @return
     */
    public static Long getSeconds(String stringDate) {
        long between = 0;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date endTime = dateformat.parse(stringDate);
            between = (endTime.getTime() - new Date().getTime()) / 1000;

        } catch (ParseException e) {
            e.getMessage();
        }
        return between;
    }

    /**
     * 获取秒
     *
     * @param stringDate
     * @return
     */
    public static Long getSecondsToNow(String stringDate) {
        long between = 0;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date endTime = dateformat.parse(stringDate);
            between = (new Date().getTime() - endTime.getTime()) / 1000;

        } catch (Exception e) {
            between = 0;
        }
        return between;
    }

    public static Date formatDate(String dateString) throws ParseException {
        return formatDate(dateString, DEFAULT_FORMAT_PATTERN);
    }

    public static Date formatDate(String dateString, String formatStr) throws ParseException {
        if (formatStr == null || formatStr.length() == 0 || "null".equalsIgnoreCase(formatStr)) {
            formatStr = DEFAULT_FORMAT_PATTERN;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        return simpleDateFormat.parse(dateString);
    }

    /**
     * 两个string时间获取秒
     *
     * @param stringDate
     * @param endDate
     * @return
     */
    public static Long getSeconds(String stringDate, String endDate) throws ParseException {
        long between = 0;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = dateformat.parse(stringDate);
        Date d2 = dateformat.parse(endDate);
        between = (d2.getTime() - d1.getTime()) / 1000;
        return between;
    }

    /**
     * 日期对象转换为字符串
     *
     * @param dateTime
     */
    public static String getStringFromDate(Date dateTime) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf1.format(dateTime);
        return date;
    }


    /**
     * 日期加减n天
     *
     * @param date
     * @param days 正数加n天，负数减n天
     * @return
     */
    public static Date addDays(Date date, int days) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, days);
        return gc.getTime();
    }

    /**
     * 日期加减n月
     *
     * @param date
     * @param months 正数加n月，负数减n月
     * @return
     */
    public static Date addMonths(Date date, int months) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, months);
        return gc.getTime();
    }

    /**
     * 日期加减n天
     *
     * @param date
     * @param years 正数加n年，负数减n年
     * @return
     */
    public static Date addYears(Date date, int years) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR, years);
        return gc.getTime();
    }

    public static void main(String[] args) throws ParseException {
        /*List<Date> dates = getDatesBetweenByPercent(DateHelper.parseTime("2013-07-08 04:51:14"),DateHelper.parseTime("2013-07-08 05:13:22"),new int[]{0,20,20,20,20,20,0});
        for (Date date : dates)
		{
			System.out.println(DateHelper.formatTime(date));
		}*/
//		System.out.println(getSecondsToNow(null));
        System.out.println(formatDate("2015-11-29 12:03:12"));
        System.out.println(addDays(formatDate("2015-12-31 12:03:12"), 1));
        System.out.println(addMonths(formatDate("2015-12-31 12:03:12"), 1));
        System.out.println(addYears(formatDate("2015-12-31 12:03:12"), 1));
    }


}

