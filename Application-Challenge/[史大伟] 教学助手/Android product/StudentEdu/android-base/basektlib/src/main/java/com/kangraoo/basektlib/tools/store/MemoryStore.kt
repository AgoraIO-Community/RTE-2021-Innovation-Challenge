package com.kangraoo.basektlib.tools.store

import android.util.LruCache
import com.kangraoo.basektlib.data.model.SExpModel

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/07/23
 * desc : 内存缓存工具
 */
class MemoryStore private constructor() {

    var cache = LruCache<Any?, Any>(
        Runtime.getRuntime().maxMemory().toInt() / 8
    )

    companion object {
        val instance: MemoryStore by lazy {
            MemoryStore()
        }
    }

    fun put(key: String, value: Any?) {
        cache.put(key, value)
    }

    fun put(map: Map<String, Any?>) {
        for ((key, value) in map) {
            put(key, value)
        }
    }

    fun put(map: Map<String, Any?>, expireMills: Long) {
        for ((key, value) in map) {
            put(key, value, expireMills)
        }
    }

    fun put(key: String, value: Any?, expireMills: Long) {
        put(key, SExpModel(value, expireMills))
    }

    fun <T> get(key: String, defaultValue: T?): T? {
        val o = cache[key]
        return getModel(key, o, defaultValue)
    }

    fun remove(key: String) {
        cache.remove(key)
    }

    fun contains(key: String) = cache.snapshot().containsKey(key)

    fun clear() = cache.evictAll()

    private fun <T> getModel(key: String, o: Any?, defaultValue: T?): T? {
        return if (o != null) {
            if (o is SExpModel) {
                if (o.isExpire()) {
                    remove(key)
                    defaultValue
                } else {
                    o.value as T?
                }
            } else o as T
        } else defaultValue
    }
}
