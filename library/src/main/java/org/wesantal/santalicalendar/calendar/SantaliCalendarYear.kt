package org.wesantal.santalicalendar.calendar

import java.util.Date

data class SantaliCalendarYear(
    val year: Int,
    val startDate: Date,
    val endDate: Date,
    val currentMonthIndex: Int,
    val months: List<SantaliCalendarMonth>
)
