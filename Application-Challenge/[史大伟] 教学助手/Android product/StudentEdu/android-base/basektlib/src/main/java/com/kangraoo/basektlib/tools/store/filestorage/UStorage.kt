package com.kangraoo.basektlib.tools.store.filestorage

import android.text.TextUtils
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.exception.LibPermissionException
import com.kangraoo.basektlib.exception.LibStorageException
import com.kangraoo.basektlib.exception.LibStorageNoEnoughSpaceException
import java.io.File
import kotlin.jvm.Throws

/**
 * 需要初始化的存储工具
 * 文件将全部通过这个工具存储在内存卡上
 */
object UStorage {
    /**
     * kb
     */
    private const val K: Long = 1024

    /**
     * mb
     */
    const val M = 1024 * K

    // 外置存储卡默认预警临界值
    private const val THRESHOLD_WARNING_SPACE = 100 * M

    // 保存文件时所需的最小空间的默认值
    const val THRESHOLD_MIN_SPCAE = 20 * M

    var initStorage = false

    private val externalStorage: ExternalStorage by lazy() {
        initStorage = true
        ExternalStorage() }

    val storageRoot: String?
        get() = externalStorage.sdkStorageRoot

    /**
     * 有效性检查
     * @return
     */
    fun checkStorageValid(): Boolean {
        return externalStorage.checkStorageValid()
    }

    /**
     * 判断能否使用存储
     */
    val isExternalStorageExist: Boolean
        get() = externalStorage.isSdkStorageReady

    /**
     * 判断外部存储是否存在，以及是否有足够空间保存指定类型的文件
     *
     * @param fileType
     * @return false: 无存储卡或无空间可写, true: 表示ok
     */
    @Throws(LibStorageException::class)
    fun hasEnoughSpaceForWrite(fileType: StorageType): Boolean {
        if (!externalStorage.isSdkStorageReady) {
            throw LibStorageException(SApplication.context().getString(R.string.libStorageHasWriteCheck))
        }
        val residual: Long = externalStorage.availableExternalSize
        if (residual < fileType.storageMinSize) {
            throw LibStorageNoEnoughSpaceException(SApplication.context().getString(R.string.libStorageNoEnoughSpaceCheck))
        } else if (residual < THRESHOLD_WARNING_SPACE) {
            return true
        }
        return true
    }

    @Throws(LibStorageException::class, LibPermissionException::class)
    fun storageCheck(fileType: StorageType) {
        if (!checkStorageValid()) {
            throw LibPermissionException(SApplication.context().getString(R.string.libStoragePermisssionCheck))
        }
        hasEnoughSpaceForWrite(fileType)
    }

    /**
     * 根据输入的文件名和类型，找到该文件的全路径。
     *
     * @param fileName
     * @param fileType
     * @return 如果存在该文件，返回路径，否则返回空
     */
    fun getReadPath(fileName: String, fileType: StorageType): String {
        return externalStorage.getReadPath(fileName, fileType)
    }

    /**
     * 返回指定类型的文件夹路径
     *
     * @param fileType
     * @return
     */
    fun getDirectoryByDirType(fileType: StorageType): String {
        return externalStorage.getDirectoryByDirType(fileType)
    }

    /**
     *
     * @param fileName
     * @param fileType
     * @return 可用的保存路径或者null
     */
    fun getWritePath(fileName: String, fileType: StorageType): String? {
        val path: String =
            externalStorage.getWritePath(fileName, fileType)
        if (TextUtils.isEmpty(path)) {
            return null
        }
        val dir = File(path).parentFile
        if (dir != null && !dir.exists()) {
            dir.mkdirs()
        }
        return path
    }
}
