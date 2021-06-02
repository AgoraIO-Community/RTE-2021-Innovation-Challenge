package com.kangraoo.basektlib.tools

import android.os.Build
import android.text.TextUtils
import androidx.annotation.StringDef
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * Created by feifan on 2017/8/21.
 * Contacts me:404619986@qq.com
 */
object URom {
    private const val TAG = "URom"
    const val ROM_MIUI = "MIUI"
    const val ROM_EMUI = "EMUI"
    const val ROM_VIVO = "VIVO"
    const val ROM_OPPO = "OPPO"
    const val ROM_FLYME = "FLYME"
    const val ROM_SMARTISAN = "SMARTISAN"
    const val ROM_QIKU = "QIKU"
    const val ROM_LETV = "LETV"
    const val ROM_LENOVO = "LENOVO"
    const val ROM_NUBIA = "NUBIA"
    const val ROM_ZTE = "ZTE"
    const val ROM_COOLPAD = "COOLPAD"
    const val ROM_UNKNOWN = "UNKNOWN"
    private const val SYSTEM_VERSION_MIUI = "ro.miui.ui.version.name"
    private const val SYSTEM_VERSION_EMUI = "ro.build.version.emui"
    private const val SYSTEM_VERSION_VIVO = "ro.vivo.os.version"
    private const val SYSTEM_VERSION_OPPO = "ro.build.version.opporom"
    private const val SYSTEM_VERSION_FLYME = "ro.build.display.id"
    private const val SYSTEM_VERSION_SMARTISAN = "ro.smartisan.version"
    private const val SYSTEM_VERSION_LETV = "ro.letv.eui"
    private const val SYSTEM_VERSION_LENOVO = "ro.lenovo.lvp.version"
    private val map: MutableMap<String, String?> = HashMap()
    private fun getSystemProperty(propName: String): String? {
        if (map.containsKey(propName)) {
            return map[propName]
        }
        synchronized(map) {
            if (map.containsKey(propName)) {
                return map[propName]
            }
            val p = Runtime.getRuntime().exec("getprop $propName")

            map[propName] = p.inputStream.use {
                BufferedReader(InputStreamReader(it), 1024).readLine()
            }
            return map[propName]
        }
    }

    @get:RomName
    var romName: String? = null
        get() {
            if (field != null) {
                return field
            }
            if (isMiuiRom) {
                return ROM_MIUI.also { field = it }
            }
            if (isHuaweiRom) {
                return ROM_EMUI.also { field = it }
            }
            if (isVivoRom) {
                return ROM_VIVO.also { field = it }
            }
            if (isOppoRom) {
                return ROM_OPPO.also { field = it }
            }
            if (isMeizuRom) {
                return ROM_FLYME.also { field = it }
            }
            if (isSmartisanRom) {
                return ROM_SMARTISAN.also { field = it }
            }
            if (is360Rom()) {
                return ROM_QIKU.also { field = it }
            }
            if (isLetvRom) {
                return ROM_LETV.also { field = it }
            }
            if (isLenovoRom) {
                return ROM_LENOVO.also { field = it }
            }
            if (isZTERom) {
                return ROM_ZTE.also { field = it }
            }
            return if (isCoolPadRom) {
                ROM_COOLPAD.also { field = it }
            } else ROM_UNKNOWN.also { field = it }
        }
        private set

    val isMiuiRom: Boolean
        get() = !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_MIUI))

    val isHuaweiRom: Boolean
        get() = !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_EMUI))

    val isVivoRom: Boolean
        get() = !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_VIVO))

    val isOppoRom: Boolean
        get() = !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_OPPO))

    val isMeizuRom: Boolean
        get() {
            val meizuFlymeOSFlag = getSystemProperty(SYSTEM_VERSION_FLYME)
            return !TextUtils.isEmpty(meizuFlymeOSFlag) && meizuFlymeOSFlag!!.toUpperCase(Locale.getDefault()).contains(ROM_FLYME)
        }

    val isSmartisanRom: Boolean
        get() = !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_SMARTISAN))

    fun is360Rom(): Boolean {
        val manufacturer = Build.MANUFACTURER
        return !TextUtils.isEmpty(manufacturer) && manufacturer.toUpperCase(Locale.getDefault()).contains(ROM_QIKU)
    }

    val isLetvRom: Boolean
        get() = !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_LETV))

    val isLenovoRom: Boolean
        get() = !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_LENOVO))

    val isCoolPadRom: Boolean
        get() {
            val model = Build.MODEL
            val fingerPrint = Build.FINGERPRINT
            return (!TextUtils.isEmpty(model) && model.toLowerCase(Locale.getDefault()).contains(ROM_COOLPAD) ||
                    !TextUtils.isEmpty(fingerPrint) && fingerPrint.toLowerCase(Locale.getDefault()).contains(ROM_COOLPAD))
        }

    val isZTERom: Boolean
        get() {
            val manufacturer = Build.MANUFACTURER
            val fingerPrint = Build.FINGERPRINT
            return (!TextUtils.isEmpty(manufacturer) && (fingerPrint.toLowerCase(Locale.getDefault()).contains(ROM_NUBIA) || fingerPrint.toLowerCase(Locale.getDefault()).contains(ROM_ZTE)) ||
                    !TextUtils.isEmpty(fingerPrint) && (fingerPrint.toLowerCase(Locale.getDefault()).contains(ROM_NUBIA) || fingerPrint.toLowerCase(Locale.getDefault()).contains(ROM_ZTE)))
        }

    val isDomesticSpecialRom: Boolean
        get() = (isMiuiRom ||
                isHuaweiRom ||
                isMeizuRom ||
                is360Rom() ||
                isOppoRom ||
                isVivoRom ||
                isLetvRom ||
                isZTERom ||
                isLenovoRom ||
                isCoolPadRom)

    @StringDef(ROM_MIUI, ROM_EMUI, ROM_VIVO, ROM_OPPO, ROM_FLYME, ROM_SMARTISAN, ROM_QIKU, ROM_LETV, ROM_LENOVO, ROM_ZTE, ROM_COOLPAD, ROM_UNKNOWN)
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RomName
}
