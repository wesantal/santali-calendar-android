package org.wesantal.santalicalendar.core

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTime {
    const val MS_PER_DAY = 86400000L
    const val IST_OFFSET_MS = 5.5 * 60 * 60 * 1000
    const val SECONDS_PER_DAY = 86400.0
    const val JULIAN_UNIX_EPOCH = 2440587.5

    fun toDate(timestampMs: Long): Date = Date(timestampMs)

    fun toTimestamp(date: Date): Long = date.time

    fun cloneDate(date: Date): Date = Date(date.time)

    fun isValidDate(date: Date): Boolean = try { date.time; true } catch (_: Exception) { false }

    fun formatDateString(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    fun getDayOfWeek(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_WEEK) - 1
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun addDays(date: Date, days: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DAY_OF_MONTH, days)
        return cal.time
    }

    fun addMilliseconds(date: Date, milliseconds: Long): Date = Date(date.time + milliseconds)

    fun startOfDay(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    fun endOfDay(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.time
    }

    fun getYear(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.YEAR)
    }

    fun getMonth(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.MONTH)
    }

    fun getDay(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    fun isLeapYear(year: Int): Boolean = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

    fun getDaysInMonth(year: Int, month: Int): Int {
        val cal = Calendar.getInstance()
        cal.set(year, month, 1)
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun createDate(year: Int, month: Int, day: Int, hours: Int = 0, minutes: Int = 0, seconds: Int = 0, milliseconds: Int = 0): Date {
        val cal = Calendar.getInstance()
        cal.set(year, month, day, hours, minutes, seconds)
        cal.set(Calendar.MILLISECOND, milliseconds)
        return cal.time
    }

    fun createUTCDate(year: Int, month: Int, day: Int, hours: Int = 0, minutes: Int = 0, seconds: Int = 0, milliseconds: Int = 0): Date {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(year, month, day, hours, minutes, seconds)
        cal.set(Calendar.MILLISECOND, milliseconds)
        return cal.time
    }

    fun differenceInMilliseconds(laterDate: Date, earlierDate: Date): Long = laterDate.time - earlierDate.time

    fun diffInDays(earlierDate: Date, laterDate: Date): Int = ((laterDate.time - earlierDate.time) / MS_PER_DAY).toInt()

    fun toISTDate(ms: Long): Date = Date(ms + IST_OFFSET_MS.toLong())
}
