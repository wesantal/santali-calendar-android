package org.wesantal.santalicalendar.core

enum class SantaliWeekDay(val olChiki: String, val english: String, val index: Int) {
    SUNDAY("ᱥᱤᱸᱜᱤ", "Sunday", 0),
    MONDAY("ᱚᱛᱮ", "Monday", 1),
    TUESDAY("ᱵᱟᱞᱮ", "Tuesday", 2),
    WEDNESDAY("ᱥᱟᱹᱜᱩᱱ", "Wednesday", 3),
    THURSDAY("ᱥᱟᱹᱨᱫᱤ", "Thursday", 4),
    FRIDAY("ᱡᱟᱹᱨᱩᱢ", "Friday", 5),
    SATURDAY("ᱧᱩᱦᱩᱢ", "Saturday", 6);

    companion object {
        fun fromIndex(index: Int): SantaliWeekDay = entries.first { it.index == index }
    }
}
