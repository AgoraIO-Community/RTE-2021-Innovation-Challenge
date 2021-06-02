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
#noinspection ShrinkerUnresolvedReference
-keep class io.agora.**{*;}
#noinspection ShrinkerUnresolvedReference
-keep class com.huawei.hms.ads.** {*; }
#noinspection ShrinkerUnresolvedReference
-keep interface com.huawei.hms.ads.** {*; }

-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#noinspection ShrinkerUnresolvedReference
-keep class com.hianalytics.android.**{*;}
#noinspection ShrinkerUnresolvedReference
-keep class com.huawei.updatesdk.**{*;}
#noinspection ShrinkerUnresolvedReference
-keep class com.huawei.hms.**{*;}



-dontwarn com.huawei.**
#noinspection ShrinkerUnresolvedReference
-keep class com.huawei.** {*;}
-dontwarn org.slf4j.**
#noinspection ShrinkerUnresolvedReference
-keep class org.slf4j.** {*;}
-dontwarn org.springframework.**
#noinspection ShrinkerUnresolvedReference
-keep class org.springframework.** {*;}
-dontwarn com.fasterxml.jackson.**
#noinspection ShrinkerUnresolvedReference
-keep class com.fasterxml.jackson.** {*;}