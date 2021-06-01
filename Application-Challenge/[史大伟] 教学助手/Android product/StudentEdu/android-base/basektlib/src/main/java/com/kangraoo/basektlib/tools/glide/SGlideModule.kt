package com.kangraoo.basektlib.tools.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.kangraoo.basektlib.exception.LibPermissionException
import com.kangraoo.basektlib.exception.LibStorageException
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.okhttp.UOkHttp
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import com.kangraoo.basektlib.tools.store.filestorage.UStorage.getDirectoryByDirType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage.storageCheck
import java.io.InputStream

/**
 * Created by hyy on 2018/09/17.
 */
@GlideModule
class SGlideModule : AppGlideModule() {
    var cacheSize = 100L * 1024L * 1024L
    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {
        val maxMemory = Runtime.getRuntime().maxMemory().toInt() // 获取系统分配给应用的总内存大小
        val memoryCacheSize = maxMemory / 8 // 设置图片内存缓存占用八分之一
        // 设置内存缓存大小
        builder.setMemoryCache(LruResourceCache(memoryCacheSize.toLong()))
        if (UStorage.initStorage) {
            try {
                storageCheck(StorageType.TYPE_IMAGE)
                var diskLruCacheFactory = DiskLruCacheFactory(
                    getDirectoryByDirType(StorageType.TYPE_IMAGE),
                    cacheSize
                )
                builder.setDiskCache(diskLruCacheFactory)
            } catch (e: LibStorageException) {
                ULog.e(e, e.message)
            } catch (e: LibPermissionException) {
                ULog.e(e, e.message)
            }
        }

        // 设置图片解码格式
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
        //        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        // 设置BitmapPool缓存内存大小
        builder.setBitmapPool(LruBitmapPool(memoryCacheSize.toLong()))
    }

    override fun registerComponents(
        context: Context,
        glide: Glide,
        registry: Registry
    ) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(UOkHttp.instance.imageOkhttp())
        )
    }
}
