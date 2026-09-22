# Changelog

All notable changes to the Santali Calendar library will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-09-22

### Added

- **Core calendar computation** using Jean Meeus astronomical algorithms
- **SantaliCalendar** main API class with year/month/day resolution
- **Lunar month calculation** based on Chandradarshan (first moon visibility)
- **Moon phase tracking**: new moon, full moon, and all 8 phases
- **18 Santali festivals** with date resolution for any year:
  - Moon-relative festivals (offset from new/full moon)
  - Fixed Gregorian festivals (Hul Maha, Adivasi Diwas, etc.)
- **Leap year support** via 19-year Metonic cycle (7 leap positions)
- **Calendar grid builder** with weekday alignment and month padding
- **Ol Chiki numeral conversion** for day display
- **Ol Chiki weekday names** for all 7 days
- **Santali month names** in both Ol Chiki and Roman script
- **Caching** for month and year computations
- **Purnima/Amavasya detection** for each day
- **Extension functions**: `getDaysInMonth`, `isLeapYear`, `getCurrentMonth`, `getFestivals`
- **175 unit tests** covering all library components
- **Maven Central publishing** configuration with GPG signing
- **Consumer ProGuard rules** for library consumers

### Technical Details

- **Astronomical algorithm**: Meeus (based on *Astronomical Algorithms*)
- **Delta T calculation** for accurate historical/future dates
- **IST offset**: 5 hours 30 minutes (UTC+5:30)
- **Day boundary**: 5:30 PM IST (11:30 AM UTC)
- **Metonic cycle start**: 2026
- **Anchor new moon**: January 22, 2023 (02:23 IST)

### Dependencies

- `androidx.annotation:annotation:1.7.1` (for `@RequiresApi`)
- JUnit 4 for testing

### Platform

- Min SDK: 23 (Android 6.0)
- Compile SDK: 35
- Java 11+
- Kotlin 1.9+
