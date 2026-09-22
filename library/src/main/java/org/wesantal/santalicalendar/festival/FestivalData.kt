package org.wesantal.santalicalendar.festival

import org.wesantal.santalicalendar.calendar.SantaliMonthId

object FestivalData {
    val santaliFestivals = listOf(
        SantaliFestivalDefinition(
            id = "santali-new-year",
            name = "ᱱᱟᱣᱟ ᱥᱮᱨᱢᱟ",
            roman = "Santali New Year",
            type = SantaliFestivalType.OBSERVANCE,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.NEW_MOON, SantaliMonthId.MAG, 0),
            description = "ᱟᱵᱚᱟᱜ ᱱᱟᱣᱟ ᱥᱮᱨᱢᱟ"
        ),
        SantaliFestivalDefinition(
            id = "mag-bonga",
            name = "ᱢᱟᱜᱽ ᱵᱚᱸᱜᱟ",
            roman = "Mag Bonga",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.NEW_MOON, SantaliMonthId.MAG, 4),
            description = "ᱢᱩᱞᱩᱜ ᱕ ᱟᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "pandit-death-anniversary",
            name = "ᱜᱩᱨᱩ ᱜᱚᱢᱠᱮ ᱜᱩᱨᱩ ᱢᱟᱸᱦᱟ",
            roman = "Guru Gomke Guru Maha",
            type = SantaliFestivalType.DEATH_ANNIVERSARY,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.NEW_MOON, SantaliMonthId.MAG, 6),
            description = "ᱢᱩᱞᱩᱜ ᱗ ᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "bidu-chandan",
            name = "ᱵᱤᱫᱩ ᱪᱟᱸᱫᱟᱱ",
            roman = "Bidu Chandan",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.FULL_MOON, SantaliMonthId.MAG, 0),
            description = "ᱢᱟᱜᱽ ᱠᱩᱱᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "baha-bonga",
            name = "ᱵᱟᱦᱟ ᱵᱚᱸᱜᱟ",
            roman = "Baha Bonga",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.NEW_MOON, SantaliMonthId.FAGUN, 4),
            description = "ᱯᱷᱟᱹᱜᱩᱱ ᱢᱩᱞᱩᱜ ᱕ ᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "eroh-bonga",
            name = "ᱮᱨᱚᱜ ᱵᱚᱸᱜᱟ",
            roman = "Eroh",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.NEW_MOON, SantaliMonthId.CHAAT, 4),
            description = "ᱢᱩᱞᱩᱜ ᱕ ᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "guru-kunami",
            name = "ᱜᱩᱨᱩ ᱠᱩᱹᱱᱟᱹᱢᱤ",
            roman = "Guru Kunami",
            type = SantaliFestivalType.BIRTH_ANNIVERSARY,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.FULL_MOON, SantaliMonthId.BAISAK, 0),
            description = "ᱵᱟᱹᱭᱥᱟᱹᱠ ᱠᱩᱱᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "dah-serma",
            name = "ᱫᱟᱜ ᱥᱮᱨᱢᱟ",
            roman = "Dah Serma",
            type = SantaliFestivalType.OBSERVANCE,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.FULL_MOON, SantaliMonthId.JHENT, 0),
            description = "ᱡᱷᱮᱸᱴ ᱵᱟᱸᱜᱟ ᱠᱩᱱᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "asalia-bonga",
            name = "ᱟᱹᱥᱟᱹᱲᱤᱭᱟᱹ ᱵᱚᱸᱜᱟ",
            roman = "Asalia Bonga",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.NEW_MOON, SantaliMonthId.ASHAL, 4),
            description = "ᱢᱩᱞᱩᱜ ᱕ ᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "hul-maha",
            name = "ᱦᱩᱞ ᱢᱟᱸᱦᱟ",
            roman = "Hul Maha",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.FixedGregorian(6, 30),
            description = "ᱥᱤᱫᱩ, ᱠᱟᱹᱱᱦᱩ, ᱪᱟᱸᱫ, ᱵᱷᱟᱭᱨᱚ, ᱯᱷᱩᱞᱚ ᱟᱨ ᱡᱷᱟᱱᱚ"
        ),
        SantaliFestivalDefinition(
            id = "hariyali",
            name = "ᱦᱟᱹᱨᱤᱭᱟᱹᱲᱤ",
            roman = "Hariyali",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.FULL_MOON, SantaliMonthId.SAAN, 0),
            description = "ᱥᱟᱱ ᱵᱚᱸᱜᱟ ᱠᱩᱱᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "jantal",
            name = "ᱡᱟᱱᱛᱟᱲ",
            roman = "Jantal",
            type = SantaliFestivalType.CULTURAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.FULL_MOON, SantaliMonthId.BHADOR, 0),
            description = "ᱵᱷᱟᱫᱚᱨ ᱵᱚᱸᱜᱟ ᱠᱩᱱᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "dasany",
            name = "ᱫᱟᱥᱟᱸᱭ",
            roman = "Dasany",
            type = SantaliFestivalType.CULTURAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.NEW_MOON, SantaliMonthId.DASANY, 4),
            description = "ᱢᱩᱞᱩᱜ ᱕ ᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "sohray",
            name = "ᱥᱚᱦᱨᱟᱭ",
            roman = "Sohray",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.FULL_MOON, SantaliMonthId.SOHRAY, 0),
            description = "ᱠᱩᱱᱟᱹᱢᱤ ᱥᱚᱦᱨᱟᱭ"
        ),
        SantaliFestivalDefinition(
            id = "ir-sid",
            name = "ᱤᱨ ᱥᱤᱫ",
            roman = "Ir Sid",
            type = SantaliFestivalType.COMMUNITY,
            rule = SantaliFestivalRule.MoonRelative(MoonPhase.NEW_MOON, SantaliMonthId.PUSH, 4),
            description = "ᱢᱩᱞᱩᱜ ᱕ ᱟᱹᱢᱤ"
        ),
        SantaliFestivalDefinition(
            id = "parsi-jitkar-maha",
            name = "ᱯᱟᱹᱨᱥᱤ ᱡᱤᱛᱠᱟᱹᱨ ᱢᱟᱸᱦᱟ",
            roman = "Parsi Jitkar Maha",
            type = SantaliFestivalType.COMMUNITY,
            rule = SantaliFestivalRule.FixedGregorian(12, 22),
            description = "ᱯᱟᱹᱨᱥᱤ ᱡᱤᱛᱠᱟᱹᱨ ᱢᱟᱸᱦᱟ"
        ),
        SantaliFestivalDefinition(
            id = "baba-tilka-majhi-janam-maha",
            name = "ᱵᱟᱵᱟ ᱛᱤᱞᱠᱟᱹ ᱢᱟᱡᱷᱤ ᱡᱟᱱᱟᱢ ᱢᱟᱦᱟ",
            roman = "Baba Tilka Majhi Janam Maha",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.FixedGregorian(2, 11),
            description = "ᱵᱟᱵᱟ ᱛᱤᱞᱠᱟᱹ ᱢᱟᱡᱷᱤ ᱡᱟᱱᱟᱢ ᱢᱟᱦᱟ"
        ),
        SantaliFestivalDefinition(
            id = "adivasi-diwas",
            name = "ᱟᱹᱫᱤᱵᱟᱹᱥᱤ ᱢᱟᱸᱦᱟ",
            roman = "Adivasi Diwas",
            type = SantaliFestivalType.FESTIVAL,
            rule = SantaliFestivalRule.FixedGregorian(8, 9),
            description = "ᱟᱹᱫᱤᱵᱟᱹᱥᱤ ᱢᱟᱸᱦᱟ"
        )
    )
}
