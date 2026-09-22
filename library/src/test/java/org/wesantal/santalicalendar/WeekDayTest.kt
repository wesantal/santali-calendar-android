package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Test
import org.wesantal.santalicalendar.core.WeekDay

class WeekDayTest {

    @Test
    fun `fromIndex returns Sunday for 0`() {
        assertEquals(WeekDay.SUNDAY, WeekDay.fromIndex(0))
    }

    @Test
    fun `fromIndex returns Monday for 1`() {
        assertEquals(WeekDay.MONDAY, WeekDay.fromIndex(1))
    }

    @Test
    fun `fromIndex returns Tuesday for 2`() {
        assertEquals(WeekDay.TUESDAY, WeekDay.fromIndex(2))
    }

    @Test
    fun `fromIndex returns Wednesday for 3`() {
        assertEquals(WeekDay.WEDNESDAY, WeekDay.fromIndex(3))
    }

    @Test
    fun `fromIndex returns Thursday for 4`() {
        assertEquals(WeekDay.THURSDAY, WeekDay.fromIndex(4))
    }

    @Test
    fun `fromIndex returns Friday for 5`() {
        assertEquals(WeekDay.FRIDAY, WeekDay.fromIndex(5))
    }

    @Test
    fun `fromIndex returns Saturday for 6`() {
        assertEquals(WeekDay.SATURDAY, WeekDay.fromIndex(6))
    }

    @Test
    fun `english names are correct`() {
        assertEquals("Sunday", WeekDay.SUNDAY.english)
        assertEquals("Monday", WeekDay.MONDAY.english)
        assertEquals("Tuesday", WeekDay.TUESDAY.english)
        assertEquals("Wednesday", WeekDay.WEDNESDAY.english)
        assertEquals("Thursday", WeekDay.THURSDAY.english)
        assertEquals("Friday", WeekDay.FRIDAY.english)
        assertEquals("Saturday", WeekDay.SATURDAY.english)
    }

    @Test
    fun `indices are sequential from 0`() {
        val entries = WeekDay.entries
        assertEquals(7, entries.size)
        for (i in entries.indices) {
            assertEquals(i, entries[i].index)
        }
    }
}
