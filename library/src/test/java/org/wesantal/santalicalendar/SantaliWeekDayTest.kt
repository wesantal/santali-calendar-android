package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.core.SantaliWeekDay

class SantaliWeekDayTest {

    @Test
    fun `fromIndex returns Sunday for 0`() {
        assertEquals(SantaliWeekDay.SUNDAY, SantaliWeekDay.fromIndex(0))
    }

    @Test
    fun `fromIndex returns Monday for 1`() {
        assertEquals(SantaliWeekDay.MONDAY, SantaliWeekDay.fromIndex(1))
    }

    @Test
    fun `fromIndex returns Tuesday for 2`() {
        assertEquals(SantaliWeekDay.TUESDAY, SantaliWeekDay.fromIndex(2))
    }

    @Test
    fun `fromIndex returns Wednesday for 3`() {
        assertEquals(SantaliWeekDay.WEDNESDAY, SantaliWeekDay.fromIndex(3))
    }

    @Test
    fun `fromIndex returns Thursday for 4`() {
        assertEquals(SantaliWeekDay.THURSDAY, SantaliWeekDay.fromIndex(4))
    }

    @Test
    fun `fromIndex returns Friday for 5`() {
        assertEquals(SantaliWeekDay.FRIDAY, SantaliWeekDay.fromIndex(5))
    }

    @Test
    fun `fromIndex returns Saturday for 6`() {
        assertEquals(SantaliWeekDay.SATURDAY, SantaliWeekDay.fromIndex(6))
    }

    @Test
    fun `olChiki names are not empty`() {
        for (day in SantaliWeekDay.entries) {
            assertTrue("OlChiki name for ${day.english} should not be empty", day.olChiki.isNotEmpty())
        }
    }

    @Test
    fun `english names match WeekDay`() {
        assertEquals("Sunday", SantaliWeekDay.SUNDAY.english)
        assertEquals("Monday", SantaliWeekDay.MONDAY.english)
        assertEquals("Tuesday", SantaliWeekDay.TUESDAY.english)
        assertEquals("Wednesday", SantaliWeekDay.WEDNESDAY.english)
        assertEquals("Thursday", SantaliWeekDay.THURSDAY.english)
        assertEquals("Friday", SantaliWeekDay.FRIDAY.english)
        assertEquals("Saturday", SantaliWeekDay.SATURDAY.english)
    }

    @Test
    fun `indices are sequential from 0`() {
        val entries = SantaliWeekDay.entries
        assertEquals(7, entries.size)
        for (i in entries.indices) {
            assertEquals(i, entries[i].index)
        }
    }
}
