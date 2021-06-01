package com.kangraoo.basektlib.tools.crash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.text.TextUtils
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HNetwork
import com.kangraoo.basektlib.tools.SSystem
import com.kangraoo.basektlib.tools.log.ULog
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import java.util.regex.Pattern

object CrashSnapshot {
    private const val TAG = "CrashSnapshot"
    private var mTotalMemory: Long = -1
    private fun parseFile(file: File, filter: String): String? {
        var str: String? = null
        if (file.exists()) {
            FileReader(file).use { fit ->
                var br = BufferedReader(fit, 1024)
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    val pattern =
                            Pattern.compile("\\s*:\\s*")
                    val ret = pattern.split(line, 2)
                    if (ret != null && ret.size > 1 && ret[0] == filter) {
                        str = ret[1]
                        break
                    }
                }
            }
        }
        return str
    }

    private fun getSize(size: String, uint: String, factor: Int): Long {
        return size.split(uint.toRegex()).toTypedArray()[0].trim { it <= ' ' }
            .toLong() * factor
    }

    @get:Synchronized
    private val totalMemory: Long
        private get() {
            if (mTotalMemory == -1L) {
                var total = 0L
                var str: String
                try {
                    if (!TextUtils.isEmpty(
                            parseFile(
                                File("/proc/meminfo"), "MemTotal"
                            ).also { str = it!! }
                        )
                    ) {
                        str = str.toUpperCase(Locale.US)
                        total = if (str.endsWith("KB")) {
                            getSize(str, "KB", 1024)
                        } else if (str.endsWith("MB")) {
                            getSize(str, "MB", 1048576)
                        } else if (str.endsWith("GB")) {
                            getSize(str, "GB", 1073741824)
                        } else {
                            -1
                        }
                    }
                } catch (e: Exception) {
                    ULog.e(e, e.message)
                }
                mTotalMemory = total
            }
            return mTotalMemory
        }

    @get:SuppressLint("NewApi")
    private val sdCardMemory: LongArray
        private get() {
            val sdCardInfo = LongArray(2)
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                val sdcardDir = Environment.getExternalStorageDirectory()
                val sf = StatFs(sdcardDir.path)
                if (Build.VERSION.SDK_INT >= 18) {
                    val bSize = sf.blockSizeLong
                    val bCount = sf.blockCountLong
                    val availBlocks = sf.availableBlocksLong
                    sdCardInfo[0] = bSize * bCount
                    sdCardInfo[1] = bSize * availBlocks
                } else {
                    val bSize = sf.blockSize.toLong()
                    val bCount = sf.blockCount.toLong()
                    val availBlocks = sf.availableBlocks.toLong()
                    sdCardInfo[0] = bSize * bCount
                    sdCardInfo[1] = bSize * availBlocks
                }
            }
            return sdCardInfo
        }

    private fun disk(): String {
        val info = sdCardMemory
        val total = info[0]
        val avail = info[1]
        return if (total <= 0) {
            "--"
        } else {
            val ratio = (avail * 100 / total).toFloat()
            String.format(
                Locale.US,
                "%.01f%% [%s]",
                ratio,
                getSizeWithUnit(total)
            )
        }
    }

    private fun ram(): String {
        val total = totalMemory
        val avail: Long = SSystem.getAvailMemory(SApplication.context())
        return if (total <= 0) {
            "--"
        } else {
            val ratio = (avail * 100 / total).toFloat()
            String.format(
                Locale.US,
                "%.01f%% [%s]",
                ratio,
                getSizeWithUnit(total)
            )
        }
    }

    private fun getSizeWithUnit(size: Long): String {
        return when {
            size >= 1073741824 -> {
                val i = (size / 1073741824).toFloat()
                String.format(Locale.US, "%.02f GB", i)
            }
            size >= 1048576 -> {
                val i = (size / 1048576).toFloat()
                String.format(Locale.US, "%.02f MB", i)
            }
            else -> {
                val i = (size / 1024).toFloat()
                String.format(Locale.US, "%.02f KB", i)
            }
        }
    }

    @JvmStatic
    fun snapshot(
        context: Context,
        uncaught: Boolean,
        timestamp: String,
        trace: String?,
        count: Int
    ): String {

        val info: MutableMap<String, String> =
            LinkedHashMap()
        info["count: "] = count.toString()
        info["time: "] = timestamp
        info["device: "] = SSystem.getPhoneModelWithManufacturer()
        info["android: "] = SSystem.getOsInfo()
        info["system: "] = Build.DISPLAY
        info["battery: "] = SSystem.battery(context)
        info["rooted: "] = if (SSystem.isRooted(context)) "yes" else "no"
        info["ram: "] = ram()
        info["disk: "] = disk()
        info["ver: "] = java.lang.String.format(Locale.getDefault(), "%d", SSystem.getVersionCode(context))
        info["caught: "] = if (uncaught) "no" else "yes"
        info["network: "] = HNetwork.getNetworkInfo(context)
        val iterator: Iterator<Map.Entry<String, String>> =
            info.entries.iterator()
        val sb = StringBuilder()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry != null) {
                sb.append(entry.key).append(entry.value)
                sb.append(System.getProperty("line.separator"))
            }
        }
        sb.append(System.getProperty("line.separator"))
        sb.append(trace)
        sb.append(System.getProperty("line.separator"))
        sb.append(System.getProperty("line.separator"))
        sb.append(System.getProperty("line.separator"))
        return sb.toString()
    }
}
