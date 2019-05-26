package com.goufaan.homeworkupload;

import java.util.Calendar;

public class Misc {
    /**
     * 与当前时间比较，得到多少年，多少月，多少天前,多少小时前，多小分钟前
     *
     * @param calendar
     *            与当前时间比较的日期值
     * @return 格式化之后的字符串
     */
    public static String getTimeFormat(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        String strdate = null;
        if (year > 0) {
            strdate = year + " 年前";
        } else if (year == 0) {
            int month = cal.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
            if (month > 0) {
                strdate = month + " 月前";
            } else if (month == 0) {
                int day = cal.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH);
                if (day > 0) {
                    strdate = day + " 天前";
                } else if (day == 0) {
                    int hour = cal.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
                    if (hour > 0) {
                        strdate = hour + " 小时前";
                    } else if (hour == 0) {
                        int minute = cal.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
                        if (minute > 0) {
                            strdate = minute + " 分钟前";
                        } else if (minute == 0) {
                            strdate = "刚刚";
                        } else {
                            strdate = -minute + " 分钟后";
                        }
                    } else {
                        strdate = -hour + " 小时后";
                    }
                }else {
                    strdate = -day + " 天后";
                }
            }else {
                strdate = -month + " 月后";
            }
        } else {
            strdate = -year + " 年后";
        }
        return strdate;
    }
}
