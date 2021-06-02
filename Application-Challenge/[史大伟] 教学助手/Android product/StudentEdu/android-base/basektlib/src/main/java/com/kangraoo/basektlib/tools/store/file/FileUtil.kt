package com.kangraoo.basektlib.tools.store.file

import android.text.TextUtils
import android.webkit.MimeTypeMap
import com.kangraoo.basektlib.tools.log.ULog
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.util.*

/**
 * 文件基本操作
 */
object FileUtil {

    /**
     * 判断是否有后缀
     * @param filename
     * @return
     */
    fun hasExtentsion(filename: String) = getExtensionName(filename).isNotEmpty()

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    fun getExtensionName(filename: String): String {
        if (filename.isNotEmpty()) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length - 1) {
                return filename.substring(dot + 1)
            }
        }
        return ""
    }

    /**
     * 获取文件名
     * @param filepath
     * @return
     */
    fun getFileNameFromPath(filepath: String): String {
        if (!TextUtils.isEmpty(filepath)) {
            val sep = filepath.lastIndexOf('/')
            if (sep > -1 && sep < filepath.length - 1) {
                return filepath.substring(sep + 1)
            }
        }
        return filepath
    }

    /**
     * 获取不带扩展名的文件名
     * @param filename
     * @return
     */
    fun getFileNameNoEx(filename: String): String {
        if (!TextUtils.isEmpty(filename)) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length) {
                return filename.substring(0, dot)
            }
        }
        return filename
    }

    /**
     * 获取MimeType
     * @param filePath
     * @return
     */
    fun getMimeType(filePath: String): String {
        if (filePath.isEmpty()) {
            return ""
        }
        var type: String = ""
        val extension =
            getExtensionName(filePath.toLowerCase(Locale.getDefault()))
        if (extension.isNotEmpty()) {
            val mime = MimeTypeMap.getSingleton()
            type = mime.getMimeTypeFromExtension(extension).toString()
        }
        ULog.i("url:", filePath, " ", "type:", type)
        if (TextUtils.isEmpty(type)) {
            if (filePath.endsWith("aac")) {
                type = "audio/aac"
            } else {
                val ext: String? = FileType.getFileType(filePath)
                if (ext != null) {
                    val mime = MimeTypeMap.getSingleton()
                    type = mime.getMimeTypeFromExtension(ext).toString()
                }
            }
        }
        return type
    }

    /**
     * 获取文件
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
     * 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() -->
     * SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     * @param file
     * @return
     */
    fun getFolderSize(file: File?): Long {
        var size: Long = 0
        if (file != null) {
            val fileList = file.listFiles()
            if (fileList != null) {
                for (i in fileList.indices) {
                    // 如果下面还有文件
                    size = if (fileList[i].isDirectory) {
                        size + getFolderSize(fileList[i])
                    } else {
                        size + fileList[i].length()
                    }
                }
            }
        }
        return size
    }

    /**
     * 格式化单位，获取文件大小
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 =
                BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 =
                BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 =
                BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 获得文件字节大小
     * @param srcPath
     * @return
     */
    fun getFileLength(srcPath: String): Long {
        if (srcPath.isEmpty()) return -1
        val srcFile = File(srcPath)
        return if (!srcFile.exists()) {
            -1
        } else srcFile.length()
    }

    /**
     * 文件是否存在
     * @param path
     * @return
     */
    fun isFileExist(path: String) = path.isNotEmpty() && File(path).exists()

    /**
     * 获取目录大小
     *
     * @param dirPath 目录路径
     * @return 文件大小
     */
    fun getDirSize(dirPath: String?): String? {
        return getDirSize(getFileByPath(dirPath))
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    @JvmStatic fun getFileByPath(filePath: String?): File? {
        return if (filePath != null && filePath.isNotEmpty()) File(filePath) else null
    }

    /**
     * 获取目录大小
     *
     * @param dir 目录
     * @return 文件大小
     */
    fun getDirSize(dir: File?): String? {
        val len: Long = getFolderSize(dir)
        return getFormatSize(len.toDouble())
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param filePath 文件路径
     * @return `true`: 创建成功<br></br>`false`: 创建失败
     */
    @JvmStatic fun createFileByDeleteOldFile(filePath: String?): Boolean {
        return createFileByDeleteOldFile(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param file 文件
     * @return `true`: 创建成功<br></br>`false`: 创建失败
     */
    fun createFileByDeleteOldFile(file: File?): Boolean {
        if (file == null) return false
        // 文件存在并且删除失败返回false
        if (file.exists() && file.isFile && !file.delete()) return false
        // 创建目录失败返回false
        return if (!createOrExistsDir(file.parentFile)) false else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsDir(file: File?): Boolean {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }
}
