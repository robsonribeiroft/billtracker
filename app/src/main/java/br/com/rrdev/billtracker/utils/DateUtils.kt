package br.com.rrdev.billtracker.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatOnPattern(pattern: String="yyyy-MMM-dd",
                         locale: Locale = Locale.getDefault(),
                         timeZone: String = "GMT-3"): String{
    val simpleDateFormat = SimpleDateFormat(pattern, locale)
    simpleDateFormat.timeZone = TimeZone.getTimeZone(timeZone)
    return simpleDateFormat.format(this)
}

fun convertTimestampToDate(timestamp: Long): Date {
    val stamp = Timestamp(timestamp)
    return Date(stamp.time)
}