package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.moon.DeltaT

class DeltaTTest {

    @Test
    fun `DeltaT for year 2000 is approximately 64 seconds`() {
        val result = DeltaT.calculate(2000.0)
        assertTrue("DeltaT for 2000 should be around 64s, got $result", result in 60.0..70.0)
    }

    @Test
    fun `DeltaT for year 1900 is positive`() {
        val result = DeltaT.calculate(1900.0)
        assertTrue("DeltaT for 1900 should be positive, got $result", result > 0)
    }

    @Test
    fun `DeltaT for year 2050 uses 2050-2150 formula`() {
        val result = DeltaT.calculate(2050.0)
        assertTrue("DeltaT for 2050 should be positive, got $result", result > 0)
    }

    @Test
    fun `DeltaT for year 2100 is in reasonable range`() {
        val result = DeltaT.calculate(2100.0)
        assertTrue("DeltaT for 2100 should be in reasonable range, got $result", result in -100.0..250.0)
    }

    @Test
    fun `DeltaT for year 1986 uses 1986-2005 formula`() {
        val result = DeltaT.calculate(1986.0)
        assertTrue("DeltaT for 1986 should be in range, got $result", result in 30.0..70.0)
    }

    @Test
    fun `DeltaT for year 2024 uses 2005-2050 formula`() {
        val result = DeltaT.calculate(2024.0)
        assertTrue("DeltaT for 2024 should be in range, got $result", result in 60.0..80.0)
    }

    @Test
    fun `DeltaT for year 1800 falls through to default formula`() {
        val result = DeltaT.calculate(1800.0)
        assertTrue("DeltaT for 1800 should be in range, got $result", result in -100.0..100.0)
    }
}
