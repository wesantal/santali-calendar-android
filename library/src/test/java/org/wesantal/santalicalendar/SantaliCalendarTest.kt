package org.wesantal.santalicalendar

import java.util.Calendar
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SantaliCalendarTest {

    private lateinit var calendar: SantaliCalendar

    @Before
    fun setUp() {
        calendar = SantaliCalendar()
    }

    @Test
    fun `getMonths returns correct number of months for leap year`() {
        val months = calendar.getMonths(2024)
        assertEquals(13, months.size)
    }

    @Test
    fun `getMonths returns correct number of months for non-leap year`() {
        val months = calendar.getMonths(2027)
        assertEquals(12, months.size)
    }

    @Test
    fun `getMonths returns months in chronological order`() {
        val months = calendar.getMonths(2024)
        for (i in 1 until months.size) {
            assertTrue(
                "Month ${months[i].index} should start after month ${months[i - 1].index}",
                months[i].startDate.time >= months[i - 1].startDate.time,
            )
        }
    }

    @Test
    fun `getMonths caches results`() {
        val months1 = calendar.getMonths(2024)
        val months2 = calendar.getMonths(2024)
        assertEquals(months1, months2)
    }

    @Test
    fun `getCalendar returns valid year structure for leap year`() {
        val year = calendar.getCalendar(2024)
        assertEquals(2024, year.year)
        assertEquals(13, year.months.size)
    }

    @Test
    fun `getCalendar returns valid year structure for non-leap year`() {
        val year = calendar.getCalendar(2027)
        assertEquals(2027, year.year)
        assertEquals(12, year.months.size)
    }

    @Test
    fun `getCalendar caches results`() {
        val year1 = calendar.getCalendar(2024)
        val year2 = calendar.getCalendar(2024)
        assertEquals(year1, year2)
    }

    @Test
    fun `getDaysInMonth returns correct count`() {
        val daysInMonth = calendar.getDaysInMonth(2024, 1)
        assertTrue("Days in month should be positive", daysInMonth > 0)
    }

    @Test
    fun `isLeapYear identifies leap years correctly`() {
        assertTrue(calendar.isLeapYear(2026))
        assertTrue(calendar.isLeapYear(2029))
        assertEquals(false, calendar.isLeapYear(2027))
    }

    @Test
    fun `getCurrentMonth returns a valid month`() {
        val month = calendar.getCurrentMonth()
        assertNotNull(month)
        assertTrue("Current month index should be >= 0", month.index >= 0)
    }

    @Test
    fun `getMonthIndex returns valid index for current date`() {
        val index = calendar.getMonthIndex(Date())
        assertTrue("Month index should be >= 0", index >= 0)
    }

    @Test
    fun `getMonthFromDate returns correct month for a date`() {
        val months = calendar.getMonths(2024)
        val testDate = Date(months[0].startDate.time + 5 * 86400000L)
        val month = calendar.getMonthFromDate(testDate)
        assertNotNull(month)
        assertEquals(0, month.index)
    }

    @Test
    fun `getFestivals returns non-empty list for 2024`() {
        val festivals = calendar.getFestivals(2024)
        assertTrue("Should have festivals for 2024", festivals.isNotEmpty())
    }

    @Test
    fun `getFestivals are sorted by date`() {
        val festivals = calendar.getFestivals(2024)
        for (i in 1 until festivals.size) {
            assertTrue(
                "Festival $i should be after festival ${i - 1}",
                festivals[i].date.time >= festivals[i - 1].date.time,
            )
        }
    }

    @Test
    fun `getFestivals have valid data`() {
        val festivals = calendar.getFestivals(2024)
        for ((id, name, roman) in festivals) {
            assertTrue("Festival id should not be empty", id.isNotEmpty())
            assertTrue("Festival name should not be empty", name.isNotEmpty())
            assertTrue("Festival roman should not be empty", roman.isNotEmpty())
        }
    }

    @Test
    fun `resolveFestival returns festival with correct data`() {
        val definition = org.wesantal.santalicalendar.festival.FestivalData.santaliFestivals.first()
        val festival = calendar.resolveFestival(definition, 2024)
        assertEquals(definition.id, festival.id)
        assertEquals(definition.name, festival.name)
        assertEquals(definition.roman, festival.roman)
    }

    @Test
    fun `resolveFestival for fixed gregorian festival`() {
        val definition =
            org.wesantal.santalicalendar.festival.FestivalData.santaliFestivals.first {
                it.rule is org.wesantal.santalicalendar.festival.SantaliFestivalRule.FixedGregorian
            }
        val festival = calendar.resolveFestival(definition, 2024)
        assertNotNull(festival.date)
        val cal = Calendar.getInstance()
        cal.time = festival.date
        val rule =
            definition.rule
                as org.wesantal.santalicalendar.festival.SantaliFestivalRule.FixedGregorian
        assertEquals(rule.day, cal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `resolveFestival for moon relative festival`() {
        val definition =
            org.wesantal.santalicalendar.festival.FestivalData.santaliFestivals.first {
                it.rule is org.wesantal.santalicalendar.festival.SantaliFestivalRule.MoonRelative
            }
        val festival = calendar.resolveFestival(definition, 2024)
        assertNotNull(festival.date)
    }
}
