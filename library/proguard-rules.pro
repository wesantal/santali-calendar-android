# ProGuard rules for Santali Calendar Library
# Add project specific ProGuard rules here.

# Keep public API classes and methods
-keep class org.wesantal.santalicalendar.** { *; }
-keep class org.wesantal.santalicalendar.calendar.** { *; }
-keep class org.wesantal.santalicalendar.festival.** { *; }
-keep class org.wesantal.santalicalendar.moon.** { *; }
-keep class org.wesantal.santalicalendar.core.** { *; }
-keep class org.wesantal.santalicalendar.utils.** { *; }

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep data class members
-keepclassmembers class * {
    @kotlin.jvm.JvmField *;
}
