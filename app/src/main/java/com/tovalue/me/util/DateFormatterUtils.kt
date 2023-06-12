package com.tovalue.me.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

enum class DateFormat(val value: String) {
    dateTimeFormat1(DateFormatterUtils.DATE_TIME_FORMAT_1),
    dateTimeFormat2(DateFormatterUtils.DATE_TIME_FORMAT_2),
    dateFormat1(DateFormatterUtils.DATE_FORMAT_1),
    dateFormat2(DateFormatterUtils.DATE_FORMAT_2),
    dateFormat3(DateFormatterUtils.DATE_FORMAT_3),
    timeFormat1(DateFormatterUtils.TIME_FORMAT_1),
    dateFormat4(DateFormatterUtils.DATE_FORMAT_4)
}

object DateFormatterUtils {
    const val DATE_TIME_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val DATE_TIME_FORMAT_2 = "dd MMM yyyy hh:mm a"
    const val DATE_FORMAT_1 = "dd MMM yyyy"
    const val DATE_FORMAT_2 = "yyyy-MM-dd"
    const val DATE_FORMAT_3 = "MMMM dd"
    const val DATE_FORMAT_4 = "MMM dd yyyy"
    const val TIME_FORMAT_1 = "hh:mm a"

    @SuppressLint("SimpleDateFormat")
    fun formatDateFromString(timeStr: String, dateFormat: DateFormat): Date? {
        return try {
            SimpleDateFormat(dateFormat.value).parse(timeStr)
        } catch (e: ParseException) {
            null
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFromMillis(milliSeconds: Long, dateFormat: String): String? {
        return try {
            SimpleDateFormat(dateFormat).format(milliSeconds)
        } catch (e: ParseException) {
            null
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateToString(date: Date, dateFormat: DateFormat): String? {
        return try {
            SimpleDateFormat(dateFormat.value).format(date)
        } catch (e: ParseException) {
            null
        }
    }
}