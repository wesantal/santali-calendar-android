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
        val cells = mutableListOf<SantaliCalendarDayCell>()
        val firstDay = currentMonth.startDate

        val startWeekday = DateUtils.getDayOfWeek(firstDay)

        if (prevMonth != null) {
            val previousStart = prevMonth.startDate
            val firstPreviousDay = (prevMonth.totalDays - startWeekday) + 1

            for (day in firstPreviousDay..prevMonth.totalDays) {
                val date = DateUtils.addDays(previousStart, day - 1)
                val isPurnima = DateUtils.isSameDay(date, prevMonth.fullMoon)
                cells.add(
                    createCalendarDay(day, date, prevMonth, false)
                )
            }
        } else {
            for (i in 0 until startWeekday) {
                cells.add(null)
            }
        }

        for (day in 1..currentMonth.totalDays) {
            val date = DateUtils.addDays(firstDay, day - 1)
            val isPurnima = DateUtils.isSameDay(date, currentMonth.fullMoon)
            cells.add(
                createCalendarDay(day, date, currentMonth, true)
            )
        }

        val remainder = cells.size % 7
        if (remainder != 0 && nextMonth != null) {
            val requiredDays = 7 - remainder
            val nextStart = nextMonth.startDate

            for (day in 1..requiredDays) {
                if (day > nextMonth.totalDays) break
                val date = DateUtils.addDays(nextStart, day - 1)
                val isPurnima = DateUtils.isSameDay(date, nextMonth.fullMoon)
                cells.add(
                    createCalendarDay(day, date, nextMonth, false)
                )
            }
        }

        while (cells.size % 7 != 0) {
            cells.add(null)
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
            days = cells
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
