package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.core.DateTime
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class DateTimeTest {

    @Test
    fun `toDate converts timestamp to Date correctly`() {
        val timestamp = 1000000000000L
        val date = DateTime.toDate(timestamp)
        assertEquals(timestamp, date.time)
    }

    @Test
    fun `toTimestamp converts Date to timestamp correctly`() {
        val date = Date(1000000000000L)
        val timestamp = DateTime.toTimestamp(date)
        assertEquals(1000000000000L, timestamp)
    }

    @Test
    fun `cloneDate creates independent copy`() {
        val original = Date(1000000000000L)
        val clone = DateTime.cloneDate(original)
        assertEquals(original.time, clone.time)
        clone.time = 2000000000000L
        assertEquals(1000000000000L, original.time)
    }

    @Test
    fun `formatDateString formats correctly in UTC`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.JANUARY, 15, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val date = cal.time
        assertEquals("2024-01-15", DateTime.formatDateString(date))
    }

    @Test
    fun `getDayOfWeek returns correct day`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.JANUARY, 14, 12, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val date = cal.time
        val dayOfWeek = DateTime.getDayOfWeek(date)
        assertEquals(0, dayOfWeek)
    }

    @Test
    fun `isSameDay returns true for same day`() {
        val cal1 = Calendar.getInstance()
        cal1.set(2024, Calendar.MARCH, 15, 10, 30, 0)
        val cal2 = Calendar.getInstance()
        cal2.set(2024, Calendar.MARCH, 15, 18, 45, 0)
        assertTrue(DateTime.isSameDay(cal1.time, cal2.time))
    }

    @Test
    fun `isSameDay returns false for different days`() {
        val cal1 = Calendar.getInstance()
        cal1.set(2024, Calendar.MARCH, 15, 10, 30, 0)
        val cal2 = Calendar.getInstance()
        cal2.set(2024, Calendar.MARCH, 16, 10, 30, 0)
        assertFalse(DateTime.isSameDay(cal1.time, cal2.time))
    }

    @Test
    fun `addDays adds days correctly`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.FEBRUARY, 28, 12, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val result = DateTime.addDays(cal.time, 1)
        val resultCal = Calendar.getInstance()
        resultCal.time = result
        assertEquals(29, resultCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `addDays handles month boundary`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.JANUARY, 31, 12, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val result = DateTime.addDays(cal.time, 1)
        val resultCal = Calendar.getInstance()
        resultCal.time = result
        assertEquals(Calendar.FEBRUARY, resultCal.get(Calendar.MONTH))
        assertEquals(1, resultCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `addMilliseconds adds correctly`() {
        val date = Date(1000000000000L)
        val result = DateTime.addMilliseconds(date, 5000L)
        assertEquals(1000000005000L, result.time)
    }

    @Test
    fun `startOfDay returns midnight`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.MARCH, 15, 14, 30, 45)
        val result = DateTime.startOfDay(cal.time)
        val resultCal = Calendar.getInstance()
        resultCal.time = result
        assertEquals(0, resultCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, resultCal.get(Calendar.MINUTE))
        assertEquals(0, resultCal.get(Calendar.SECOND))
    }

    @Test
    fun `endOfDay returns 23_59_59_999`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.MARCH, 15, 14, 30, 45)
        val result = DateTime.endOfDay(cal.time)
        val resultCal = Calendar.getInstance()
        resultCal.time = result
        assertEquals(23, resultCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, resultCal.get(Calendar.MINUTE))
        assertEquals(59, resultCal.get(Calendar.SECOND))
        assertEquals(999, resultCal.get(Calendar.MILLISECOND))
    }

    @Test
    fun `getYear returns correct year`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.MARCH, 15)
        assertEquals(2024, DateTime.getYear(cal.time))
    }

    @Test
    fun `getMonth returns correct month`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.MARCH, 15)
        assertEquals(Calendar.MARCH, DateTime.getMonth(cal.time))
    }

    @Test
    fun `getDay returns correct day`() {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.MARCH, 15)
        assertEquals(15, DateTime.getDay(cal.time))
    }

    @Test
    fun `isLeapYear correctly identifies leap years`() {
        assertTrue(DateTime.isLeapYear(2024))
        assertTrue(DateTime.isLeapYear(2000))
        assertFalse(DateTime.isLeapYear(1900))
        assertFalse(DateTime.isLeapYear(2023))
        assertTrue(DateTime.isLeapYear(2048))
        assertFalse(DateTime.isLeapYear(2100))
    }

    @Test
    fun `getDaysInMonth returns correct count`() {
        assertEquals(31, DateTime.getDaysInMonth(2024, Calendar.JANUARY))
        assertEquals(29, DateTime.getDaysInMonth(2024, Calendar.FEBRUARY))
        assertEquals(28, DateTime.getDaysInMonth(2023, Calendar.FEBRUARY))
        assertEquals(30, DateTime.getDaysInMonth(2024, Calendar.APRIL))
    }

    @Test
    fun `createDate creates correct date`() {
        val date = DateTime.createDate(2024, Calendar.MARCH, 15, 10, 30, 45)
        val cal = Calendar.getInstance()
        cal.time = date
        assertEquals(2024, cal.get(Calendar.YEAR))
        assertEquals(Calendar.MARCH, cal.get(Calendar.MONTH))
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH))
        assertEquals(10, cal.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, cal.get(Calendar.MINUTE))
        assertEquals(45, cal.get(Calendar.SECOND))
    }

    @Test
    fun `createUTCDate creates correct UTC date`() {
        val date = DateTime.createUTCDate(2024, Calendar.MARCH, 15, 10, 30, 45)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.time = date
        assertEquals(2024, cal.get(Calendar.YEAR))
        assertEquals(Calendar.MARCH, cal.get(Calendar.MONTH))
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH))
        assertEquals(10, cal.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, cal.get(Calendar.MINUTE))
        assertEquals(45, cal.get(Calendar.SECOND))
    }

    @Test
    fun `differenceInMilliseconds calculates correctly`() {
        val early = Date(1000000000000L)
        val later = Date(1000000005000L)
        assertEquals(5000L, DateTime.differenceInMilliseconds(later, early))
    }

    @Test
    fun `diffInDays calculates correctly`() {
        val early = Date(1000000000000L)
        val later = Date(1000000000000L + 86400000L * 3)
        assertEquals(3, DateTime.diffInDays(early, later))
    }

    @Test
    fun `toISTDate adds IST offset`() {
        val ms = 1000000000000L
        val result = DateTime.toISTDate(ms)
        val expected = ms + (5.5 * 60 * 60 * 1000).toLong()
        assertEquals(expected, result.time)
    }

    @Test
    fun `MS_PER_DAY constant is correct`() {
        assertEquals(86400000L, DateTime.MS_PER_DAY)
    }

    @Test
    fun `JULIAN_UNIX_EPOCH constant is correct`() {
        assertEquals(2440587.5, DateTime.JULIAN_UNIX_EPOCH, 0.001)
    }
}
