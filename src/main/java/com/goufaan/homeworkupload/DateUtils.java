package com.goufaan.homeworkupload;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    public static Calendar calendar = Calendar.getInstance();

    /**
     * 距离截止日期还有多长时间
     *
     * @param date
     * @return
     */
    public static String FormatDate(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long time = deadline - now;
        if (time >= 0){
            if (time <= ONE_HOUR)
                return time / ONE_MINUTE + "分钟后";
            else if (time <= ONE_DAY)
                return time / ONE_HOUR + "小时后";
            else{
                return time / ONE_DAY + "天 " + time % ONE_DAY / ONE_HOUR + "小时后";
            }
        }else{
            time = -time;
            if (time <= ONE_HOUR)
                return time / ONE_MINUTE + "分钟前";
            else if (time <= ONE_DAY)
                return time / ONE_HOUR + "小时前";
            else if (time <= ONE_DAY * 2)
                return "昨天";
            else if (time <= ONE_DAY * 3)
                return "前天";
            else if (time <= ONE_MONTH) {
                return time / ONE_DAY + "天前";
            } else if (time <= ONE_YEAR) {
                return time / ONE_MONTH + "月前";
            } else {
                return time / ONE_YEAR + "年前";
            }
        }
    }
}