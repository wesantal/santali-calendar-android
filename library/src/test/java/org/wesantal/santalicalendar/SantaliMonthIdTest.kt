package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.calendar.SantaliMonthId

class SantaliMonthIdTest {

    @Test
    fun `SantaliMonthId has 13 entries`() {
        assertEquals(13, SantaliMonthId.entries.size)
    }

    @Test
    fun `fromIndex returns correct month for all indices`() {
        for (i in 0..12) {
            val monthId = SantaliMonthId.fromIndex(i)
            assertEquals(i, monthId.index)
        }
    }

    @Test
    fun `all months have non-empty OlChiki names`() {
        for (month in SantaliMonthId.entries) {
            assertTrue("Month ${month.name} should have non-empty OlChiki name",
                month.olChiki.isNotEmpty())
        }
    }

    @Test
    fun `all months have non-empty roman names`() {
        for (month in SantaliMonthId.entries) {
            assertTrue("Month ${month.name} should have non-empty roman name",
                month.roman.isNotEmpty())
        }
    }

    @Test
    fun `SARCHA is the leap month`() {
        assertTrue("SARCHA should be leap month", SantaliMonthId.SARCHA.isLeapMonth)
    }

    @Test
    fun `non-SARCHA months are not leap months`() {
        for (month in SantaliMonthId.entries) {
            if (month != SantaliMonthId.SARCHA) {
                assertEquals("Month ${month.name} should not be leap month",
                    false, month.isLeapMonth)
            }
        }
    }

    @Test
    fun `indices are sequential from 0 to 12`() {
        val indices = SantaliMonthId.entries.map { it.index }.sorted()
        assertEquals((0..12).toList(), indices)
    }

    @Test
    fun `MAG is first month`() {
        assertEquals(0, SantaliMonthId.MAG.index)
        assertEquals("Mag", SantaliMonthId.MAG.roman)
    }

    @Test
    fun `PUSH is 12th month`() {
        assertEquals(11, SantaliMonthId.PUSH.index)
        assertEquals("Pus", SantaliMonthId.PUSH.roman)
    }

    @Test
    fun `SARCHA is 13th month`() {
        assertEquals(12, SantaliMonthId.SARCHA.index)
        assertEquals("Sarcha Chando", SantaliMonthId.SARCHA.roman)
    }
}
