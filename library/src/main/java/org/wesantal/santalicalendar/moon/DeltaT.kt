package org.wesantal.santalicalendar.moon

object DeltaT {
    fun calculate(year: Double): Double {
        val t: Double

        if (year in 2005.0..<2050.0) {
            t = (year - 2000) / 100
            return 62.92 + 32.217 * t + 55.89 * t * t
        }

        if (year in 1986.0..<2005.0) {
            t = (year - 2000) / 100
            return 63.86 + 33.45 * t - 603.74 * t * t + 1727.5 * t * t * t
        }

        if (year in 2050.0..<2150.0) {
            t = (year - 1820) / 100
            return -20 + 32 * t * t - 0.5628 * (2150 - year)
        }

        t = (year - 2000) / 100
        return 64.7 + 64.5 * t + 0.25 * t * t
    }
}
