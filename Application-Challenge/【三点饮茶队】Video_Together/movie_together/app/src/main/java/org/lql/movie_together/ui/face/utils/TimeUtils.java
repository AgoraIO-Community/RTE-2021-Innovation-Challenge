package org.lql.movie_together.ui.face.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by v_shishuaifeng on 2020/2/6.
 */

public class TimeUtils {

    /**
     * 将时间戳转换为星期
     *
     * @param time 时间戳
     * @return
     */
    public static String getWeek(Date time) {

        Calendar cd = Calendar.getInstance();
        cd.setTime(time);
        int week = cd.get(Calendar.DAY_OF_WEEK);

        String weekString;
        switch (week) {
            case Calendar.SUNDAY:
                weekString = "星期日";
                break;
            case Calendar.MONDAY:
                weekString = "星期一";
                break;
            case Calendar.TUESDAY:
                weekString = "星期二";
                break;
            case Calendar.WEDNESDAY:
                weekString = "星期三";
                break;
            case Calendar.THURSDAY:
                weekString = "星期四";
                break;
            case Calendar.FRIDAY:
                weekString = "星期五";
                break;
            default:
                weekString = "星期六";
                break;

        }

        return weekString;
    }

    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getTimeShort(Date time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(time);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @param time 时间戳
     * @return 返回短时间格式 yyyy-MM-dd
     */
    public static String getStringDateShort(Date time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(time);
        return dateString;
    }
}
