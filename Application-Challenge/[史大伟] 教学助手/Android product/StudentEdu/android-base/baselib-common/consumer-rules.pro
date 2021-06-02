#-keep class androidx.recyclerview.widget.**{*;}
#-keep class androidx.viewpager2.widget.**{*;}
#
# -keep class com.gyf.immersionbar.* {*;}
# -dontwarn com.gyf.immersionbar.**
#
#-keep public class com.google.android.material.bottomnavigation.BottomNavigationView { *; }
#-keep public class com.google.android.material.bottomnavigation.BottomNavigationMenuView { *; }
#-keep public class com.google.android.material.bottomnavigation.BottomNavigationPresenter { *; }
#-keep public class com.google.android.material.bottomnavigation.BottomNavigationItemView { *; }
#
#-keep class com.umeng.** {*;}
#-keep class com.uc.** {*;}
#-keepclassmembers class * {
#   public <init> (org.json.JSONObject);
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keep class com.zui.** {*;}
#-keep class com.miui.** {*;}
#-keep class com.heytap.** {*;}
#-keep class a.** {*;}
#-keep class com.vivo.** {*;}
#
##-keep public class [您的应用包名].R$*{
##public static final int *;
##}
#
#-keep class com.umeng.** {*;}
##//您如果集成了U-APM产品可以加入该混淆
#-keep class com.uc.** {*;}
#-keepclassmembers class * {
#   public <init> (org.json.JSONObject);
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
##-keep public class [您的应用包名].R$*{
##public static final int *;
##}
#
#-keep class com.uc.** {*;}

################################个人####################################
-keep class com.qdedu.baselibcommon.data.AppDataModel
-keep class com.qdedu.baselibcommon.bridge.* {*;}
-keep class com.qdedu.baselibcommon.data.model.params.* {*;}
-keep class com.qdedu.baselibcommon.data.model.responses.* {*;}
-keep class com.qdedu.baselibcommon.exception.* {*;}