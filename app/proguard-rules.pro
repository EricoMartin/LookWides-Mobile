# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# This will strip `Log.v`, `Log.d`, and `Log.i` statements and will leave `Log.w` and `Log.e` statements intact.

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int e(...);
    public static int w(...);
    public static int i(...);
}
-dontshrink
-dontobfuscate
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-dontwarn com.mobilefintech09.lookwides.settings.**
-keepclassmembers class com.mobilefintech09.lookwides.settings**{
 *;
}
-keepclassmembers class com.mobilefintech09.lookwides.entities**{
 *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class com.mobilefintech09.lookwides**{
 *;
}

-keepclassmembers class com.mobilefintech09.lookwides.settings**{
 *;
}

-keepclassmembers class com.mobilefintech09.lookwides.R**{
 *;
}

-keep public class * extends android.preference.Preference
-keep public class * extends android.preference.PreferenceFragment
-keep public class * extends android.preference.PreferenceActivity