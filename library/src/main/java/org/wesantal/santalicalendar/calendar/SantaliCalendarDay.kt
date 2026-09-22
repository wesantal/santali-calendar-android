package org.wesantal.santalicalendar.calendar

import java.util.Date

data class SantaliCalendarDay(
    val day: Int,
    val date: Date,
    val weekDay: String,
    val olChikiDay: String,
    val isToday: Boolean,
    val santaliWeekDay: String,
    val isAmavasya: Boolean,
    val isPurnima: Boolean,
    val isFirstMoonDay: Boolean,
    val isCurrentMonth: Boolean
)

typealias SantaliCalendarDayCell = SantaliCalendarDay?
