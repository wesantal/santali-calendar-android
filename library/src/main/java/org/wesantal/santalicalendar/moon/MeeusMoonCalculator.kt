package org.wesantal.santalicalendar.moon

import android.os.Build
import androidx.annotation.RequiresApi
import org.wesantal.santalicalendar.calendar.SantaliMonth
import org.wesantal.santalicalendar.calendar.SantaliMonthId
import org.wesantal.santalicalendar.core.DateTime
import org.wesantal.santalicalendar.core.SantaliWeekDay
import org.wesantal.santalicalendar.utils.DateUtils
import java.util.Date
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.sin

object MeeusMoonCalculator {
    const val MS_PER_DAY = 86400000L
    const val SECONDS_PER_DAY = 86400.0
    const val JULIAN_UNIX_EPOCH = 2440587.5
    const val IST_OFFSET_MS = (5.5 * 60 * 60 * 1000).toLong()
    const val METONIC_CYCLE_START = 2026
    val METONIC_LEAP_POS = setOf(1, 4, 7, 9, 12, 15, 18)
    const val ANCHOR_NM_MS = 1674334380000L // 2023-01-22T02:23:00+05:30

    private val SANTALI_MONTHS_BASE = listOf(
        SantaliMonthId.MAG,
        SantaliMonthId.FAGUN,
        SantaliMonthId.CHAAT,
        SantaliMonthId.BAISAK,
        SantaliMonthId.JHENT,
        SantaliMonthId.ASHAL,
        SantaliMonthId.SAAN,
        SantaliMonthId.BHADOR,
        SantaliMonthId.DASANY,
        SantaliMonthId.SOHRAY,
        SantaliMonthId.AGHAN,
        SantaliMonthId.PUSH,
        SantaliMonthId.SARCHA
    )

    private val newMoonChain = mutableListOf(ANCHOR_NM_MS)

    private fun degToRad(degrees: Double): Double = degrees * Math.PI / 180

    private fun normalizeDegrees(degrees: Double): Double {
        var value = degrees % 360
        if (value < 0) value += 360
        return value
    }

    private fun sinDeg(degrees: Double): Double = sin(degToRad(degrees))

    fun cosDeg(degrees: Double): Double = cos(degToRad(degrees))

    private fun julianDayToUnixMs(jd: Double): Double = kotlin.math.round((jd - JULIAN_UNIX_EPOCH) * MS_PER_DAY)

    fun unixMsToJulianDay(ms: Double): Double = ms / MS_PER_DAY + JULIAN_UNIX_EPOCH

    private fun getApproximateK(ms: Long): Double {
        val date = Date(ms)
        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        cal.time = date
        val year = cal.get(java.util.Calendar.YEAR) + cal.get(java.util.Calendar.MONTH) / 12.0
        return (year - 2000) * 12.3685
    }

    private data class LunarArguments(
        val t: Double,
        val t2: Double,
        val t3: Double,
        val t4: Double,
        val m: Double,
        val mp: Double,
        val f: Double,
        val om: Double,
        val e: Double
    )

    private fun getLunarArguments(k: Double): LunarArguments {
        val t = k / 1236.85
        val t2 = t * t
        val t3 = t2 * t
        val t4 = t3 * t

        val m = normalizeDegrees(2.5534 + 29.1053567 * k - 0.0000014 * t2 - 0.00000011 * t3)
        val mp = normalizeDegrees(201.5643 + 385.81693528 * k + 0.0107582 * t2 + 0.00001238 * t3 - 0.000000058 * t4)
        val f = normalizeDegrees(160.7108 + 390.67050284 * k - 0.0016118 * t2 - 0.00000227 * t3 + 0.000000011 * t4)
        val om = normalizeDegrees(124.7746 - 1.56375588 * k + 0.0020672 * t2 + 0.00000215 * t3)
        val e = 1 - 0.002516 * t - 0.0000074 * t2

        return LunarArguments(t, t2, t3, t4,m, mp, f, om, e)
    }

    private fun planetaryCorrection(t: Double, k: Double): Double {
        val a1 = 299.77 + 0.107408 * k - 0.000325 * t * t
        val a2 = 251.88 + 0.016321 * k
        val a3 = 251.83 + 26.651886 * k
        val a4 = 349.42 + 36.412478 * k
        val a5 = 84.66 + 18.206239 * k
        val a6 = 141.74 + 53.303771 * k
        val a7 = 207.14 + 2.453732 * k
        val a8 = 154.84 + 7.30686 * k
        val a9 = 34.52 + 27.261239 * k
        val a10 = 207.19 + 0.121824 * k
        val a11 = 291.34 + 1.844379 * k
        val a12 = 161.72 + 24.198154 * k
        val a13 = 239.56 + 25.513099 * k
        val a14 = 331.55 + 3.592518 * k

        return 0.000325 * sinDeg(a1) +
                0.000165 * sinDeg(a2) +
                0.000164 * sinDeg(a3) +
                0.000126 * sinDeg(a4) +
                0.00011 * sinDeg(a5) +
                0.000062 * sinDeg(a6) +
                0.00006 * sinDeg(a7) +
                0.000056 * sinDeg(a8) +
                0.000047 * sinDeg(a9) +
                0.000042 * sinDeg(a10) +
                0.00004 * sinDeg(a11) +
                0.000037 * sinDeg(a12) +
                0.000035 * sinDeg(a13) +
                0.000023 * sinDeg(a14)
    }

    private fun newMoonCorrection(args: LunarArguments): Double {
        val m = args.m
        val mp = args.mp
        val f = args.f
        val om = args.om
        val e = args.e
        return -0.4072 * sinDeg(mp) +
                0.17241 * e * sinDeg(m) +
                0.01608 * sinDeg(2 * mp) +
                0.01039 * sinDeg(2 * f) +
                0.00739 * e * sinDeg(mp - m) -
                0.00514 * e * sinDeg(mp + m) +
                0.00208 * e * e * sinDeg(2 * m) -
                0.00111 * sinDeg(mp - 2 * f) -
                0.00057 * sinDeg(mp + 2 * f) +
                0.00056 * e * sinDeg(2 * mp + m) -
                0.00042 * sinDeg(3 * mp) +
                0.00042 * e * sinDeg(m + 2 * f) +
                0.00038 * e * sinDeg(m - 2 * f) -
                0.00024 * e * sinDeg(2 * mp - m) -
                0.00017 * sinDeg(om) -
                0.00007 * sinDeg(mp + 2 * m) +
                0.00004 * sinDeg(2 * mp - 2 * f) +
                0.00004 * sinDeg(3 * m) +
                0.00003 * sinDeg(mp + m - 2 * f) +
                0.00003 * sinDeg(2 * mp + 2 * f) -
                0.00003 * sinDeg(mp + m + 2 * f) +
                0.00003 * sinDeg(mp - m + 2 * f) -
                0.00002 * sinDeg(mp - m - 2 * f) -
                0.00002 * sinDeg(3 * mp + m) +
                0.00002 * sinDeg(4 * mp)
    }

    private fun fullMoonCorrection(args: LunarArguments): Double {
        val m = args.m
        val mp = args.mp
        val f = args.f
        val om = args.om
        val e = args.e
        return -0.40614 * sinDeg(mp) +
                0.17302 * e * sinDeg(m) +
                0.01614 * sinDeg(2 * mp) +
                0.01043 * sinDeg(2 * f) +
                0.00734 * e * sinDeg(mp - m) -
                0.00515 * e * sinDeg(mp + m) +
                0.00209 * e * e * sinDeg(2 * m) -
                0.00111 * sinDeg(mp - 2 * f) -
                0.00057 * sinDeg(mp + 2 * f) +
                0.00056 * e * sinDeg(2 * mp + m) -
                0.00042 * sinDeg(3 * mp) +
                0.00042 * e * sinDeg(m + 2 * f) +
                0.00038 * e * sinDeg(m - 2 * f) -
                0.00024 * e * sinDeg(2 * mp - m) -
                0.00017 * sinDeg(om) -
                0.00007 * sinDeg(mp + 2 * m) +
                0.00004 * sinDeg(2 * mp - 2 * f) +
                0.00004 * sinDeg(3 * m) +
                0.00003 * sinDeg(mp + m - 2 * f) +
                0.00003 * sinDeg(2 * mp + 2 * f) -
                0.00003 * sinDeg(mp + m + 2 * f) +
                0.00003 * sinDeg(mp - m + 2 * f) -
                0.00002 * sinDeg(mp - m - 2 * f) -
                0.00002 * sinDeg(3 * mp + m) +
                0.00002 * sinDeg(4 * mp)
    }

    private fun getBaseJDE(k: Double): Double {
        val t = k / 1236.85
        val t2 = t * t
        val t3 = t2 * t
        val t4 = t3 * t
        return 2451550.09765 +
                29.530588853 * k +
                0.0001337 * t2 -
                0.00000015 * t3 +
                0.00000000073 * t4
    }

    fun getNewMoon(k: Long): Long {
        val kDouble = k.toDouble()
        val args = getLunarArguments(kDouble)
        var jde = getBaseJDE(kDouble) + newMoonCorrection(args)
        jde += planetaryCorrection(args.t, kDouble)
        val approximateYear = 2000 + kDouble / 12.3685
        val deltaT = DeltaT.calculate(approximateYear)
        jde -= deltaT / SECONDS_PER_DAY
        return julianDayToUnixMs(jde).toLong()
    }

    fun getFullMoon(k: Long): Long {
        val kFull = floor(k.toDouble()) + 0.5
        val args = getLunarArguments(kFull)
        var jde = getBaseJDE(kFull) + fullMoonCorrection(args)
        jde += planetaryCorrection(args.t, kFull)
        val approximateYear = 2000 + kFull / 12.3685
        val deltaT = DeltaT.calculate(approximateYear)
        jde -= deltaT / SECONDS_PER_DAY
        return julianDayToUnixMs(jde).toLong()
    }

    fun getFullMoonBetween(startMs: Long, endMs: Long): Long? {
        if (endMs <= startMs) return null

        val mid = (startMs + endMs) / 2
        val kApprox = getApproximateK(mid)
        var best: Long? = null
        var bestDiff = Long.MAX_VALUE
        val center = floor(kApprox).toLong()

        for (k in (center - 2)..(center + 3)) {
            val fullMoon = getFullMoon(k)
            if (fullMoon in startMs until endMs) {
                val diff = abs(fullMoon - mid)
                if (diff < bestDiff) {
                    bestDiff = diff
                    best = fullMoon
                }
            }
        }
        return best
    }

    fun getChandradarshan(newMoonMs: Long): Long {
        val istMs = newMoonMs + IST_OFFSET_MS
        val istDate = Date(istMs)
        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        cal.time = istDate
        val hour = cal.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = cal.get(java.util.Calendar.MINUTE)
        val second = cal.get(java.util.Calendar.SECOND)
        val millisecond = cal.get(java.util.Calendar.MILLISECOND)
        val totalMinutes = hour * 60.0 + minute + second / 60.0 + millisecond / 60000.0

        val istMidnight = istMs - (istMs % MS_PER_DAY)
        val cutoff = istMidnight + 17 * 60 * 60 * 1000L

        return if (totalMinutes < 17 * 60) {
            cutoff - IST_OFFSET_MS
        } else {
            cutoff + MS_PER_DAY - IST_OFFSET_MS
        }
    }

    fun isSantaliLeapYear(year: Int): Boolean {
        val position = (((year - METONIC_CYCLE_START) % 19 + 19) % 19) + 1
        return METONIC_LEAP_POS.contains(position)
    }

    private fun extendNewMoonChain(untilMs: Long): Long {
        var last = newMoonChain.last()

        while (last < untilMs + 60 * MS_PER_DAY) {
            val searchFrom = last + 25 * MS_PER_DAY
            val kApprox = getApproximateK(searchFrom)
            val center = floor(kApprox).toLong()
            var best: Long? = null

            for (k in (center - 2)..(center + 4)) {
                val nm = getNewMoon(k)
                if (nm > last + 20 * MS_PER_DAY && nm < last + 40 * MS_PER_DAY) {
                    if (best == null || nm < best) {
                        best = nm
                    }
                }
            }

            if (best == null) break
            if (best <= last) break

            newMoonChain.add(best)
            last = best
        }
        return last
    }

    fun getMagStartMoon(year: Int): Long {
        var index = 0
        for (y in 2023 until year) {
            index += if (isSantaliLeapYear(y)) 13 else 12
        }

        while (newMoonChain.size <= index) {
            val last = newMoonChain.last()
            extendNewMoonChain(last + 40 * MS_PER_DAY)
        }
        return newMoonChain[index]
    }

    fun getNextNewMoon(afterMs: Long): Long {
        val kApprox = getApproximateK(afterMs)
        val center = floor(kApprox).toLong()
        var best: Long? = null

        for (k in (center - 1)..(center + 4)) {
            val nm = getNewMoon(k)
            if (nm > afterMs && (best == null || nm < best)) {
                best = nm
            }
        }

        return best ?: getNewMoon(kApprox.toLong() + 2)
    }

    fun getPurnimaDayNum(startMs: Long, endMs: Long, fullMoonMs: Long): Int {
        if (endMs <= startMs) return 1
        val numDays = round((endMs - startMs).toDouble() / MS_PER_DAY).toInt()
        if (numDays <= 0) return 1
        val dayDuration = (endMs - startMs).toDouble() / numDays
        var dayNum = floor((fullMoonMs - startMs).toDouble() / dayDuration).toInt() + 1
        dayNum = dayNum.coerceIn(1, numDays)
        return dayNum
    }

    fun getKunamiDate(startMs: Long, endMs: Long, fullMoonMs: Long): Date {
        val dayNum = getPurnimaDayNum(startMs, endMs, fullMoonMs)
        val numDays = round((endMs - startMs).toDouble() / MS_PER_DAY).toInt()
        val dayDuration = (endMs - startMs).toDouble() / numDays
        val kunamiStart = startMs + (dayNum - 1) * dayDuration
        return Date(kunamiStart.toLong())
    }

    fun getSantaliMonths(year: Int): List<SantaliMonth> {
        val totalMonths = if (isSantaliLeapYear(year)) 13 else 12
        val months = mutableListOf<SantaliMonth>()
        var currentNewMoon = getMagStartMoon(year)

        for (index in 0 until totalMonths) {
            val startMs = getChandradarshan(currentNewMoon)
            val nextNM = getNextNewMoon(currentNewMoon + 25 * MS_PER_DAY)
            val endMs = getChandradarshan(nextNM)
            val fullMoonMs = getFullMoonBetween(startMs, endMs)
            val monthId = SANTALI_MONTHS_BASE[index]

            val totalDays = round((endMs - startMs).toDouble() / MS_PER_DAY).toInt()
            val dayDuration = (endMs - startMs).toDouble() / totalDays

            val actualFullMoonMs = if (fullMoonMs != null) {
                fullMoonMs
            } else {
                startMs + (totalDays / 2.0 * MS_PER_DAY).toLong()
            }
            val kunamiDay = getPurnimaDayNum(startMs, endMs, actualFullMoonMs)
            val kunamiStartMs = startMs + (kunamiDay - 1) * dayDuration

            months.add(
                SantaliMonth(
                    id = monthId,
                    name = monthId.olChiki,
                    roman = monthId.roman,
                    index = index,
                    isLeapMonth = index == 12,
                    startDate = Date(startMs),
                    newMoon = Date(startMs),
                    fullMoon = Date(kunamiStartMs.toLong()),
                    endDate = Date(endMs),
                    totalDays = totalDays,
                    displayEndDate = Date(endMs - MS_PER_DAY)
                )
            )

            currentNewMoon = nextNM
        }
        return months
    }

    fun getMonthIndex(date: Date): Int {
        val targetMs = date.time
        val cal = java.util.Calendar.getInstance()
        cal.time = date
        val year = cal.get(java.util.Calendar.YEAR)

        var months = getSantaliMonths(year)
        for (month in months) {
            val start = month.startDate.time
            val end = month.endDate.time
            if (targetMs in start..<end) return month.index
        }

        months = getSantaliMonths(year - 1)
        for (month in months) {
            val start = month.startDate.time
            val end = month.endDate.time
            if (targetMs in start..<end) return month.index
        }

        months = getSantaliMonths(year + 1)
        for (month in months) {
            val start = month.startDate.time
            val end = month.endDate.time
            if (targetMs in start..<end) return month.index
        }

        return 0
    }

    fun getMonthFromIndex(index: Int): SantaliMonth {
        val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val months = getSantaliMonths(year)
        return months.firstOrNull { it.index == index }
            ?: throw IllegalArgumentException("Invalid Santali month index: $index")
    }

    fun getSantaliMonth(date: Date = Date()): SantaliMonth {
        val targetMs = date.time
        val cal = java.util.Calendar.getInstance()
        cal.time = date
        val year = cal.get(java.util.Calendar.YEAR)

        val months = getSantaliMonths(year)
        val current = months.firstOrNull {
            targetMs >= it.startDate.time && targetMs < it.endDate.time
        }
        if (current != null) return current

        val previous = getSantaliMonths(year - 1)
        val previousMonth = previous.firstOrNull {
            targetMs >= it.startDate.time && targetMs < it.endDate.time
        }
        if (previousMonth != null) return previousMonth

        return getMonthFromIndex(getMonthIndex(date))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(date: Date = Date()): org.wesantal.santalicalendar.calendar.SantaliDate {
        val targetMs = date.time
        val cal = java.util.Calendar.getInstance()
        cal.time = date
        val gregorianYear = cal.get(java.util.Calendar.YEAR)

        var months = getSantaliMonths(gregorianYear)
        var month = months.firstOrNull {
            targetMs >= it.startDate.time && targetMs < it.endDate.time
        }

        if (month == null) {
            months = getSantaliMonths(gregorianYear - 1)
            month = months.firstOrNull {
                targetMs >= it.startDate.time && targetMs < it.endDate.time
            }
        }

        if (month == null) {
            months = getSantaliMonths(gregorianYear + 1)
            month = months.firstOrNull {
                targetMs >= it.startDate.time && targetMs < it.endDate.time
            }
        }

        if (month == null) {
            throw IllegalStateException("Unable to determine Santali date for ${date.toInstant()}")
        }

        val startMs = month.startDate.time
        val endMs = month.endDate.time
        val totalDays = round((endMs - startMs).toDouble() / MS_PER_DAY).toInt()
        val dayDuration = (endMs - startMs).toDouble() / totalDays
        val day = ((targetMs - startMs) / dayDuration).toInt() + 1
        val clampedDay = day.coerceIn(1, totalDays)

        val isPurnima = DateUtils.formatDateString(date) == DateUtils.formatDateString(month.fullMoon)
        val isAmavasya = DateUtils.isSameDay(date, month.newMoon)

        return org.wesantal.santalicalendar.calendar.SantaliDate(
            day = clampedDay,
            date = date,
            weekDay = SantaliWeekDay.fromIndex(DateUtils.getDayOfWeek(date)).olChiki,
            month = month,
            isPurnima = isPurnima,
            isAmavasya = isAmavasya,
            isLeapMonth = month.isLeapMonth,
            isFirstMoonDay = clampedDay == 1
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getToday(): org.wesantal.santalicalendar.calendar.SantaliDate = getDate(Date())

    fun getSantaliDayStart(date: Date): Date {
        val cal = java.util.Calendar.getInstance()
        cal.time = date
        return DateTime.createUTCDate(
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH),
            cal.get(java.util.Calendar.DAY_OF_MONTH),
            11, 30, 0, 0
        )
    }

    fun getCalendarMonthIndex(date: Date): Int {
        val target = getSantaliDayStart(date)
        val targetMs = target.time
        val cal = java.util.Calendar.getInstance()
        cal.time = date
        val year = cal.get(java.util.Calendar.YEAR)

        var months = getSantaliMonths(year)
        for (month in months) {
            if (targetMs >= month.startDate.time && targetMs < month.endDate.time) return month.index
        }

        months = getSantaliMonths(year - 1)
        for (month in months) {
            if (targetMs >= month.startDate.time && targetMs < month.endDate.time) return month.index
        }

        months = getSantaliMonths(year + 1)
        for (month in months) {
            if (targetMs >= month.startDate.time && targetMs < month.endDate.time) return month.index
        }

        return 0
    }

    fun getNewMoonsForYear(year: Int): List<Long> {
        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        cal.set(year, 0, 1, 0, 0, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(year + 1, 0, 1, 0, 0, 0)
        val end = cal.timeInMillis

        val kStart = floor(getApproximateK(start)).toLong() - 2
        val kEnd = ceil(getApproximateK(end)).toLong() + 2

        val result = mutableListOf<Long>()
        for (k in kStart..kEnd) {
            val nm = getNewMoon(k)
            if (nm in start until end) {
                result.add(nm)
            }
        }
        return result.sorted()
    }

    fun getFullMoonsForYear(year: Int): List<Long> {
        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        cal.set(year, 0, 1, 0, 0, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(year + 1, 0, 1, 0, 0, 0)
        val end = cal.timeInMillis

        val kStart = floor(getApproximateK(start)).toLong() - 2
        val kEnd = ceil(getApproximateK(end)).toLong() + 2

        val result = mutableListOf<Long>()
        for (k in kStart..kEnd) {
            val fm = getFullMoon(k)
            if (fm in start until end) {
                result.add(fm)
            }
        }
        return result.sorted()
    }
}
