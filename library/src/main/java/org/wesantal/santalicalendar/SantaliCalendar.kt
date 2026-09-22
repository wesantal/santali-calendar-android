package org.wesantal.santalicalendar

import android.os.Build
import androidx.annotation.RequiresApi
import org.wesantal.santalicalendar.calendar.SantaliCalendarMonth
import org.wesantal.santalicalendar.calendar.SantaliCalendarYear
import org.wesantal.santalicalendar.calendar.SantaliDate
import org.wesantal.santalicalendar.calendar.SantaliMonth
import org.wesantal.santalicalendar.festival.FestivalData
import org.wesantal.santalicalendar.festival.MoonPhase
import org.wesantal.santalicalendar.festival.SantaliFestival
import org.wesantal.santalicalendar.festival.SantaliFestivalDefinition
import org.wesantal.santalicalendar.festival.SantaliFestivalRule
import org.wesantal.santalicalendar.moon.MeeusMoonCalculator
import org.wesantal.santalicalendar.utils.CalendarUtils
import org.wesantal.santalicalendar.core.DateTime
import org.wesantal.santalicalendar.utils.DateUtils
import java.util.Date

class SantaliCalendar {
    private val monthCache = mutableMapOf<Int, List<SantaliMonth>>()
    private val yearCache = mutableMapOf<Int, SantaliCalendarYear>()

    fun resolveFestival(definition: SantaliFestivalDefinition, year: Int): SantaliFestival {
        val months = getMonths(year)
        return when (val rule = definition.rule) {
            is SantaliFestivalRule.FixedGregorian -> {
                val monthIndex = maxOf(0, rule.month - 1)
                val date = DateUtils.createDate(year, monthIndex, rule.day)
                val month = getMonthFromDate(date)
                SantaliFestival(
                    id = definition.id,
                    name = definition.name,
                    roman = definition.roman,
                    monthId = month.id,
                    type = definition.type,
                    date = date,
                    description = definition.description
                )
            }
            is SantaliFestivalRule.MoonRelative -> {
                val month = months.firstOrNull { it.id == rule.monthId }
                    ?: throw IllegalStateException("Month ${rule.monthId} not found")
                val date = if (rule.phase == MoonPhase.FULL_MOON) {
                    DateUtils.addDays(month.fullMoon, rule.offsetDays)
                } else {
                    DateUtils.addDays(month.newMoon, rule.offsetDays)
                }
                SantaliFestival(
                    id = definition.id,
                    name = definition.name,
                    roman = definition.roman,
                    monthId = rule.monthId,
                    type = definition.type,
                    date = date,
                    description = definition.description
                )
            }
        }
    }

    fun getMonths(year: Int): List<SantaliMonth> {
        monthCache[year]?.let { return it }
        val months = MeeusMoonCalculator.getSantaliMonths(year)
        monthCache[year] = months
        return months
    }

    fun getCalendar(year: Int): SantaliCalendarYear {
        yearCache[year]?.let { return it }
        val calendar = CalendarUtils.buildCalendar(year)
        yearCache[year] = calendar
        return calendar
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(date: Date = Date()): SantaliDate = MeeusMoonCalculator.getDate(date)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSantaliToday(): SantaliDate = MeeusMoonCalculator.getToday()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getToday(): SantaliDate {
        val dt = DateUtils.endOfDay(Date())
        return MeeusMoonCalculator.getDate(dt)
    }

    fun getMonthIndex(date: Date): Int = MeeusMoonCalculator.getMonthIndex(date)

    fun getMonthFromDate(date: Date): SantaliCalendarMonth {
        val year = DateTime.getYear(date)
        val monthIndex = getMonthIndex(date)
        return getCalendar(year).months[monthIndex]
    }

}

fun SantaliCalendar.getCalendarMonthIndex(date: Date): Int {
    return MeeusMoonCalculator.getCalendarMonthIndex(date)
}

fun SantaliCalendar.getDaysInMonth(year: Int, month: Int): Int {
    val calendar = getCalendar(year)
    require(month in 1..calendar.months.size) { "Invalid month" }
    return calendar.months[month - 1].totalDays
}

fun SantaliCalendar.isLeapYear(year: Int): Boolean {
    return MeeusMoonCalculator.isSantaliLeapYear(year)
}

fun SantaliCalendar.getCurrentMonth(): SantaliCalendarMonth {
    val now = Date()
    val year = DateTime.getYear(now)
    val monthIndex = MeeusMoonCalculator.getMonthIndex(now)
    return getCalendar(year).months[monthIndex]
}

fun SantaliCalendar.getFestivals(year: Int): List<SantaliFestival> {
    val festivals = mutableListOf<SantaliFestival>()
    for (definition in FestivalData.santaliFestivals) {
        try {
            festivals.add(resolveFestival(definition, year))
        } catch (_: Exception) {
            // Skip adding festival if fails
        }
    }
    return festivals.sortedBy { it.date }
}
