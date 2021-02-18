package com.zte.msg.alarmcenter.utils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/22 12:42
 */
public class DateUtils {

//    static {
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC+8"));
//    }

    public static String formatDate(Date date) {

        return org.apache.http.client.utils.DateUtils.formatDate(date, Constants.DATE_FORMAT_PATTERN);
    }

    public static Date parseDate(String date) {

        return org.apache.http.client.utils.DateUtils.parseDate(date);
    }

    public static Timestamp zeroClockOf(long mill) {
        return new Timestamp(mill - 8 * 3600 - mill % Constants.DAY_MILL);
    }

    public static Timestamp sevenDayBefore(long mill) {
        return new Timestamp(mill - mill % Constants.DAY_MILL - Constants.DAY_MILL * 7);
    }

    public static Timestamp now() {
        return timestamp(System.currentTimeMillis());
    }

    public static Timestamp timestamp(long mill) {
        return new Timestamp(mill);
    }

    public static void main(String[] args) {
        System.out.println(zeroClockOf(System.currentTimeMillis()));
        System.out.println(sevenDayBefore(System.currentTimeMillis()));
    }
}
