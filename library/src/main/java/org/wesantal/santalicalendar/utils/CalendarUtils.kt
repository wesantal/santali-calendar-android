package org.wesantal.santalicalendar.utils

import org.wesantal.santalicalendar.calendar.SantaliCalendarDay
import org.wesantal.santalicalendar.calendar.SantaliCalendarDayCell
import org.wesantal.santalicalendar.calendar.SantaliCalendarMonth
import org.wesantal.santalicalendar.calendar.SantaliCalendarYear
import org.wesantal.santalicalendar.calendar.SantaliMonth
import org.wesantal.santalicalendar.core.SantaliWeekDay
import org.wesantal.santalicalendar.core.WeekDay
import org.wesantal.santalicalendar.moon.MeeusMoonCalculator
import java.util.Date

object CalendarUtils {
    private fun createCalendarDay(
        santaliDay: Int,
        currentDate: Date,
        month: SantaliMonth,
        isCurrentMonth: Boolean
    ): SantaliCalendarDay {
        val weekIndex = DateUtils.getDayOfWeek(currentDate)
        return SantaliCalendarDay(
            day = santaliDay,
            date = currentDate,
            isFirstMoonDay = santaliDay == 1,
            olChikiDay = OlChikiNumerals.toOlChikiNumeral(santaliDay),
            weekDay = WeekDay.fromIndex(weekIndex).english,
            santaliWeekDay = SantaliWeekDay.fromIndex(weekIndex).olChiki,
            isToday = DateUtils.isSameDay(currentDate, Date()),
            isAmavasya = santaliDay == month.totalDays,
            isPurnima = DateUtils.isSameDay(currentDate, month.fullMoon),
            isCurrentMonth = isCurrentMonth
        )
    }

    fun buildCalendarMonth(
        currentMonth: SantaliMonth,
        prevMonth: SantaliMonth? = null,
        nextMonth: SantaliMonth? = null
    ): SantaliCalendarMonth {
        val startDate = currentMonth.startDate
        val dayCells = mutableListOf<SantaliCalendarDayCell>()

        val startWeekDayIndex = DateUtils.getDayOfWeek(startDate)

        if (prevMonth != null && startWeekDayIndex > 0) {
            val prevMonthEnd = prevMonth.endDate
            val startDay = prevMonth.totalDays - startWeekDayIndex + 1
            for (day in startDay..prevMonth.totalDays) {
                val daysFromEnd = prevMonth.totalDays - day + 1
                val currentDate = DateUtils.addDays(prevMonthEnd, -daysFromEnd)
                dayCells.add(createCalendarDay(day, currentDate, prevMonth, false))
            }
        } else {
            for (index in 0  until startWeekDayIndex) {
                dayCells.add(null)
            }
        }

        for (day in 1..currentMonth.totalDays) {
            val currentDate = DateUtils.addDays(startDate, day - 1)
            dayCells.add(createCalendarDay(day, currentDate, currentMonth, true))
        }

        val remainder = dayCells.size % 7
        if (remainder != 0) {
            val requiredCells = 7 - remainder
            if (nextMonth != null) {
                val nextMonthStart = nextMonth.startDate
                for (day in 1..requiredCells) {
                    val currentDate = DateUtils.addDays(nextMonthStart, day - 1)
                    dayCells.add(createCalendarDay(day, currentDate, nextMonth, false))
                }
            } else {
                for (index in 0 until requiredCells) {
                    dayCells.add(null)
                }
            }
        }

        return SantaliCalendarMonth(
            id = currentMonth.id,
            name = currentMonth.name,
            roman = currentMonth.roman,
            index = currentMonth.index,
            isLeapMonth = currentMonth.isLeapMonth,
            startDate = currentMonth.startDate,
            endDate = currentMonth.endDate,
            totalDays = currentMonth.totalDays,
            newMoonDate = currentMonth.newMoon,
            fullMoonDate = currentMonth.fullMoon,
            displayEndDate = currentMonth.displayEndDate,
            days = dayCells
        )
    }

    fun buildCalendar(year: Int): SantaliCalendarYear {
        val months = MeeusMoonCalculator.getSantaliMonths(year)
        val yearMonths = months.mapIndexed { index, month ->
            buildCalendarMonth(
                month,
                months.getOrNull(index - 1),
                months.getOrNull(index + 1)
            )
        }
        val currentMonthIndex = MeeusMoonCalculator.getCalendarMonthIndex(Date())
        val startDate = months.firstOrNull()?.startDate ?: throw IllegalStateException("No months computed")
        val endDate = months.lastOrNull()?.endDate ?: throw IllegalStateException("No months computed")

        return SantaliCalendarYear(
            year = year,
            months = yearMonths,
            currentMonthIndex = currentMonthIndex,
            startDate = startDate,
            endDate = endDate
        )
    }
}
