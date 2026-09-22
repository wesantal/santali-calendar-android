package org.wesantal.santalicalendar.utils

object JulianDate {
    const val JULIAN_UNIX_EPOCH = 2440587.5
    const val MS_PER_DAY = 86400000.0

    fun julianDayToUnixMs(jd: Double): Double = (jd - JULIAN_UNIX_EPOCH) * MS_PER_DAY

    fun unixMsToJulianDay(ms: Double): Double = ms / MS_PER_DAY + JULIAN_UNIX_EPOCH

    fun julianDayToDate(jd: Double): java.util.Date {
        val ms = julianDayToUnixMs(jd)
        return java.util.Date(ms.toLong())
    }

    fun dateToJulianDay(date: java.util.Date): Double = unixMsToJulianDay(date.time.toDouble())
}
