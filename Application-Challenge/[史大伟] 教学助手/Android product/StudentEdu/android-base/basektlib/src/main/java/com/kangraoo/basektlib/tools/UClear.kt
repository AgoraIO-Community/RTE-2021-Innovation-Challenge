package com.kangraoo.basektlib.tools

import android.content.Context
import com.kangraoo.basektlib.exception.LibPermissionException
import com.kangraoo.basektlib.exception.LibStorageException
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.DiskCacheStore
import com.kangraoo.basektlib.tools.store.MemoryStore
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import java.io.File

/**
 * 清除类
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2018/09/18
 * desc :
 * version: 1.0
 */
object UClear {
    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    fun cleanInternalCache(context: Context) {
        AttachmentStore.deleteFilesByDirectory(context.cacheDir)
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    fun cleanDatabases(context: Context) {
        AttachmentStore.deleteFilesByDirectory(File("/data/data/" + context.packageName + "/databases"))
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    fun cleanSharedPreference(context: Context) {
        AttachmentStore.deleteFilesByDirectory(File("/data/data/" + context.packageName + "/shared_prefs"))
    }

    /**
     * * 按名字清除本应用数据库 * *
     *
     * @param context
     * @param dbName
     */
    fun cleanDatabaseByName(context: Context, dbName: String?) {
        context.deleteDatabase(dbName)
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    fun cleanFiles(context: Context) {
        AttachmentStore.deleteFilesByDirectory(context.filesDir)
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/)
     *
     * @param context
     */
    fun cleanExternalCache(context: Context?) {
        if (UStorage.storageRoot != null) {
            AttachmentStore.deleteFilesByDirectory(File(UStorage.storageRoot!!))
        }
    }

    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     *
     * @param filePath
     */
    fun cleanCustomCache(filePath: String?) {
        AttachmentStore.deleteFilesByDirectory(File(filePath))
    }

    /**
     * 清除内存缓存
     */
    fun cleanMemoryCache() {
        MemoryStore.instance.clear()
    }

    /**
     * 清除应用文件缓存
     */
    fun cleanDiskCache() {
        try {
            DiskCacheStore.instance.clear()
        } catch (e: LibPermissionException) {
            ULog.e(e)
        } catch (e: LibStorageException) {
            ULog.e(e)
        }
    }

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     * @param filepath
     */
    fun cleanApplicationData(context: Context, vararg filepath: String?) {
        cleanMemoryCache()
        cleanDiskCache()
        cleanInternalCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanSharedPreference(context)
        cleanFiles(context)
        if (filepath == null) {
            return
        }
        for (filePath in filepath) {
            cleanCustomCache(filePath)
        }
    }
}
