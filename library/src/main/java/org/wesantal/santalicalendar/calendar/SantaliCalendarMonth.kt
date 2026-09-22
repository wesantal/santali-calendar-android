package org.wesantal.santalicalendar.calendar

data class SantaliCalendarMonth(
    val id: SantaliMonthId,
    val name: String,
    val roman: String,
    val index: Int,
    val isLeapMonth: Boolean,
    val startDate: java.util.Date,
    val endDate: java.util.Date,
    val totalDays: Int,
    val newMoonDate: java.util.Date,
    val fullMoonDate: java.util.Date,
    val displayEndDate: java.util.Date,
    val days: List<SantaliCalendarDayCell>
)
