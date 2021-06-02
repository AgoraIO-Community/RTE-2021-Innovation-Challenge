package com.kangraoo.basektlib.tools.store.filestorage

import android.os.Environment
import android.text.TextUtils
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.SSystem
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import java.io.File
private const val NO_MEDIA_FILE_NAME = ".nomedia"

/** package  */
internal class ExternalStorage {

    /**
     * 屏蔽软件扫描
     */
    private var hasPermission = true // 是否拥有存储卡权限

    var sdkStorageRoot: String? = null
        private set

    init {
        /**
         * 判断权限
         */
        hasPermission = SSystem.checkSdcardPermission(SApplication.context())
        val storageRoot = SApplication.instance().sConfiger.storageRoot
        if (!TextUtils.isEmpty(storageRoot)) {
            val dir = File(storageRoot)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            if (dir.exists() && !dir.isFile) {
                sdkStorageRoot = storageRoot
                if (!storageRoot!!.endsWith("/")) {
                    sdkStorageRoot = "$storageRoot/"
                }
            }
        }
        if (TextUtils.isEmpty(sdkStorageRoot)) {
            loadStorageState()
        }
        SApplication.instance().sConfiger.storageRoot = sdkStorageRoot
        createSubFolders()
    }

    private fun loadStorageState() {
        sdkStorageRoot = "${SApplication.context().getExternalFilesDir(null)?.path ?: SApplication.context().filesDir.path}/"
    }

    /**
     * 构建基础文件夹
     */
    private fun createSubFolders() {
        var result = true
        val root = File(sdkStorageRoot)
        if (root.exists() && !root.isDirectory) {
            root.delete()
        }
        for (storageType in StorageType.values()) {
            result = result && makeDirectory(storageType)
        }
        if (result) {
            createNoMediaFile(sdkStorageRoot)
        }
    }

    fun makeDirectory(storageType: StorageType): Boolean {
        return AttachmentStore.makeDirectory(sdkStorageRoot + storageType.storageDirectoryName)
    }

    private fun createNoMediaFile(path: String?) {
        AttachmentStore.create("$path/$NO_MEDIA_FILE_NAME")
    }

    /**
     * 文件全名转绝对路径（写）
     *
     * @param fileName
     * 文件全名（文件名.扩展名）
     * @return 返回绝对路径信息
     */
    fun getWritePath(fileName: String, fileType: StorageType): String {
        return pathForName(fileName, fileType, false, false)
    }

    private fun pathForName(
        fileName: String,
        type: StorageType,
        dir: Boolean,
        check: Boolean
    ): String {
        val directory = getDirectoryByDirType(type)
        val path = StringBuilder(directory)
        if (!dir) {
            path.append(fileName)
        }
        val pathString = path.toString()
        val file = File(pathString)
        return if (check) {
            if (file.exists()) {
                if (dir && file.isDirectory ||
                    !dir && !file.isDirectory
                ) {
                    return pathString
                }
            }
            ""
        } else {
            pathString
        }
    }

    /**
     * 返回指定类型的文件夹路径
     *
     * @param fileType
     * @return
     */
    fun getDirectoryByDirType(fileType: StorageType): String {
        return sdkStorageRoot + fileType.storageDirectoryName
    }

    /**
     * 根据输入的文件名和类型，找到该文件的全路径。
     * @param fileName
     * @param fileType
     * @return 如果存在该文件，返回路径，否则返回空
     */
    fun getReadPath(fileName: String, fileType: StorageType) = if (fileName.isEmpty()) "" else pathForName(fileName, fileType, false, true)

    /**
     * 判断sd卡是否在状态，可用
     * @return
     */
    val isSdkStorageReady: Boolean
        get() {

            val externalRoot =
                SApplication.context().getExternalFilesDir(null)?.absolutePath
            return if (externalRoot != null) {
                if (sdkStorageRoot!!.startsWith(externalRoot)) {
                    Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
                } else {
                    true
                }
            } else {
                false
            }
        }

    /**
     * 获取存储路径剩余空间
     * @return
     */
    val availableExternalSize: Long
        get() = SSystem.getResidualSpace(sdkStorageRoot)

    /**
     * 有效性检查
     */
    fun checkStorageValid(): Boolean {
        if (hasPermission) {
            return true // M以下版本&授权过的M版本不需要检查
        }
        hasPermission = SSystem.checkSdcardPermission(SApplication.context()) // 检查是否已经获取权限了
        if (hasPermission) {
            ULog.i("get permission to access storage")
            // 已经重新获得权限，那么重新检查一遍初始化过程
            createSubFolders()
        }
        return hasPermission
    }
}
