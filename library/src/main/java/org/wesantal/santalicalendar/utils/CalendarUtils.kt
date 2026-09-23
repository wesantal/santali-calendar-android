package org.wesantal.santalicalendar.utils

import java.util.Date
import org.wesantal.santalicalendar.calendar.SantaliCalendarDay
import org.wesantal.santalicalendar.calendar.SantaliCalendarDayCell
import org.wesantal.santalicalendar.calendar.SantaliCalendarMonth
import org.wesantal.santalicalendar.calendar.SantaliCalendarYear
import org.wesantal.santalicalendar.calendar.SantaliMonth
import org.wesantal.santalicalendar.core.SantaliWeekDay
import org.wesantal.santalicalendar.core.WeekDay
import org.wesantal.santalicalendar.moon.MeeusMoonCalculator

object CalendarUtils {
    private fun createCalendarDay(
        santaliDay: Int,
        currentDate: Date,
        month: SantaliMonth,
        isCurrentMonth: Boolean,
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
            isCurrentMonth = isCurrentMonth,
        )
    }

    private const val COLUMNS = 7
    private const val MIN_ROWS = 5
    private const val TOTAL_CELLS = COLUMNS * MIN_ROWS

    fun buildCalendarMonth(
        currentMonth: SantaliMonth,
        prevMonth: SantaliMonth? = null,
        nextMonth: SantaliMonth? = null,
    ): SantaliCalendarMonth {

        val cells = mutableListOf<SantaliCalendarDayCell>()

        val firstDay = currentMonth.startDate
        val startWeekday = DateUtils.getDayOfWeek(firstDay)

        // Previous month cells
        if (prevMonth != null) {
            val firstPreviousDay = prevMonth.totalDays - startWeekday + 1

            for (day in firstPreviousDay..prevMonth.totalDays) {
                val date = DateUtils.addDays(prevMonth.startDate, day - 1)

                cells.add(
                    createCalendarDay(
                        santaliDay = day,
                        currentDate = date,
                        month = prevMonth,
                        isCurrentMonth = false,
                    )
                )
            }
        } else {
            repeat(startWeekday) { cells.add(null) }
        }

        // Current month
        for (day in 1..currentMonth.totalDays) {
            val date = DateUtils.addDays(currentMonth.startDate, day - 1)

            cells.add(
                createCalendarDay(
                    santaliDay = day,
                    currentDate = date,
                    month = currentMonth,
                    isCurrentMonth = true,
                )
            )
        }

        // Next month
        if (nextMonth != null) {
            var nextDay = 1

            while (cells.size % COLUMNS != 0) {

                if (nextDay <= nextMonth.totalDays) {
                    val date = DateUtils.addDays(nextMonth.startDate, nextDay - 1)

                    cells.add(
                        createCalendarDay(
                            santaliDay = nextDay,
                            currentDate = date,
                            month = nextMonth,
                            isCurrentMonth = false,
                        )
                    )

                    nextDay++
                } else {
                    cells.add(null)
                }
            }
        } else {
            while (cells.size % COLUMNS != 0) {
                cells.add(null)
            }
        }

        // Minimum 5 rows
        val minimumCells = MIN_ROWS * COLUMNS

        while (cells.size < minimumCells) {
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
            days = cells,
        )
    }

    fun buildCalendar(year: Int): SantaliCalendarYear {
        val months = MeeusMoonCalculator.getSantaliMonths(year)
        val yearMonths = months.mapIndexed { index, month ->
            buildCalendarMonth(month, months.getOrNull(index - 1), months.getOrNull(index + 1))
        }
        val currentMonthIndex = MeeusMoonCalculator.getCalendarMonthIndex(Date())
        val startDate =
            months.firstOrNull()?.startDate ?: throw IllegalStateException("No months computed")
        val endDate =
            months.lastOrNull()?.endDate ?: throw IllegalStateException("No months computed")

        return SantaliCalendarYear(
            year = year,
            months = yearMonths,
            currentMonthIndex = currentMonthIndex,
            startDate = startDate,
            endDate = endDate,
        )
    }
}
