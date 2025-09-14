# --- Base ---
-repackageclasses 'o'
-allowaccessmodification
-keepattributes SourceFile,LineNumberTable

# --- WebView & JS ---
-keepclassmembers class * extends android.webkit.WebViewClient {
    public boolean *(...);
}
-keepclassmembers class * extends android.webkit.WebView {
    *** javascriptInterface;
}
-keepclassmembers class com.exam.exammodwithx.WebviewActivity$JSBridge {
    public <methods>;
}

# --- Native ---
-keep class com.exam.exammodwithx.SecureNative {
    native <methods>;
}

# --- Model ---
-keep class com.exam.exammodwithx.** { *; }

# --- Toasty ---
-keep class es.dmoral.toasty.** { *; }

# --- Google ---
-dontwarn com.google.**
-keep class com.google.** { *; }
