package org.wesantal.santalicalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wesantal.santalicalendar.festival.FestivalData
import org.wesantal.santalicalendar.festival.MoonPhase
import org.wesantal.santalicalendar.festival.SantaliFestivalRule
import org.wesantal.santalicalendar.festival.SantaliFestivalType

class FestivalDataTest {

    @Test
    fun `santaliFestivals is not empty`() {
        assertTrue("Festival list should not be empty", FestivalData.santaliFestivals.isNotEmpty())
    }

    @Test
    fun `all festivals have unique ids`() {
        val ids = FestivalData.santaliFestivals.map { it.id }
        assertEquals("All festival IDs should be unique", ids.size, ids.toSet().size)
    }

    @Test
    fun `all festivals have non-empty names`() {
        for ((id, name) in FestivalData.santaliFestivals) {
            assertTrue("Festival $id should have non-empty name", name.isNotEmpty())
        }
    }

    @Test
    fun `all festivals have non-empty roman names`() {
        for ((id, _, roman) in FestivalData.santaliFestivals) {
            assertTrue("Festival $id should have non-empty roman name", roman.isNotEmpty())
        }
    }

    @Test
    fun `all festivals have valid types`() {
        val validTypes = SantaliFestivalType.entries.toSet()
        for ((id, _, _, type) in FestivalData.santaliFestivals) {
            assertTrue("Festival $id should have valid type", validTypes.contains(type))
        }
    }

    @Test
    fun `FixedGregorian festivals have valid month and day`() {
        val fixedFestivals =
            FestivalData.santaliFestivals.filter { it.rule is SantaliFestivalRule.FixedGregorian }
        for (festival in fixedFestivals) {
            val rule = festival.rule as SantaliFestivalRule.FixedGregorian
            assertTrue("Month should be 1-12, got ${rule.month}", rule.month in 1..12)
            assertTrue("Day should be 1-31, got ${rule.day}", rule.day in 1..31)
        }
    }

    @Test
    fun `MoonRelative festivals have valid month ids`() {
        val moonFestivals =
            FestivalData.santaliFestivals.filter { it.rule is SantaliFestivalRule.MoonRelative }
        for (festival in moonFestivals) {
            val rule = festival.rule as SantaliFestivalRule.MoonRelative
            assertNotNull("Month ID should not be null", rule.monthId)
        }
    }

    @Test
    fun `hul-maha is on June 30`() {
        val hulMaha = FestivalData.santaliFestivals.first { it.id == "hul-maha" }
        val rule = hulMaha.rule as SantaliFestivalRule.FixedGregorian
        assertEquals(6, rule.month)
        assertEquals(30, rule.day)
    }

    @Test
    fun `santali-new-year is on new moon of Mag with no offset`() {
        val newYear = FestivalData.santaliFestivals.first { it.id == "santali-new-year" }
        val rule = newYear.rule as SantaliFestivalRule.MoonRelative
        assertEquals(MoonPhase.NEW_MOON, rule.phase)
        assertEquals(org.wesantal.santalicalendar.calendar.SantaliMonthId.MAG, rule.monthId)
        assertEquals(0, rule.offsetDays)
    }

    @Test
    fun `bidu-chandan is on full moon of Mag with no offset`() {
        val biduChandan = FestivalData.santaliFestivals.first { it.id == "bidu-chandan" }
        val rule = biduChandan.rule as SantaliFestivalRule.MoonRelative
        assertEquals(MoonPhase.FULL_MOON, rule.phase)
        assertEquals(org.wesantal.santalicalendar.calendar.SantaliMonthId.MAG, rule.monthId)
        assertEquals(0, rule.offsetDays)
    }

    @Test
    fun `baha-bonga is 4 days after new moon of Fagun`() {
        val bahaBonga = FestivalData.santaliFestivals.first { it.id == "baha-bonga" }
        val rule = bahaBonga.rule as SantaliFestivalRule.MoonRelative
        assertEquals(MoonPhase.NEW_MOON, rule.phase)
        assertEquals(org.wesantal.santalicalendar.calendar.SantaliMonthId.FAGUN, rule.monthId)
        assertEquals(4, rule.offsetDays)
    }

    @Test
    fun `adivasi-diwas is on August 9`() {
        val adivasiDiwas = FestivalData.santaliFestivals.first { it.id == "adivasi-diwas" }
        val rule = adivasiDiwas.rule as SantaliFestivalRule.FixedGregorian
        assertEquals(8, rule.month)
        assertEquals(9, rule.day)
    }

    @Test
    fun `expected number of festivals`() {
        assertEquals(18, FestivalData.santaliFestivals.size)
    }

    @Test
    fun `all festival types are represented`() {
        val types = FestivalData.santaliFestivals.map { it.type }.toSet()
        assertTrue("Should have FESTIVAL type", types.contains(SantaliFestivalType.FESTIVAL))
        assertTrue("Should have OBSERVANCE type", types.contains(SantaliFestivalType.OBSERVANCE))
        assertTrue("Should have CULTURAL type", types.contains(SantaliFestivalType.CULTURAL))
        assertTrue("Should have COMMUNITY type", types.contains(SantaliFestivalType.COMMUNITY))
        assertTrue(
            "Should have BIRTH_ANNIVERSARY type",
            types.contains(SantaliFestivalType.BIRTH_ANNIVERSARY),
        )
        assertTrue(
            "Should have DEATH_ANNIVERSARY type",
            types.contains(SantaliFestivalType.DEATH_ANNIVERSARY),
        )
    }
}
