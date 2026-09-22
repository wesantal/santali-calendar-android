package org.wesantal.santalicalendar.calendar

import java.util.Date

enum class SantaliMonthId(val olChiki: String, val roman: String, val index: Int, val isLeapMonth: Boolean = false) {
    MAG("ᱢᱟᱜᱽ", "Mag", 0),
    FAGUN("ᱯᱷᱟᱹᱜᱩᱱ", "Fagun", 1),
    CHAAT("ᱪᱟᱹᱛ", "Chaat", 2),
    BAISAK("ᱵᱟᱹᱭᱥᱟᱹᱠ", "Baisak", 3),
    JHENT("ᱡᱷᱮᱸᱴ", "Jhent", 4),
    ASHAL("ᱟᱥᱟᱲ", "Ashal", 5),
    SAAN("ᱥᱟᱱ", "Saan", 6),
    BHADOR("ᱵᱷᱟᱫᱚᱨ", "Bhador", 7),
    DASANY("ᱫᱟᱥᱟᱸᱭ", "Dasany", 8),
    SOHRAY("ᱥᱚᱦᱨᱟᱭ", "Sohray", 9),
    AGHAN("ᱟᱜᱷᱟᱬ", "Aaghan", 10),
    PUSH("ᱯᱩᱥ", "Pus", 11),
    SARCHA("ᱥᱟᱨᱪᱟ ᱪᱟᱸᱫᱳ", "Sarcha Chando", 12, isLeapMonth = true);

    companion object {
        private val byIndex = entries.associateBy { it.index }
        fun fromIndex(index: Int): SantaliMonthId = byIndex[index] ?: throw IllegalArgumentException("Invalid Santali month index: $index")
    }
}

data class SantaliMonth(
    val id: SantaliMonthId,
    val name: String,
    val roman: String,
    val index: Int,
    val isLeapMonth: Boolean,
    val startDate: Date,
    val endDate: Date,
    val totalDays: Int,
    val newMoon: Date,
    val fullMoon: Date,
    val displayEndDate: Date
)
