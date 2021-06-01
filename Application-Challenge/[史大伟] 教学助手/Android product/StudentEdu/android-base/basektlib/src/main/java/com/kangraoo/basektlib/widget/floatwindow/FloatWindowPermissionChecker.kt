package com.kangraoo.basektlib.widget.floatwindow

import android.Manifest
import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication.Companion.context
import com.kangraoo.basektlib.tools.URom
import com.kangraoo.basektlib.tools.URom.isDomesticSpecialRom
import com.kangraoo.basektlib.tools.URom.isMeizuRom
import com.kangraoo.basektlib.tools.URom.romName
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast

/**
 * Created by feifan on 2017/8/21.
 * Contacts me:404619986@qq.com
 */
object FloatWindowPermissionChecker {
    private const val TAG = "FloatWindowPermissionChecker"

    // 无法跳转的提示，应当根据不同的Rom给予不同的提示，比如Oppo应该提示去手机管家里开启，这里偷懒懒得写了
    //    private static final String TOAST_HINT = "无法跳转至权限设置页面，请手动设置或向我反馈";
    @JvmStatic
    fun checkFloatWindowPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context())
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // AppOpsManager添加于API 19
            checkOps()
        } else {
            // 4.4以下一般都可以直接添加悬浮窗
            true
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun checkOps(): Boolean {
        try {
            val `object` = context().getSystemService(Context.APP_OPS_SERVICE)
                ?: return false
            val localClass: Class<*> = `object`.javaClass
            val arrayOfClass: Array<Class<*>?> = arrayOfNulls(3)
            arrayOfClass[0] = Integer.TYPE
            arrayOfClass[1] = Integer.TYPE
            arrayOfClass[2] = String::class.java
            val method = localClass.getMethod("checkOp", *arrayOfClass)
                ?: return false
            val arrayOfObject1 = arrayOfNulls<Any>(3)
            arrayOfObject1[0] = 24
            arrayOfObject1[1] = Binder.getCallingUid()
            arrayOfObject1[2] = context().packageName
            val m = method.invoke(`object`, *arrayOfObject1) as Int
            // 4.4至6.0之间的非国产手机，例如samsung，sony一般都可以直接添加悬浮窗
            return m == AppOpsManager.MODE_ALLOWED || !isDomesticSpecialRom
        } catch (ignore: Exception) {
        }
        return false
    }

    @JvmStatic
    fun askForFloatWindowPermission(context: Context) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.libFloatWindowPermission))
            .setPositiveButton(context.getString(R.string.libYes)) { dialog, which ->
                tryJumpToPermissonPage(context)
                dialog.dismiss()
            }
            .setNegativeButton(context.getString(R.string.libNo)) { dialog, which -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun tryJumpToPermissonPage(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (romName) {
                URom.ROM_MIUI -> applyMiuiPermission(context)
                URom.ROM_EMUI -> applyHuaweiPermission(context)
                URom.ROM_VIVO -> applyVivoPermission(context)
                URom.ROM_OPPO -> applyOppoPermission(context)
                URom.ROM_QIKU -> apply360Permission(context)
                URom.ROM_SMARTISAN -> applySmartisanPermission(context)
                URom.ROM_COOLPAD -> applyCoolpadPermission(context)
                URom.ROM_ZTE -> applyZTEPermission(context)
                URom.ROM_LENOVO -> applyLenovoPermission(context)
                URom.ROM_LETV -> applyLetvPermission(context)
                else -> Toast.makeText(
                    context,
                    context.getString(R.string.libFloatWindowNotJumpPermission),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            if (isMeizuRom) {
                applyMeizuPermission(context)
            } else {
                applyCommonPermission(context)
            }
        }
    }

    private fun startActivitySafely(intent: Intent, context: Context): Boolean {
        return if (isIntentAvailable(intent, context)) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            true
        } else {
            false
        }
    }

    private fun isIntentAvailable(intent: Intent?, context: Context): Boolean {
        return intent != null && context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        ).size > 0
    }

    private fun showAlertToast(context: Context) {
        TipToast.tip(Tip.Error, context.getString(R.string.libFloatWindowNotJumpPermission),
            Toast.LENGTH_LONG)
    }

    private fun applyCommonPermission(context: Context) {
        try {
            val clazz: Class<*> = Settings::class.java
            val field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION")
            val intent = Intent(field[null].toString())
            intent.data = Uri.parse("package:" + context.packageName)
            startActivitySafely(intent, context)
        } catch (e: Exception) {
            showAlertToast(context)
        }
    }

    private fun applyCoolpadPermission(context: Context) {
        val intent = Intent()
        intent.setClassName(
            "com.yulong.android.seccenter",
            "com.yulong.android.seccenter.dataprotection.ui.AppListActivity"
        )
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyLenovoPermission(context: Context) {
        val intent = Intent()
        intent.setClassName(
            "com.lenovo.safecenter",
            "com.lenovo.safecenter.MainTab.LeSafeMainActivity"
        )
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyZTEPermission(context: Context) {
        val intent = Intent()
        intent.action = "com.zte.heartyservice.intent.action.startActivity.PERMISSION_SCANNER"
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyLetvPermission(context: Context) {
        val intent = Intent()
        intent.setClassName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AppActivity")
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyVivoPermission(context: Context) {
        val intent = Intent()
        intent.setClassName(
            "com.iqoo.secure",
            "com.iqoo.secure.ui.phoneoptimize.FloatWindowManager"
        )
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyOppoPermission(context: Context) {
        val intent = Intent()
        intent.putExtra("packageName", context.packageName)
        intent.action = "com.oppo.safe"
        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity")
        if (!startActivitySafely(intent, context)) {
            intent.action = "com.color.safecenter"
            intent.setClassName(
                "com.color.safecenter",
                "com.color.safecenter.permission.floatwindow.FloatWindowListActivity"
            )
            if (!startActivitySafely(intent, context)) {
                intent.action = "com.coloros.safecenter"
                intent.setClassName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity"
                )
                if (!startActivitySafely(intent, context)) {
                    showAlertToast(context)
                }
            }
        }
    }

    private fun apply360Permission(context: Context) {
        val intent = Intent()
        intent.setClassName(
            "com.android.settings",
            "com.android.settings.Settings\$OverlaySettingsActivity"
        )
        if (!startActivitySafely(intent, context)) {
            intent.setClassName(
                "com.qihoo360.mobilesafe",
                "com.qihoo360.mobilesafe.ui.index.AppEnterActivity"
            )
            if (!startActivitySafely(intent, context)) {
                showAlertToast(context)
            }
        }
    }

    private fun applyMiuiPermission(context: Context) {
        val intent = Intent()
        intent.action = "miui.intent.action.APP_PERM_EDITOR"
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra("extra_pkgname", context.packageName)
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyMeizuPermission(context: Context) {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        intent.putExtra("packageName", context.packageName)
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyHuaweiPermission(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            var comp = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity"
            )
            intent.component = comp
            if (!startActivitySafely(intent, context)) {
                comp = ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.notificationmanager.ui.NotificationManagmentActivity"
                )
                intent.component = comp
                context.startActivity(intent)
            }
        } catch (e: SecurityException) {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp =
                ComponentName("com.Android.settings", "com.android.settings.permission.TabItem")
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            showAlertToast(context)
        }
    }

    private fun applySmartisanPermission(context: Context) {
        var intent = Intent("com.smartisanos.security.action.SWITCHED_PERMISSIONS_NEW")
        intent.setClassName(
            "com.smartisanos.security",
            "com.smartisanos.security.SwitchedPermissions"
        )
        intent.putExtra("index", 17) // 有版本差异,不一定定位正确
        if (!startActivitySafely(intent, context)) {
            intent = Intent("com.smartisanos.security.action.SWITCHED_PERMISSIONS")
            intent.setClassName(
                "com.smartisanos.security",
                "com.smartisanos.security.SwitchedPermissions"
            )
            intent.putExtra("permission", arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW))
            if (!startActivitySafely(intent, context)) {
                showAlertToast(context)
            }
        }
    }
}
