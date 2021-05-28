package com.kangraoo.basektlib.tools

import android.text.TextUtils
import androidx.annotation.StringDef
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc :
 */
object UTime {

    const val YMDHMS: String = "yyyy-MM-dd HH:mm:ss"

    /** 时间日期格式化到年月日时. */
    const val YMDH = "yyyy-MM-dd HH:00"

    /** 时间日期格式化到年月日. */
    const val YMD = "yyyy-MM-dd"

    /** 时间日期格式化到年月日. */
    const val YMD2 = "yyyy/MM/dd"

    /** 时间日期格式化到年月. */
    const val YM = "yyyy-MM"
    /** 时间日期格式化到年月. */
    const val YM_CH = "yyyy年MM月"

    /** 时间日期格式化到年月日时分. */
    const val YMDHM = "yyyy-MM-dd HH:mm"

    /** 时间日期格式化到年月日时分. */
    const val YMDHM_CH = "MM月dd日 HH:mm"

    /** 时间日期格式化到月日. */
    const val MD = "MM-dd"

    /** 时分秒. */
    const val HMS = "HH:mm:ss"

    /** 时分. */
    const val HM = "HH:mm"

    /******************** 时间相关常量 ********************/
    /**
     * 秒与毫秒的倍数
     */
    const val SEC = 1000

    /**
     * 分与毫秒的倍数
     */
    const val MIN = 60000

    /**
     * 时与毫秒的倍数
     */
    const val HOUR = 3600000

    /**
     * 天与毫秒的倍数
     */
    const val DAY = 86400000

    @StringDef(YMDHMS, YMDH, YMD, YMD2, YM, YM_CH, YMDHM, YMDHM_CH, MD, HMS, HM)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class FormatType

    @JvmStatic fun nowDateTime(@FormatType format: String): String {
        val date = Date()
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(date)
    }

    /**
     * 年月日创建格式化后时间
     */
    @JvmStatic fun formatDatetime(year: Int, month: Int, day: Int, @FormatType format: String): String {
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(GregorianCalendar(year, month, day).time)
    }

    /**
     * 格式化后的时间转为data
     */
    @JvmStatic fun dateFromFormatString(formatDate: String, @FormatType format: String): Date {
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.parse(formatDate)
    }

    /**
     * 毫秒转格式
     */
    @JvmStatic fun dateTimeString(milliseconds: Long, @FormatType format: String): String {
        val date = Date(milliseconds)
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(date)
    }

    /**
     * 获取北京时间
     */
    @JvmStatic fun beijingNowTimeString(@FormatType format: String): String {
        val timezone = TimeZone.getTimeZone("Asia/Shanghai")
        val date = Date(currentTimeMillis())
        val df = SimpleDateFormat(format, Locale.getDefault())
        df.setTimeZone(timezone)

        val gregorianCalendar = GregorianCalendar()
        gregorianCalendar.timeZone = timezone
        val prefix = if (gregorianCalendar.get(Calendar.AM_PM) == Calendar.AM) "上午" else "下午"

        return prefix + df.format(date)
    }
    /**
     * 获取北京时间
     */
    @JvmStatic fun getBeijingNowTime(@FormatType format: String): String {
        val timezone = TimeZone.getTimeZone("Asia/Shanghai")
        val date = Date(currentTimeMillis())
        val df = SimpleDateFormat(format, Locale.getDefault())
        df.setTimeZone(timezone)

        return df.format(date)
    }

    /**
     * 获取当前时间同 new data
     */
    @JvmStatic fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    @JvmStatic fun currentTimeSecond(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    /**
     * 根据日期获得星期
     */
    @JvmStatic fun getWeekOfDate(date: Date): String {
        val weekDaysName = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = date
        val intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        return weekDaysName[intWeek]
    }

    /**
     * 是否是一样的时间
     */
    @JvmStatic fun isSameDay(time1: Long, time2: Long): Boolean {
        return isSameDay(Date(time1), Date(time2))
    }
    /**
     * 是否是一样的时间
     */
    @JvmStatic fun isSameDay(date1: Date, date2: Date): Boolean {
        val (cal1, cal2) = toDate(date1, date2)
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(
            Calendar.DAY_OF_YEAR
        )
    }

    /**
     * 判断两个日期是否在同一周
     */
    @JvmStatic fun isSameWeekDates(date1: Date, date2: Date): Boolean {
        val (cal1, cal2) = toDate(date1, date2)
        val subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)
        return when {
            0 == subYear -> cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)
            1 == subYear && 11 == cal2.get(Calendar.MONTH) -> cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(
                Calendar.WEEK_OF_YEAR
            )
            -1 == subYear && 11 == cal1.get(Calendar.MONTH) -> cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(
                Calendar.WEEK_OF_YEAR
            )
            else -> false
        }
    }

    private inline fun toDate(date1: Date, date2: Date): Pair<Calendar, Calendar> {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        return Pair(cal1, cal2)
    }

    /**
     * 格式化时间
     */
    @JvmStatic fun stringForTime(timeMs: Int): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        return if (hours > 0) {
            String.format(
                Locale.getDefault(),
                "%d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        } else {
            String.format(
                Locale.getDefault(),
                "%02d:%02d",
                minutes,
                seconds
            )
        }
    }

    @JvmStatic fun getTimeShowString(milliseconds: Long): String? {
        val dataString: String
        val currentTime = Date(milliseconds)
        val todayStart = Calendar.getInstance()
        todayStart[Calendar.HOUR_OF_DAY] = 0
        todayStart[Calendar.MINUTE] = 0
        todayStart[Calendar.SECOND] = 0
        todayStart[Calendar.MILLISECOND] = 0
        val todaybegin = todayStart.time
        val yesterdaybegin = Date(todaybegin.time - 3600 * 24 * 1000)
        dataString = if (!currentTime.before(todaybegin)) {
            "今天"
        } else if (!currentTime.before(yesterdaybegin)) {
            "昨天"
        } else {
            val sdf =
                SimpleDateFormat("yyyy", Locale.getDefault())
            if (todayStart[Calendar.YEAR] == sdf.format(currentTime).toInt()) {
                val dateformatter =
                    SimpleDateFormat("MM-dd", Locale.getDefault())
                dateformatter.format(currentTime)
            } else {
                val dateformatter =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateformatter.format(currentTime)
            }
        }
        return dataString
    }

    /**
     * 根据当前时间判断传递的时间间隔
     *
     * @param createDate
     * @param isShowTimeDetail 日期后是否显示时分
     * @return
     */
    @JvmStatic fun getTimeText(createDate: String, isShowTimeDetail: Boolean): String? {
        var r = ""
        if (TextUtils.isEmpty(createDate)) return r
        val dateFormat: SimpleDateFormat
        if (dayInterval(createDate) == 0) { // 今天
            dateFormat = SimpleDateFormat("HH:mm")
            r = "今天 " + if (isShowTimeDetail) dateFormat.format(createDate.toLong()) else ""
        } else if (dayInterval(createDate) > 0 && dayInterval(
                createDate
            ) < 2
        ) {
            dateFormat = SimpleDateFormat("HH:mm")
            r = "昨天 " + if (isShowTimeDetail) dateFormat.format(createDate.toLong()) else ""
        } else if (dayInterval(createDate) > 1 && dayInterval(
                createDate
            ) < 3
        ) {
            dateFormat = SimpleDateFormat("HH:mm")
            r = "前天 " + if (isShowTimeDetail) dateFormat.format(createDate.toLong()) else ""
        } else { // 超出当日
            dateFormat = if (isNowYear(createDate)) {
                SimpleDateFormat(if (isShowTimeDetail) "MM-dd HH:mm" else "MM-dd")
            } else { //超出本年
                SimpleDateFormat(if (isShowTimeDetail) "yyyy-MM-dd HH:mm" else "yyyy-MM-dd")
            }
            r = dateFormat.format(createDate.toLong())
        }
        return r
    }

    /**
     * 判断是否是本年
     *
     * @param createDate
     * @return
     */
    private fun isNowYear(createDate: String): Boolean {
        val c = Calendar.getInstance()
        val nowYear = c[Calendar.YEAR].toString()
        val format = SimpleDateFormat("yyyy")
        val year = format.format(createDate.toLong())
        return nowYear == year
    }
    private const val NOT_THIS_YEAR = 1000

    /**
     * 判断指定日期跟当前日期的间隔
     *
     * @param day
     * @return
     */
    @JvmStatic fun dayInterval(day: String): Int {
        var diffDay = 0
        val pre = Calendar.getInstance()
        val predate = Date(System.currentTimeMillis())
        pre.time = predate
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd")
        try {
            val date = format.parse(format.format(day.toLong()))
            cal.time = date
        } catch (p: ParseException) {
            p.printStackTrace()
        }
        diffDay = if (cal[Calendar.YEAR] == pre[Calendar.YEAR]) {
            pre[Calendar.DAY_OF_YEAR] - cal[Calendar.DAY_OF_YEAR]
        } else {
            NOT_THIS_YEAR
        }
        return diffDay
    }
}
