package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Test
import org.wesantal.santalicalendar.utils.OlChikiNumerals

class OlChikiNumeralsTest {

    @Test
    fun `toOlChikiNumeral converts 0 correctly`() {
        assertEquals("᱐", OlChikiNumerals.toOlChikiNumeral(0))
    }

    @Test
    fun `toOlChikiNumeral converts 1 correctly`() {
        assertEquals("᱑", OlChikiNumerals.toOlChikiNumeral(1))
    }

    @Test
    fun `toOlChikiNumeral converts 9 correctly`() {
        assertEquals("᱙", OlChikiNumerals.toOlChikiNumeral(9))
    }

    @Test
    fun `toOlChikiNumeral converts 10 correctly`() {
        assertEquals("᱑᱐", OlChikiNumerals.toOlChikiNumeral(10))
    }

    @Test
    fun `toOlChikiNumeral converts 15 correctly`() {
        assertEquals("᱑᱕", OlChikiNumerals.toOlChikiNumeral(15))
    }

    @Test
    fun `toOlChikiNumeral converts 42 correctly`() {
        assertEquals("᱔᱒", OlChikiNumerals.toOlChikiNumeral(42))
    }

    @Test
    fun `toOlChikiNumeral converts 100 correctly`() {
        assertEquals("᱑᱐᱐", OlChikiNumerals.toOlChikiNumeral(100))
    }

    @Test
    fun `toOlChikiNumeral converts 999 correctly`() {
        assertEquals("᱙᱙᱙", OlChikiNumerals.toOlChikiNumeral(999))
    }

    @Test
    fun `toOlChikiNumeral Long converts 123 correctly`() {
        assertEquals("᱑᱒᱓", OlChikiNumerals.toOlChikiNumeral(123L))
    }

    @Test
    fun `toOlChikiNumeral Long converts 0 correctly`() {
        assertEquals("᱐", OlChikiNumerals.toOlChikiNumeral(0L))
    }
}
