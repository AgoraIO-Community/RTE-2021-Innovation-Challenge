package com.kangraoo.basektlib.tools.store.file

import android.graphics.Bitmap
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.file.FileUtil.getFileByPath
import java.io.*

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/18
 * desc :
 */
object AttachmentStore {

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    fun makeDirectory(path: String): Boolean {
        val file = File(path)
        var exist = file.exists()
        if (!exist) {
            exist = file.mkdirs()
        }
        return exist
    }

    /**
     * 创建文件
     * @param filePath
     * @return
     */
    fun create(filePath: String): File {
        val f = File(filePath)
        if (f.parentFile != null) {
            if (!f.parentFile!!.exists()) { // 如果不存在上级文件夹
                f.parentFile!!.mkdirs()
            }
        }
        f.createNewFile()
        return f
    }

    /**
     * 把文件从文件系统中读取出来
     *
     * @param path
     * @return
     */
    fun load(path: String): ByteArray {
        val file = File(path)
        return file.readBytes()
    }

    /**
     * 把文件读为string
     * @param path
     * @return
     */
    fun loadAsString(path: String): String {
        val file = File(path)
        return file.readText()
    }

    /**
     * 删除指定路径文件
     *
     * @param path
     */
    fun delete(path: String): Boolean {
        var f = File(path)
        return if (f.exists()) {
            f = renameOnDelete(f)
            f.delete()
        } else {
            false
        }
    }

    /**
     * 在删除之前重命名，以避免Android的文件系统锁定
     * @param file
     * @return
     */
    private fun renameOnDelete(file: File): File {
        val tmpPath = "${file.parent}/${System.currentTimeMillis()}_tmp"
        val tmpFile = File(tmpPath)
        return if (file.renameTo(tmpFile)) tmpFile else file
    }

    /**
     * 结束应用删除
     * @param path
     */
    fun deleteOnExit(path: String) {
        val f = File(path)
        if (f.exists()) {
            f.deleteOnExit()
        }
    }

    /**
     * 删除路径
     * @param path
     * @return
     */
    fun deleteDir(path: String): Boolean {
        return deleteDir(path, true)
    }

    /**
     * 删除路径
     * @param path 目录
     * @param rename 是否重命名
     * @return
     */
    fun deleteDir(path: String, rename: Boolean): Boolean {
        var file = File(path)
        if (file.exists()) {
            if (rename) {
                file = renameOnDelete(file)
            }
            val list = file.listFiles()
            if (list != null) {
                val len = list.size
                for (i in 0 until len) {
                    if (list[i].isDirectory) {
                        deleteDir(list[i].path, false)
                    } else {
                        list[i].delete()
                    }
                }
            }
        }
        return file.delete()
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将删除 * *
     *
     * @param directory
     */
    fun deleteFilesByDirectory(directory: File) {
        if (directory.exists()) {
            if (directory.isDirectory) {
                for (item in directory.listFiles()) {
                    if (item.isDirectory) {
                        deleteDir(item.path)
                    } else {
                        delete(item.path)
                    }
                }
            } else {
                delete(directory.path)
            }
        }
    }

    /**
     * 保存图片
     * @param bitmap 图片
     * @param path 路径
     * @param recyle 是否释放
     * @return
     */
    fun saveBitmap(bitmap: Bitmap, path: String, recyle: Boolean, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 80): Boolean {
        return FileOutputStream(path).use {
            val b = bitmap.compress(format, quality, BufferedOutputStream(it))
            if (recyle) {
                bitmap.recycle()
            }
            b
        }
    }

    fun save(path: String, content: String): Long {
        return save(content.toByteArray(), path)
    }

    /**
     * 把数据保存到文件系统中，并且返回其大小
     *
     * @param data
     * @param filePath
     * @return 如果保存失败,则返回-1
     */
    fun save(data: ByteArray, filePath: String): Long {
        val f = create(filePath)
        f.writeBytes(data)
        return f.length()
    }

    /**
     * 保存文件返回大小
     * @param is
     * @param filePath
     * @return 保存失败，返回-1
     */
    fun save(inputStream: InputStream, filePath: String): Long {
        return save(inputStream.readBytes(), filePath)
    }

    /**
     * 把拷贝文件
     * @param srcPath
     * @param dstPath
     * @return
     */
    fun copy(srcPath: String, dstPath: String): Long {
        val source = File(srcPath)
        if (srcPath == dstPath) {
            return source.length()
        }
        val f = create(dstPath)
        source.copyTo(f)
        return f.length()
    }

    /**
     * 将某个文件移动到另一个位置
     * @param srcFilePath
     * @param dstFilePath
     * @return
     */
    fun move(srcFilePath: String, dstFilePath: String): Boolean {
        if (srcFilePath == dstFilePath) {
            ULog.i("文件重命名失败：新旧文件名绝对路径相同")
            return false
        }
        val srcFile = File(srcFilePath)
        val dstFile = create(dstFilePath)

        return srcFile.renameTo(dstFile)
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsFile(filePath: String?): Boolean {
        return createOrExistsFile(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile
        return if (!createOrExistsDir(file.parentFile)) false
        else try {
            file.createNewFile()
        } catch (e: IOException) {
            ULog.e(e)
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
