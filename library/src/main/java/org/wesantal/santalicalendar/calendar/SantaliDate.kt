package org.wesantal.santalicalendar.calendar

import java.util.Date

data class SantaliDate(
    val day: Int,
    val date: Date,
    val weekDay: String,
    val month: SantaliMonth,
    val isPurnima: Boolean,
    val isAmavasya: Boolean,
    val isLeapMonth: Boolean,
    val isFirstMoonDay: Boolean
)
