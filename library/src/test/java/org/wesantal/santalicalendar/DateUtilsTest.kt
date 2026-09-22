package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.utils.DateUtils
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class DateUtilsTest {

    @Test
    fun `toDate converts timestamp to Date`() {
        val timestamp = 1000000000000L
        val date = DateUtils.toDate(timestamp)
        assertEquals(timestamp, date.time)
    }

    @Test
    fun `toTimestamp converts Date to timestamp`() {
        val date = Date(1000000000000L)
        val timestamp = DateUtils.toTimestamp(date)
        assertEquals(1000000000000L, timestamp)
    }

    @Test
    fun `formatDateString formats correctly`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.JANUARY, 15, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        assertEquals("2024-01-15", DateUtils.formatDateString(cal.time))
    }

    @Test
    fun `getDayOfWeek returns correct day`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.JANUARY, 14, 12, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        assertEquals(0, DateUtils.getDayOfWeek(cal.time))
    }

    @Test
    fun `isSameDay returns true for same day`() {
        val cal1 = Calendar.getInstance()
        cal1.set(2024, Calendar.MARCH, 15, 10, 30, 0)
        val cal2 = Calendar.getInstance()
        cal2.set(2024, Calendar.MARCH, 15, 18, 45, 0)
        assertTrue(DateUtils.isSameDay(cal1.time, cal2.time))
    }

    @Test
    fun `isSameDay returns false for different days`() {
        val cal1 = Calendar.getInstance()
        cal1.set(2024, Calendar.MARCH, 15, 10, 30, 0)
        val cal2 = Calendar.getInstance()
        cal2.set(2024, Calendar.MARCH, 16, 10, 30, 0)
        assertFalse(DateUtils.isSameDay(cal1.time, cal2.time))
    }

    @Test
    fun `addDays adds days correctly`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.FEBRUARY, 28, 12, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val result = DateUtils.addDays(cal.time, 1)
        val resultCal = Calendar.getInstance()
        resultCal.time = result
        assertEquals(29, resultCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `addMilliseconds adds correctly`() {
        val date = Date(1000000000000L)
        val result = DateUtils.addMilliseconds(date, 5000L)
        assertEquals(1000000005000L, result.time)
    }

    @Test
    fun `startOfDay returns midnight`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.MARCH, 15, 14, 30, 45)
        val result = DateUtils.startOfDay(cal.time)
        val resultCal = Calendar.getInstance()
        resultCal.time = result
        assertEquals(0, resultCal.get(Calendar.HOUR_OF_DAY))
    }

    @Test
    fun `endOfDay returns end of day`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.MARCH, 15, 14, 30, 45)
        val result = DateUtils.endOfDay(cal.time)
        val resultCal = Calendar.getInstance()
        resultCal.time = result
        assertEquals(23, resultCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, resultCal.get(Calendar.MINUTE))
    }

    @Test
    fun `isLeapYear correctly identifies leap years`() {
        assertTrue(DateUtils.isLeapYear(2024))
        assertTrue(DateUtils.isLeapYear(2000))
        assertFalse(DateUtils.isLeapYear(1900))
        assertFalse(DateUtils.isLeapYear(2023))
    }

    @Test
    fun `getDaysInMonth returns correct count`() {
        assertEquals(31, DateUtils.getDaysInMonth(2024, Calendar.JANUARY))
        assertEquals(29, DateUtils.getDaysInMonth(2024, Calendar.FEBRUARY))
        assertEquals(28, DateUtils.getDaysInMonth(2023, Calendar.FEBRUARY))
    }

    @Test
    fun `createDate creates correct date`() {
        val date = DateUtils.createDate(2024, Calendar.MARCH, 15, 10, 30, 45)
        val cal = Calendar.getInstance()
        cal.time = date
        assertEquals(2024, cal.get(Calendar.YEAR))
        assertEquals(Calendar.MARCH, cal.get(Calendar.MONTH))
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `createUTCDate creates correct UTC date`() {
        val date = DateUtils.createUTCDate(2024, Calendar.MARCH, 15, 10, 30, 45)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.time = date
        assertEquals(10, cal.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, cal.get(Calendar.MINUTE))
    }

    @Test
    fun `IST_OFFSET_MS constant is correct`() {
        val expected = (5.5 * 60 * 60 * 1000).toLong()
        assertEquals(expected, DateUtils.IST_OFFSET_MS)
    }

    @Test
    fun `MS_PER_DAY constant is correct`() {
        assertEquals(86400000L, DateUtils.MS_PER_DAY)
    }

    @Test
    fun `startOfSantaliDay returns correct boundary`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.MARCH, 15, 14, 30, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val result = DateUtils.startOfSantaliDay(cal.time)
        val resultCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        resultCal.time = result
        assertEquals(11, resultCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, resultCal.get(Calendar.MINUTE))
    }

    @Test
    fun `endOfSantaliDay is one MS_PER_DAY after startOfSantaliDay`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.MARCH, 15, 14, 30, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = DateUtils.startOfSantaliDay(cal.time)
        val end = DateUtils.endOfSantaliDay(cal.time)
        assertEquals(DateUtils.MS_PER_DAY, end.time - start.time)
    }

    @Test
    fun `isSameSantaliDay returns true for same Santali day`() {
        val cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal1.set(2024, Calendar.MARCH, 15, 14, 30, 0)
        val cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal2.set(2024, Calendar.MARCH, 15, 20, 45, 0)
        assertTrue(DateUtils.isSameSantaliDay(cal1.time, cal2.time))
    }
}
