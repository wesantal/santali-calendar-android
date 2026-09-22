package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.utils.CalendarUtils
import org.wesantal.santalicalendar.moon.MeeusMoonCalculator

class CalendarUtilsTest {

    @Test
    fun `buildCalendarMonth returns correct month structure`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        val calendarMonth = CalendarUtils.buildCalendarMonth(months[0])
        assertNotNull(calendarMonth)
        assertEquals(months[0].id, calendarMonth.id)
        assertEquals(months[0].name, calendarMonth.name)
        assertEquals(months[0].roman, calendarMonth.roman)
        assertEquals(months[0].index, calendarMonth.index)
    }

    @Test
    fun `buildCalendarMonth days list is not empty`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        val calendarMonth = CalendarUtils.buildCalendarMonth(months[0])
        assertTrue("Days list should not be empty", calendarMonth.days.isNotEmpty())
    }

    @Test
    fun `buildCalendarMonth days count matches totalDays plus padding`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        val calendarMonth = CalendarUtils.buildCalendarMonth(months[0])
        assertTrue("Days list should have at least totalDays elements",
            calendarMonth.days.size >= months[0].totalDays)
    }

    @Test
    fun `buildCalendarMonth days list size is multiple of 7`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        val calendarMonth = CalendarUtils.buildCalendarMonth(months[0])
        assertEquals("Days list size should be multiple of 7", 0, calendarMonth.days.size % 7)
    }

    @Test
    fun `buildCalendarMonth with prev month fills leading cells`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        val calendarMonth = CalendarUtils.buildCalendarMonth(months[1], months[0])
        assertNotNull(calendarMonth)
        assertTrue("Days list should not be empty", calendarMonth.days.isNotEmpty())
    }

    @Test
    fun `buildCalendarMonth with next month fills trailing cells`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        val calendarMonth = CalendarUtils.buildCalendarMonth(months[0], null, months[1])
        assertNotNull(calendarMonth)
        assertTrue("Days list should not be empty", calendarMonth.days.isNotEmpty())
    }

    @Test
    fun `buildCalendar returns year with correct number of months for leap year`() {
        val calendarYear = CalendarUtils.buildCalendar(2024)
        assertEquals(2024, calendarYear.year)
        assertEquals(13, calendarYear.months.size)
    }

    @Test
    fun `buildCalendar returns year with correct number of months for non-leap year`() {
        val calendarYear = CalendarUtils.buildCalendar(2027)
        assertEquals(2027, calendarYear.year)
        assertEquals(12, calendarYear.months.size)
    }

    @Test
    fun `buildCalendar returns year with valid start and end dates`() {
        val calendarYear = CalendarUtils.buildCalendar(2024)
        assertTrue("End date should be after start date",
            calendarYear.endDate.time > calendarYear.startDate.time)
    }

    @Test
    fun `buildCalendar year months have correct indices`() {
        val calendarYear = CalendarUtils.buildCalendar(2024)
        for (i in calendarYear.months.indices) {
            assertEquals("Month at index $i should have index $i",
                i, calendarYear.months[i].index)
        }
    }

    @Test
    fun `buildCalendar months have valid date ranges`() {
        val calendarYear = CalendarUtils.buildCalendar(2024)
        for (month in calendarYear.months) {
            assertTrue("Month ${month.index} end should be after start",
                month.endDate.time > month.startDate.time)
        }
    }

    @Test
    fun `buildCalendar months have non-empty days lists`() {
        val calendarYear = CalendarUtils.buildCalendar(2024)
        for (month in calendarYear.months) {
            assertTrue("Month ${month.index} should have days",
                month.days.isNotEmpty())
        }
    }

    @Test
    fun `buildCalendar month days are multiple of 7`() {
        val calendarYear = CalendarUtils.buildCalendar(2024)
        for (month in calendarYear.months) {
            assertEquals("Month ${month.index} days size should be multiple of 7",
                0, month.days.size % 7)
        }
    }
}
