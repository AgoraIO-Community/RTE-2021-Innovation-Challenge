package com.kangraoo.basektlib.tools.okhttp

import com.kangraoo.basektlib.app.SYS_PERSISTENT
import com.kangraoo.basektlib.tools.store.MMKVStore
import com.tencent.mmkv.MMKV

const val URL_PERSISTENT = 1
const val HEADER_PERSISTENT = 2
const val ALL_PERSISTENT = 3

/**
 * http持久化管理
 */
class HttpPersistentManager {

    companion object {
        val instance: HttpPersistentManager by lazy() {
            HttpPersistentManager() }
    }

    private fun store(host: String, type: Int) = MMKVStore.instanceSafe(SYS_PERSISTENT + host + "_" + type, MMKV.MULTI_PROCESS_MODE)

    private val headerPersistent = HashMap<String, HashMap<String, String?>>()
    private val urlPersistent = HashMap<String, HashMap<String, String?>>()

    @Synchronized
    fun setPersistent(host: String, key: String, value: String?, type: Int = ALL_PERSISTENT) {
        if (type == ALL_PERSISTENT || type == HEADER_PERSISTENT) {
            if (!headerPersistent.containsKey(host)) {
                headerPersistent[host] = HashMap<String, String?>()
            }
            var keyMap: HashMap<String, String?> = headerPersistent[host]!!
            keyMap[key] = value
            store(host, HEADER_PERSISTENT).put(key, value ?: "")
        }
        if (type == ALL_PERSISTENT || type == URL_PERSISTENT) {
            if (!urlPersistent.containsKey(host)) {
                urlPersistent[host] = HashMap<String, String?>()
            }
            var keyMap: HashMap<String, String?> = urlPersistent[host]!!
            keyMap[key] = value
            store(host, URL_PERSISTENT).put(key, value ?: "")
        }
    }

    @Synchronized
    fun setPersistent(host: String, map: HashMap<String, String?>, type: Int = ALL_PERSISTENT) {
        if (type == ALL_PERSISTENT || type == HEADER_PERSISTENT) {
            if (!headerPersistent.containsKey(host)) {
                headerPersistent[host] = HashMap<String, String?>()
            }
            var keyMap: HashMap<String, String?> = headerPersistent[host]!!
            keyMap.putAll(map)
            keyMap.forEach {
                store(host, HEADER_PERSISTENT).put(it.key, it.value ?: "")
            }
        }
        if (type == ALL_PERSISTENT || type == URL_PERSISTENT) {
            if (!urlPersistent.containsKey(host)) {
                urlPersistent[host] = HashMap<String, String?>()
            }
            var keyMap: HashMap<String, String?> = urlPersistent[host]!!
            keyMap.putAll(map)
            keyMap.forEach {
                store(host, URL_PERSISTENT).put(it.key, it.value ?: "")
            }
        }
    }

    fun getPersistent(host: String, type: Int = ALL_PERSISTENT): HashMap<String, String?>? {
        var map: HashMap<String, String?>? = null
        if (type == ALL_PERSISTENT || type == HEADER_PERSISTENT) {
            var headerMap = if (headerPersistent.containsKey(host)) { headerPersistent[host] } else { null }
            if (headerMap != null) {
                if (map == null) {
                    map = HashMap()
                }
                map.putAll(headerMap)
            }
        }
        if (type == ALL_PERSISTENT || type == URL_PERSISTENT) {
            var urlMap = if (urlPersistent.containsKey(host)) { urlPersistent[host] } else { null }
            if (urlMap != null) {
                if (map == null) {
                    map = HashMap()
                }
                map.putAll(urlMap)
            }
        }
        return map
    }

    @Synchronized
    fun flushPersistent(host: String, type: Int = ALL_PERSISTENT) {
        if (type == ALL_PERSISTENT || type == HEADER_PERSISTENT) {
            var map = all(host, HEADER_PERSISTENT)
            headerPersistent[host]?.clear()
            if (map != null) {
                if (!headerPersistent.containsKey(host)) {
                    headerPersistent[host] = HashMap<String, String?>()
                }
                val keyMap: HashMap<String, String?> = headerPersistent[host]!!
                keyMap.putAll(map)
            }
        }
        if (type == ALL_PERSISTENT || type == URL_PERSISTENT) {
            var map = all(host, URL_PERSISTENT)
            urlPersistent[host]?.clear()
            if (map != null) {
                if (!urlPersistent.containsKey(host)) {
                    urlPersistent[host] = HashMap<String, String?>()
                }
                val keyMap: HashMap<String, String?> = urlPersistent[host]!!
                keyMap.putAll(map)
            }
        }
    }

    @Synchronized
    fun removeAllPersistent(host: String, type: Int = ALL_PERSISTENT) {
        if (type == ALL_PERSISTENT || type == HEADER_PERSISTENT) {
            headerPersistent[host]?.clear()
            store(host, HEADER_PERSISTENT).clear()
        }
        if (type == ALL_PERSISTENT || type == URL_PERSISTENT) {
            urlPersistent[host]?.clear()
            store(host, URL_PERSISTENT).clear()
        }
    }

    private fun all(host: String, type: Int): Map<String, String?>? {
        var mmkvStore = store(host, type)
        var mmkv = mmkvStore.mmkv
        var keys = mmkv.allKeys()
        if (keys != null) {
            var map = HashMap<String, String?>()
            keys.forEach {
                if (it != null) {
                    val value: String? = mmkvStore.get(it, null, String::class.java)
                    map[it] = value
                }
            }
            return map
        }
        return null
    }
}
