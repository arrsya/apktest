# Keep JS bridge (runtime reflection)
-keepclassmembers class com.exam.exammodwithx.WebviewActivity$JSBridge {
   public *;
}

# Keep native methods (if any)
-keepclasseswithmembernames class * {
    native <methods>;
}

# Strip logging
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Repackage everything into single short package
-repackageclasses 'o'
-allowaccessmodification
