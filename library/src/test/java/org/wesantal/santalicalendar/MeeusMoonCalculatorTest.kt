package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.moon.MeeusMoonCalculator
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class MeeusMoonCalculatorTest {

    @Test
    fun `MS_PER_DAY constant is correct`() {
        assertEquals(86400000L, MeeusMoonCalculator.MS_PER_DAY)
    }

    @Test
    fun `SECONDS_PER_DAY constant is correct`() {
        assertEquals(86400.0, MeeusMoonCalculator.SECONDS_PER_DAY, 0.001)
    }

    @Test
    fun `JULIAN_UNIX_EPOCH constant is correct`() {
        assertEquals(2440587.5, MeeusMoonCalculator.JULIAN_UNIX_EPOCH, 0.001)
    }

    @Test
    fun `IST_OFFSET_MS constant is correct`() {
        val expected = (5.5 * 60 * 60 * 1000).toLong()
        assertEquals(expected, MeeusMoonCalculator.IST_OFFSET_MS)
    }

    @Test
    fun `METONIC_CYCLE_START constant is correct`() {
        assertEquals(2026, MeeusMoonCalculator.METONIC_CYCLE_START)
    }

    @Test
    fun `METONIC_LEAP_POS contains 7 positions`() {
        assertEquals(7, MeeusMoonCalculator.METONIC_LEAP_POS.size)
    }

    @Test
    fun `METONIC_LEAP_POS contains expected positions`() {
        assertTrue(MeeusMoonCalculator.METONIC_LEAP_POS.contains(1))
        assertTrue(MeeusMoonCalculator.METONIC_LEAP_POS.contains(4))
        assertTrue(MeeusMoonCalculator.METONIC_LEAP_POS.contains(7))
        assertTrue(MeeusMoonCalculator.METONIC_LEAP_POS.contains(9))
        assertTrue(MeeusMoonCalculator.METONIC_LEAP_POS.contains(12))
        assertTrue(MeeusMoonCalculator.METONIC_LEAP_POS.contains(15))
        assertTrue(MeeusMoonCalculator.METONIC_LEAP_POS.contains(18))
    }

    @Test
    fun `unixMsToJulianDay converts correctly`() {
        val ms = 0.0
        val expected = 2440587.5
        val result = MeeusMoonCalculator.unixMsToJulianDay(ms)
        assertEquals(expected, result, 0.001)
    }

    @Test
    fun `cosDeg returns correct values`() {
        assertEquals(1.0, MeeusMoonCalculator.cosDeg(0.0), 0.0001)
        assertEquals(0.0, MeeusMoonCalculator.cosDeg(90.0), 0.0001)
        assertEquals(-1.0, MeeusMoonCalculator.cosDeg(180.0), 0.0001)
        assertEquals(0.0, MeeusMoonCalculator.cosDeg(270.0), 0.0001)
    }

    @Test
    fun `isSantaliLeapYear identifies leap years correctly for METONIC cycle`() {
        // Position 1 is leap year
        assertTrue(MeeusMoonCalculator.isSantaliLeapYear(2026))
        // Position 4 is leap year
        assertTrue(MeeusMoonCalculator.isSantaliLeapYear(2029))
        // Position 7 is leap year
        assertTrue(MeeusMoonCalculator.isSantaliLeapYear(2032))
        // Position 9 is leap year
        assertTrue(MeeusMoonCalculator.isSantaliLeapYear(2034))
        // Position 12 is leap year
        assertTrue(MeeusMoonCalculator.isSantaliLeapYear(2037))
        // Position 15 is leap year
        assertTrue(MeeusMoonCalculator.isSantaliLeapYear(2040))
        // Position 18 is leap year
        assertTrue(MeeusMoonCalculator.isSantaliLeapYear(2043))
    }

    @Test
    fun `isSantaliLeapYear returns false for non-leap positions`() {
        // Position 2 is not leap
        assertEquals(false, MeeusMoonCalculator.isSantaliLeapYear(2027))
        // Position 3 is not leap
        assertEquals(false, MeeusMoonCalculator.isSantaliLeapYear(2028))
    }

    @Test
    fun `getNewMoon returns a valid timestamp`() {
        val k = 0L
        val result = MeeusMoonCalculator.getNewMoon(k)
        assertTrue("New moon timestamp should be positive", result > 0)
    }

    @Test
    fun `getFullMoon returns a valid timestamp`() {
        val k = 0L
        val result = MeeusMoonCalculator.getFullMoon(k)
        assertTrue("Full moon timestamp should be positive", result > 0)
    }

    @Test
    fun `getFullMoon is approximately 14 to 15 point 5 days after new moon`() {
        val k = 0L
        val newMoon = MeeusMoonCalculator.getNewMoon(k)
        val fullMoon = MeeusMoonCalculator.getFullMoon(k)
        val diffDays = (fullMoon - newMoon).toDouble() / MeeusMoonCalculator.MS_PER_DAY
        assertTrue("Full moon should be ~14.77 days after new moon, got $diffDays", diffDays in 14.0..15.5)
    }

    @Test
    fun `getNewMoon for recent k values returns recent dates`() {
        // k for year 2024 is approximately (2024-2000)*12.3685 ≈ 297
        val k = 297L
        val result = MeeusMoonCalculator.getNewMoon(k)
        val date = Date(result)
        val cal = Calendar.getInstance()
        cal.time = date
        assertEquals(2024, cal.get(Calendar.YEAR))
    }

    @Test
    fun `getChandradarshan returns a valid timestamp`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.JANUARY, 11, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val newMoonMs = cal.timeInMillis
        val result = MeeusMoonCalculator.getChandradarshan(newMoonMs)
        assertTrue("Chandradarshan should be after new moon", result > newMoonMs)
    }

    @Test
    fun `getChandradarshan is within 1-2 days of new moon`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.JANUARY, 11, 2, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val newMoonMs = cal.timeInMillis
        val result = MeeusMoonCalculator.getChandradarshan(newMoonMs)
        val diffDays = (result - newMoonMs).toDouble() / MeeusMoonCalculator.MS_PER_DAY
        assertTrue("Chandradarshan should be within 1-2 days of new moon, got $diffDays", diffDays in 0.0..2.5)
    }

    @Test
    fun `getFullMoonBetween returns a value within range`() {
        val startMs = 1704067200000L // 2024-01-01
        val endMs = 1706745600000L   // 2024-02-01
        val result = MeeusMoonCalculator.getFullMoonBetween(startMs, endMs)
        assertNotNull("Full moon should be found in January 2024", result)
        assertTrue("Full moon should be >= startMs", result!! >= startMs)
        assertTrue("Full moon should be < endMs", result < endMs)
    }

    @Test
    fun `getFullMoonBetween returns null when no full moon in range`() {
        val startMs = 1704067200000L // 2024-01-01
        val endMs = 1704153600000L   // 2024-01-02 (only 1 day)
        val result = MeeusMoonCalculator.getFullMoonBetween(startMs, endMs)
        assertEquals(null, result)
    }

    @Test
    fun `getPurnimaDayNum returns valid day number`() {
        val startMs = 1704067200000L
        val endMs = 1706745600000L
        val fullMoonMs = 1705452000000L
        val result = MeeusMoonCalculator.getPurnimaDayNum(startMs, endMs, fullMoonMs)
        assertTrue("Day number should be positive", result >= 1)
    }

    @Test
    fun `getPurnimaDayNum returns 1 when endMs less than or equal to startMs`() {
        val startMs = 1706745600000L
        val endMs = 1704067200000L
        val fullMoonMs = 1705452000000L
        val result = MeeusMoonCalculator.getPurnimaDayNum(startMs, endMs, fullMoonMs)
        assertEquals(1, result)
    }

    @Test
    fun `getKunamiDate returns a valid Date`() {
        val startMs = 1704067200000L
        val endMs = 1706745600000L
        val fullMoonMs = 1705452000000L
        val result = MeeusMoonCalculator.getKunamiDate(startMs, endMs, fullMoonMs)
        assertNotNull(result)
        assertTrue("Kunami date should be >= start", result.time >= startMs)
    }

    @Test
    fun `getNextNewMoon returns a date after the given date`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(2024, Calendar.JANUARY, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val afterMs = cal.timeInMillis
        val result = MeeusMoonCalculator.getNextNewMoon(afterMs)
        assertTrue("Next new moon should be after the given date", result > afterMs)
    }

    @Test
    fun `getMagStartMoon returns a valid timestamp for 2024`() {
        val result = MeeusMoonCalculator.getMagStartMoon(2024)
        assertTrue("Mag start moon should be positive", result > 0)
        val date = Date(result)
        val cal = Calendar.getInstance()
        cal.time = date
        // Mag month typically starts in December/January
        val month = cal.get(Calendar.MONTH)
        assertTrue("Mag should start in Dec/Jan, got month $month", month == Calendar.DECEMBER || month == Calendar.JANUARY)
    }

    @Test
    fun `getSantaliMonths returns correct number of months for leap year`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        assertEquals("Leap year should have 13 months", 13, months.size)
    }

    @Test
    fun `getSantaliMonths returns correct number of months for non-leap year`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2027)
        assertEquals("Non-leap year should have 12 months", 12, months.size)
    }

    @Test
    fun `getSantaliMonths returns months with correct indices`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        for (i in months.indices) {
            assertEquals("Month at index $i should have index $i", i, months[i].index)
        }
    }

    @Test
    fun `getSantaliMonths returns months in chronological order`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        for (i in 1 until months.size) {
            assertTrue(
                "Month ${months[i].index} should start after month ${months[i - 1].index}",
                months[i].startDate.time >= months[i - 1].startDate.time
            )
        }
    }

    @Test
    fun `getSantaliMonths returns months with valid date ranges`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        for (month in months) {
            assertTrue("Month ${month.index} end should be after start",
                month.endDate.time > month.startDate.time)
            assertTrue("Month ${month.index} totalDays should be positive",
                month.totalDays > 0)
        }
    }

    @Test
    fun `getSantaliMonths month ids match SANTALI_MONTHS_BASE order for leap year`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        val expectedIds = listOf("MAG", "FAGUN", "CHAAT", "BAISAK", "JHENT", "ASHAL",
            "SAAN", "BHADOR", "DASANY", "SOHRAY", "AGHAN", "PUSH", "SARCHA")
        assertEquals(expectedIds.size, months.size)
        for (i in months.indices) {
            assertEquals("Month at index $i should be ${expectedIds[i]}",
                expectedIds[i], months[i].id.name)
        }
    }

    @Test
    fun `getSantaliMonths month ids match SANTALI_MONTHS_BASE order for non-leap year`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2027)
        val expectedIds = listOf("MAG", "FAGUN", "CHAAT", "BAISAK", "JHENT", "ASHAL",
            "SAAN", "BHADOR", "DASANY", "SOHRAY", "AGHAN", "PUSH")
        assertEquals(expectedIds.size, months.size)
        for (i in months.indices) {
            assertEquals("Month at index $i should be ${expectedIds[i]}",
                expectedIds[i], months[i].id.name)
        }
    }

    @Test
    fun `getMonthIndex returns correct index for a date`() {
        // Create a date that should fall in MAG month (index 0)
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        val magMonth = months.first { it.index == 0 }
        val testDate = Date(magMonth.startDate.time + 5 * MeeusMoonCalculator.MS_PER_DAY)
        val result = MeeusMoonCalculator.getMonthIndex(testDate)
        assertEquals(0, result)
    }

    @Test
    fun `getMonthIndex handles dates at month boundaries`() {
        val months = MeeusMoonCalculator.getSantaliMonths(2024)
        // Test with date at start of second month
        if (months.size > 1) {
            val secondMonth = months[1]
            val testDate = Date(secondMonth.startDate.time + MeeusMoonCalculator.MS_PER_DAY)
            val result = MeeusMoonCalculator.getMonthIndex(testDate)
            assertEquals(1, result)
        }
    }

    @Test
    fun `getMonthFromIndex returns correct month`() {
        val month = MeeusMoonCalculator.getMonthFromIndex(0)
        assertEquals(0, month.index)
        assertEquals("MAG", month.id.name)
    }

    @Test
    fun `getNewMoonsForYear returns multiple new moons`() {
        val newMoons = MeeusMoonCalculator.getNewMoonsForYear(2024)
        assertTrue("Should have at least 12 new moons in a year", newMoons.size >= 12)
        assertTrue("Should have at most 13 new moons in a year", newMoons.size <= 13)
    }

    @Test
    fun `getNewMoonsForYear returns sorted list`() {
        val newMoons = MeeusMoonCalculator.getNewMoonsForYear(2024)
        for (i in 1 until newMoons.size) {
            assertTrue("New moons should be sorted", newMoons[i] > newMoons[i - 1])
        }
    }

    @Test
    fun `getFullMoonsForYear returns multiple full moons`() {
        val fullMoons = MeeusMoonCalculator.getFullMoonsForYear(2024)
        assertTrue("Should have at least 12 full moons in a year", fullMoons.size >= 12)
        assertTrue("Should have at most 13 full moons in a year", fullMoons.size <= 13)
    }

    @Test
    fun `getFullMoonsForYear returns sorted list`() {
        val fullMoons = MeeusMoonCalculator.getFullMoonsForYear(2024)
        for (i in 1 until fullMoons.size) {
            assertTrue("Full moons should be sorted", fullMoons[i] > fullMoons[i - 1])
        }
    }

    @Test
    fun `getCalendarMonthIndex returns valid index`() {
        val result = MeeusMoonCalculator.getCalendarMonthIndex(Date())
        assertTrue("Calendar month index should be >= 0", result >= 0)
    }

    @Test
    fun `Mag 2026 Chandradarshan matches Dart reference`() {
        val magStartMoon = MeeusMoonCalculator.getMagStartMoon(2026)
        val chandradarshan = MeeusMoonCalculator.getChandradarshan(magStartMoon)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = chandradarshan
        assertEquals("Year should be 2026", 2026, cal.get(Calendar.YEAR))
        assertEquals("Month should be January", Calendar.JANUARY, cal.get(Calendar.MONTH))
        assertEquals("Day should be 19", 19, cal.get(Calendar.DAY_OF_MONTH))
        assertEquals("Hour should be 11", 11, cal.get(Calendar.HOUR_OF_DAY))
        assertEquals("Minute should be 30", 30, cal.get(Calendar.MINUTE))
    }

    @Test
    fun `Mag 2026 new moon on Jan 18 after 5PM IST`() {
        val magStartMoon = MeeusMoonCalculator.getMagStartMoon(2026)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = magStartMoon
        assertEquals("New moon year should be 2026", 2026, cal.get(Calendar.YEAR))
        assertEquals("New moon month should be January", Calendar.JANUARY, cal.get(Calendar.MONTH))
        assertEquals("New moon day should be 18", 18, cal.get(Calendar.DAY_OF_MONTH))
        assertTrue("New moon hour should be >= 17", cal.get(Calendar.HOUR_OF_DAY) >= 17)
    }

    @Test
    fun `getNewMoon 322 produces correct date`() {
        val nm = MeeusMoonCalculator.getNewMoon(322)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = nm
        assertEquals("Should be January 18", 18, cal.get(Calendar.DAY_OF_MONTH))
        assertTrue("Hour should be >= 17", cal.get(Calendar.HOUR_OF_DAY) >= 17)
    }

    @Test
    fun `Dart anchor matches Kotlin anchor`() {
        assertEquals(
            "Anchor should match Dart value",
            1674334380000L,
            MeeusMoonCalculator.ANCHOR_NM_MS
        )
    }
}
