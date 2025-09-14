# --- SUPER AGRESIF ---
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose

# Hanya keep entry-point (Application & Activities)
-keep public class com.exam.exammodwithx.ExamApplication { *; }
-keep public class com.exam.exammodwithx.MainActivity { *; }
-keep public class com.exam.exammodwithx.WebviewActivity { *; }
-keep public class com.exam.exammodwithx.SettingsActivity { *; }

# WebView & JS interface
-keepclassmembers class com.exam.exammodwithx.WebviewActivity$JSBridge { public *; }
-keepclassmembers class * extends android.webkit.WebViewClient { public *; }
-keepclassmembers class * extends android.webkit.WebView { *; }

# Native
-keep class com.exam.exammodwithx.SecureNative { native <methods>; }

# Hapus semua logging
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
}

# Acak semua yang tersisa
-repackageclasses 'o'
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
