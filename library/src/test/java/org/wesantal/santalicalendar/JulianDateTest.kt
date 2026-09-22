package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.utils.JulianDate
import java.util.Date

class JulianDateTest {

    @Test
    fun `julianDayToUnixMs converts known Julian Day correctly`() {
        val jd = 2440587.5
        val expected = 0.0
        val result = JulianDate.julianDayToUnixMs(jd)
        assertEquals(expected, result, 0.001)
    }

    @Test
    fun `unixMsToJulianDay converts known Unix timestamp correctly`() {
        val ms = 0.0
        val expected = 2440587.5
        val result = JulianDate.unixMsToJulianDay(ms)
        assertEquals(expected, result, 0.001)
    }

    @Test
    fun `julianDayToUnixMs and unixMsToJulianDay are inverses`() {
        val jd = 2459580.5
        val ms = JulianDate.julianDayToUnixMs(jd)
        val roundTrip = JulianDate.unixMsToJulianDay(ms)
        assertEquals(jd, roundTrip, 0.001)
    }

    @Test
    fun `julianDayToDate converts J2000 epoch correctly`() {
        val jd = 2451545.0
        val date = JulianDate.julianDayToDate(jd)
        val expected = Date(946728000000L)
        assertTrue(Math.abs(expected.time - date.time) < 1000)
    }

    @Test
    fun `dateToJulianDay converts J2000 epoch correctly`() {
        val date = Date(946728000000L)
        val expected = 2451545.0
        val result = JulianDate.dateToJulianDay(date)
        assertEquals(expected, result, 0.01)
    }

    @Test
    fun `JULIAN_UNIX_EPOCH constant is correct`() {
        assertEquals(2440587.5, JulianDate.JULIAN_UNIX_EPOCH, 0.001)
    }

    @Test
    fun `MS_PER_DAY constant is correct`() {
        assertEquals(86400000.0, JulianDate.MS_PER_DAY, 0.001)
    }
}
