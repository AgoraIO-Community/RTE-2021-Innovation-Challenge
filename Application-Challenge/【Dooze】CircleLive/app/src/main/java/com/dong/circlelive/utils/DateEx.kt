package com.dong.circlelive.utils

import android.content.Context
import android.text.format.DateUtils
import androidx.annotation.MainThread
import com.dong.circlelive.R
import com.dong.circlelive.appContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by dooze on 2021/5/24  5:31 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

val yyyyMMddDateFormat = SimpleDateFormat("yyyy-MM-dd")

@MainThread
fun getDateString(context: Context = appContext, time: Long, displayAgoInText: Boolean = true): String {
    if (time <= 0L) {
        return ""
    }
    val currentTime = System.currentTimeMillis()
    val offset = currentTime - time
    return when {
        offset in -DateUtils.HOUR_IN_MILLIS..DateUtils.MINUTE_IN_MILLIS -> {
            context.getString(R.string.time_string_just_now)
        }
        offset >= DateUtils.MINUTE_IN_MILLIS
                && offset < DateUtils.HOUR_IN_MILLIS -> {
            val quantity = (offset / DateUtils.MINUTE_IN_MILLIS).toInt()
            context.resources.getQuantityString(
                if (displayAgoInText) R.plurals.time_string_minutes_ago else R.plurals.time_string_minutes_format,
                quantity,
                quantity
            )
        }
        offset >= DateUtils.HOUR_IN_MILLIS
                && offset < DateUtils.DAY_IN_MILLIS -> {
            val quantity = (offset / DateUtils.HOUR_IN_MILLIS).toInt()
            context.resources.getQuantityString(
                if (displayAgoInText) R.plurals.time_string_hours_ago else R.plurals.time_string_hours_format,
                quantity,
                quantity
            )
        }
        offset >= DateUtils.DAY_IN_MILLIS
                && offset < 5 * DateUtils.DAY_IN_MILLIS -> {
            val quantity = (offset / DateUtils.DAY_IN_MILLIS).toInt()
            context.resources.getQuantityString(
                if (displayAgoInText) R.plurals.time_string_days_ago else R.plurals.time_string_days_format,
                quantity,
                quantity
            )
        }
        else -> {
            yyyyMMddDateFormat.format(time)
        }
    }
}

fun Date.dateTime(): String = getDateString(time = time)