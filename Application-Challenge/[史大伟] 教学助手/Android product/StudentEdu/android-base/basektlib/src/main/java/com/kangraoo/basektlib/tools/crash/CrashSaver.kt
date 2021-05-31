package com.kangraoo.basektlib.tools.crash

import android.content.Context
import android.text.TextUtils
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.exception.LibPermissionException
import com.kangraoo.basektlib.exception.LibStorageException
import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.crash.CrashSnapshot.snapshot
import com.kangraoo.basektlib.tools.encryption.MessageDigestUtils
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import java.io.*

object CrashSaver {
    private const val TAG = "CrashSaver"
    const val FIX = ".crashlog"
    @JvmStatic
    fun save(
        context: Context,
        ex: Throwable,
        uncaught: Boolean
    ) {
        try {
            UStorage.storageCheck(StorageType.TYPE_LOG)
            var writer: Writer? = null
            var printWriter: PrintWriter? = null
            var stackTrace = ""
            try {
                writer = StringWriter()
                printWriter = PrintWriter(writer)
                ex.printStackTrace(printWriter)
                var cause = ex.cause
                while (cause != null) {
                    cause.printStackTrace(printWriter)
                    cause = cause.cause
                }
                stackTrace = writer.toString()
            } catch (e: Exception) {
                ULog.e(e, e.message)
            } finally {
                if (writer != null) {
                    try {
                        writer.close()
                    } catch (e: IOException) {
                        ULog.e(e, e.message)
                    }
                }
                printWriter?.close()
            }
            val signature = stackTrace.replace("\\([^\\(]*\\)".toRegex(), "")
            val filename: String = MessageDigestUtils.md5(signature)
            if (TextUtils.isEmpty(filename)) {
                return
            }
            val timestamp: String = UTime.nowDateTime(UTime.YMDHMS)
            var mBufferedWriter: BufferedWriter? = null
            try {
                UStorage.storageCheck(StorageType.TYPE_LOG)
                val mFile = File(
                    UStorage.getWritePath(
                        filename + FIX, StorageType.TYPE_LOG
                    )
                )
                val pFile: File = mFile.parentFile
                if (!pFile.exists()) { // 如果文件夹不存在，则先创建文件夹
                    pFile.mkdirs()
                }
                var count = 1
                if (mFile.exists()) {
                    var reader: LineNumberReader? = null
                    try {
                        reader = LineNumberReader(FileReader(mFile))
                        val line = reader.readLine()
                        if (line.startsWith("count")) {
                            var index = line.indexOf(":")
                            if (index != -1) {
                                var count_str = line.substring(++index)
                                if (count_str != null) {
                                    count_str = count_str.trim { it <= ' ' }
                                    count = count_str.toInt()
                                    count++
                                }
                            }
                        }
                    } catch (e: Exception) {
                        ULog.e(e, e.message)
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close()
                            } catch (e: Exception) {
                                ULog.e(e, e.message)
                            }
                        }
                    }
                    mFile.delete()
                }
                mFile.createNewFile()
                mBufferedWriter = BufferedWriter(FileWriter(mFile, true)) // 追加模式写文件
                mBufferedWriter.append(
                    snapshot(
                        context,
                        uncaught,
                        timestamp,
                        stackTrace,
                        count
                    )
                )
                mBufferedWriter.flush()
                ULog.i(context.getString(R.string.libCrashLogInPhone))
            } catch (e: Exception) {
                ULog.e(e, e.message)
            } finally {
                if (mBufferedWriter != null) {
                    try {
                        mBufferedWriter.close()
                    } catch (e: IOException) {
                        ULog.e(e, e.message)
                    }
                }
            }
        } catch (e: LibStorageException) {
            ULog.e(e, e.message)
        } catch (e: LibPermissionException) {
            ULog.e(e, e.message)
        }
    }
}
