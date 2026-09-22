package org.wesantal.santalicalendar.core

enum class WeekDay(val english: String, val index: Int) {
    SUNDAY("Sunday", 0),
    MONDAY("Monday", 1),
    TUESDAY("Tuesday", 2),
    WEDNESDAY("Wednesday", 3),
    THURSDAY("Thursday", 4),
    FRIDAY("Friday", 5),
    SATURDAY("Saturday", 6);

    companion object {
        fun fromIndex(index: Int): WeekDay = entries.first { it.index == index }
    }
}
