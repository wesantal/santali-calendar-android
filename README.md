# Santali Calendar

An Ol Chiki (Santali) lunar calendar library for Android, providing accurate moon phase calculations, festival dates, and month/day conversions based on the Metonic cycle.

[![Maven Central](https://img.shields.io/maven-central/v/org.wesantal/santali-calendar)](https://central.sonatype.com/artifact/org.wesantal/santali-calendar)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## Features

- **Lunar month calculation** using the Meeus astronomical algorithm
- **Moon phase tracking** (new moon, full moon, Chandradarshan)
- **18 Santali festivals** with date resolution for any year
- **Ol Chiki numeral conversion** for day display
- **Leap year support** via the 19-year Metonic cycle
- **Calendar grid builder** with weekday alignment and month padding
- **No Android framework dependencies** beyond `androidx.annotation`

## Installation

### Maven Central

```kotlin
// build.gradle.kts
dependencies {
    implementation("org.wesantal:santali-calendar:1.0.4")
}
```

### Local (for testing)

```bash
./gradlew :library:publishReleasePublicationToLocalRepository
```

```kotlin
repositories {
    mavenLocal()
}
dependencies {
    implementation("org.wesantal:santali-calendar:1.0.4")
}
```

## Quick Start

```kotlin
import org.wesantal.santalicalendar.SantaliCalendar

val calendar = SantaliCalendar()

// Get today's Santali date
val today = calendar.getToday()
println("${today.month.roman} ${today.day}, ${today.weekDay}")

// Get all months for a year
val months = calendar.getMonths(2025)
months.forEach { month ->
    println("${month.roman}: ${month.startDate} - ${month.endDate} (${month.totalDays} days)")
}

// Get all festivals for a year
val festivals = calendar.getFestivals(2025)
festivals.forEach { festival ->
    println("${festival.roman}: ${festival.date}")
}

// Check if a year is a leap year
val isLeap = calendar.isLeapYear(2025) // true if 13 months
```

## API Reference

### SantaliCalendar

The main entry point. Create an instance to access calendar operations.

| Method | Description |
|---|---|
| `getToday()` | Returns today's Santali date |
| `getDate(date)` | Converts a `java.util.Date` to Santali date |
| `getMonths(year)` | Returns all Santali months for a Gregorian year |
| `getCalendar(year)` | Returns full calendar year with day grid |
| `getFestivals(year)` | Returns all festivals sorted by date |
| `getMonthIndex(date)` | Returns the month index (0-based) for a date |
| `getMonthFromDate(date)` | Returns the calendar month containing a date |
| `getDaysInMonth(year, month)` | Returns number of days in a Santali month |
| `isLeapYear(year)` | Checks if a year has 13 months |
| `getCurrentMonth()` | Returns the current Santali month |
| `resolveFestival(definition, year)` | Resolves a festival definition to an actual date |

### SantaliMonth

| Field | Type | Description |
|---|---|---|
| `id` | `SantaliMonthId` | Month identifier enum |
| `name` | `String` | Ol Chiki month name |
| `roman` | `String` | Romanized month name |
| `index` | `Int` | 0-based month index |
| `isLeapMonth` | `Boolean` | True for Sarcha (13th month) |
| `startDate` | `Date` | Chandradarshan (month start) |
| `endDate` | `Date` | Next month's Chandradarshan |
| `totalDays` | `Int` | Number of days in the month |
| `newMoon` | `Date` | New moon date |
| `fullMoon` | `Date` | Full moon (Purnima/Kunami) date |

### SantaliDate

| Field | Type | Description |
|---|---|---|
| `day` | `Int` | Day number in the month |
| `date` | `Date` | Gregorian date |
| `weekDay` | `String` | Ol Chiki weekday name |
| `month` | `SantaliMonth` | The Santali month |
| `isPurnima` | `Boolean` | True if full moon day |
| `isAmavasya` | `Boolean` | True if new moon day |
| `isLeapMonth` | `Boolean` | True if in leap month |
| `isFirstMoonDay` | `Boolean` | True if day 1 of month |

### SantaliCalendarMonth

Extends `SantaliMonth` with a calendar grid:

| Field | Type | Description |
|---|---|---|
| `days` | `List<SantaliCalendarDayCell>` | 7-column grid (null for padding) |

### SantaliCalendarDay

| Field | Type | Description |
|---|---|---|
| `day` | `Int` | Day number |
| `date` | `Date` | Gregorian date |
| `weekDay` | `String` | English weekday name |
| `olChikiDay` | `String` | Ol Chiki numeral for the day |
| `santaliWeekDay` | `String` | Ol Chiki weekday name |
| `isToday` | `Boolean` | True if this is today |
| `isAmavasya` | `Boolean` | True if new moon day |
| `isPurnima` | `Boolean` | True if full moon day |
| `isFirstMoonDay` | `Boolean` | True if day 1 |
| `isCurrentMonth` | `Boolean` | True if in the primary month |

### SantaliMonthId

13 enum values:

| Index | Ol Chiki | Roman | Leap? |
|---|---|---|---|
| 0 | ᱢᱟᱜᱽ | Mag | No |
| 1 | ᱯᱷᱟᱹᱜᱩᱱ | Fagun | No |
| 2 | ᱪᱟᱹᱛ | Chaat | No |
| 3 | ᱵᱟᱹᱭᱥᱟᱹᱠ | Baisak | No |
| 4 | ᱡᱷᱮᱸᱴ | Jhent | No |
| 5 | ᱟᱥᱟᱲ | Ashal | No |
| 6 | ᱥᱟᱱ | Saan | No |
| 7 | ᱵᱷᱟᱫᱚᱨ | Bhador | No |
| 8 | ᱫᱟᱥᱟᱸᱭ | Dasany | No |
| 9 | ᱥᱚᱦᱨᱟᱭ | Sohray | No |
| 10 | ᱟᱜᱷᱟᱬ | Aaghan | No |
| 11 | ᱯᱩᱥ | Pus | No |
| 12 | ᱥᱟᱨᱪᱟ ᱪᱟᱸᱫᱳ | Sarcha Chando | Yes |

### SantaliWeekDay

| Index | Ol Chiki | English |
|---|---|---|
| 0 | ᱥᱤᱸᱜᱤ | Sunday |
| 1 | ᱚᱛᱮ | Monday |
| 2 | ᱵᱟᱞᱮ | Tuesday |
| 3 | ᱥᱟᱹᱜᱩᱱ | Wednesday |
| 4 | ᱥᱟᱹᱨᱫᱤ | Thursday |
| 5 | ᱡᱟᱹᱨᱩᱢ | Friday |
| 6 | ᱧᱩᱦᱩᱢ | Saturday |

### Festivals

18 built-in festivals resolved for any year:

| ID | Roman Name | Rule |
|---|---|---|
| `santali-new-year` | Santali New Year | New Moon, Mag, +0 days |
| `mag-bonga` | Mag Bonga | New Moon, Mag, +4 days |
| `pandit-death-anniversary` | Guru Gomke Guru Maha | New Moon, Mag, +6 days |
| `bidu-chandan` | Bidu Chandan | Full Moon, Mag, +0 days |
| `baha-bonga` | Baha Bonga | New Moon, Fagun, +4 days |
| `eroh-bonga` | Eroh | New Moon, Chaat, +4 days |
| `guru-kunami` | Guru Kunami | Full Moon, Baisak, +0 days |
| `dah-serma` | Dah Serma | Full Moon, Jhent, +0 days |
| `asalia-bonga` | Asalia Bonga | New Moon, Ashal, +4 days |
| `hul-maha` | Hul Maha | Gregorian June 30 |
| `hariyali` | Hariyali | Full Moon, Saan, +0 days |
| `jantal` | Jantal | Full Moon, Bhador, +0 days |
| `dasany` | Dasany | New Moon, Dasany, +4 days |
| `sohray` | Sohray | Full Moon, Sohray, +0 days |
| `ir-sid` | Ir Sid | New Moon, Pus, +4 days |
| `parsi-jitkar-maha` | Parsi Jitkar Maha | Gregorian December 22 |
| `baba-tilka-majhi-janam-maha` | Baba Tilka Majhi Janam Maha | Gregorian February 11 |
| `adivasi-diwas` | Adivasi Diwas | Gregorian August 9 |

### Festival Types

| Type | Description |
|---|---|
| `FESTIVAL` | Major cultural/religious festival |
| `BIRTH_ANNIVERSARY` | Birth anniversary of important figures |
| `DEATH_ANNIVERSARY` | Death anniversary / memorial |
| `CULTURAL` | Cultural celebration |
| `COMMUNITY` | Community observance |
| `OBSERVANCE` | General observance |

### Utility Classes

#### OlChikiNumerals

```kotlin
import org.wesantal.santalicalendar.utils.OlChikiNumerals

OlChikiNumerals.toOlChikiNumeral(15)  // "᱑᱕"
OlChikiNumerals.toOlChikiNumeral(42)  // "᱔᱒"
```

#### JulianDate

```kotlin
import org.wesantal.santalicalendar.utils.JulianDate

JulianDate.dateToJulianDay(date)      // Julian Day Number
JulianDate.julianDayToDate(jd)        // java.util.Date
```

## Requirements

- **Min SDK**: 23 (Android 6.0)
- **Compile SDK**: 37
- **Java**: 11+
- **Kotlin**: 1.9+

## How It Works

The library uses the **Jean Meeus astronomical algorithms** to compute:

1. **New moon times** via the mean lunation model with perturbation corrections
2. **Full moon times** using the same algorithm with full moon corrections
3. **Chandradarshan** (first moon visibility) calculated from new moon times
4. **Metonic cycle** (19-year cycle) for leap year determination with 7 leap positions

Months begin at Chandradarshan (not the astronomical new moon), and the day boundary is at 5:30 PM IST (11:30 AM UTC).

## License

```
Copyright 2026 WeSantal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
