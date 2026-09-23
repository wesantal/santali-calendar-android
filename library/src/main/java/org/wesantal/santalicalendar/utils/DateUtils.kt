package org.wesantal.santalicalendar.utils

import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import org.wesantal.santalicalendar.core.DateTime

object DateUtils {
    const val MS_PER_DAY = 86400000L
    const val IST_OFFSET_MS = (5.5 * 60 * 60 * 1000).toLong()

    fun toDate(timestampMs: Long): Date = Date(timestampMs)

    fun toTimestamp(date: Date): Long = date.time

    fun formatDateString(date: Date): String = DateTime.formatDateString(date)

    fun getDayOfWeek(date: Date): Int = DateTime.getDayOfWeek(date)

    fun isSameDay(date1: Date, date2: Date): Boolean = DateTime.isSameDay(date1, date2)

    fun addDays(date: Date, days: Int): Date = DateTime.addDays(date, days)

    fun addMilliseconds(date: Date, milliseconds: Long): Date =
        DateTime.addMilliseconds(date, milliseconds)

    fun startOfDay(date: Date): Date = DateTime.startOfDay(date)

    fun endOfDay(date: Date): Date = DateTime.endOfDay(date)

    fun isLeapYear(year: Int): Boolean = DateTime.isLeapYear(year)

    fun getDaysInMonth(year: Int, month: Int): Int = DateTime.getDaysInMonth(year, month)

    fun createDate(
        year: Int,
        month: Int,
        day: Int,
        hours: Int = 0,
        minutes: Int = 0,
        seconds: Int = 0,
        milliseconds: Int = 0,
    ): Date = DateTime.createDate(year, month, day, hours, minutes, seconds, milliseconds)

    fun createUTCDate(
        year: Int,
        month: Int,
        day: Int,
        hours: Int = 0,
        minutes: Int = 0,
        seconds: Int = 0,
        milliseconds: Int = 0,
    ): Date = DateTime.createUTCDate(year, month, day, hours, minutes, seconds, milliseconds)

    fun startOfSantaliDay(date: Date): Date {
        val istMs = date.time + IST_OFFSET_MS
        val istDate = Date(istMs)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.time = istDate
        var day = cal.get(Calendar.DAY_OF_MONTH)
        if (cal.get(Calendar.HOUR_OF_DAY) < 17) {
            day -= 1
        }
        return createUTCDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), day, 11, 30, 0, 0)
    }

    fun endOfSantaliDay(date: Date): Date = Date(startOfSantaliDay(date).time + MS_PER_DAY)

    fun isSameSantaliDay(date1: Date, date2: Date): Boolean =
        startOfSantaliDay(date1).time == startOfSantaliDay(date2).time

    fun getSantaliDayNumber(date: Date, monthStart: Date, monthEnd: Date): Int {
        val target = startOfSantaliDay(date).time
        val start = monthStart.time
        val end = monthEnd.time
        if (target !in start..<end) return 0
        return ((target - start) / MS_PER_DAY).toInt() + 1
    }

    fun getSantaliDateBoundary(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        return createUTCDate(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH),
            11,
            30,
            0,
            0,
        )
    }
}
