package org.wesantal.santalicalendar.utils

object OlChikiNumerals {
    private val OL_CHIKI_DIGITS = charArrayOf('᱐', '᱑', '᱒', '᱓', '᱔', '᱕', '᱖', '᱗', '᱘', '᱙')

    fun toOlChikiNumeral(n: Int): String {
        return n.toString().map { digit ->
            OL_CHIKI_DIGITS[digit - '0']
        }.joinToString("")
    }

    fun toOlChikiNumeral(n: Long): String {
        return n.toString().map { digit ->
            OL_CHIKI_DIGITS[digit - '0']
        }.joinToString("")
    }
}
