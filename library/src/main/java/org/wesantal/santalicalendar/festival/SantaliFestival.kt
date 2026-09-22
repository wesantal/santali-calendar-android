package org.wesantal.santalicalendar.festival

import java.util.Date

sealed class SantaliFestivalRule {
    data class FixedGregorian(val month: Int, val day: Int) : SantaliFestivalRule()
    data class MoonRelative(val phase: MoonPhase, val monthId: org.wesantal.santalicalendar.calendar.SantaliMonthId, val offsetDays: Int) : SantaliFestivalRule()
}

enum class MoonPhase {
    NEW_MOON,
    FULL_MOON
}

enum class SantaliFestivalType {
    FESTIVAL,
    BIRTH_ANNIVERSARY,
    DEATH_ANNIVERSARY,
    CULTURAL,
    COMMUNITY,
    OBSERVANCE
}

data class SantaliFestivalDefinition(
    val id: String,
    val name: String,
    val roman: String,
    val type: SantaliFestivalType,
    val rule: SantaliFestivalRule,
    val description: String? = null
)

data class SantaliFestival(
    val id: String,
    val name: String,
    val roman: String,
    val monthId: org.wesantal.santalicalendar.calendar.SantaliMonthId,
    val type: SantaliFestivalType,
    val date: Date,
    val description: String? = null
)
