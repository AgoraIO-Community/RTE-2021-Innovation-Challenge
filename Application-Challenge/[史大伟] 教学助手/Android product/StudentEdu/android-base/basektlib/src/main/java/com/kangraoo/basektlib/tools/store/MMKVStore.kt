package com.kangraoo.basektlib.tools.store

import android.os.Parcelable
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.exception.LibGetDataException
import com.kangraoo.basektlib.exception.LibSaveDataException
import com.kangraoo.basektlib.tools.encryption.HEncryption
import com.kangraoo.basektlib.tools.log.ULog
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVHandler
import com.tencent.mmkv.MMKVLogLevel
import com.tencent.mmkv.MMKVRecoverStrategic

class MMKVStore private constructor(name: String, mode: Int, cryptKey: String?) {
    val mmkv: MMKV = MMKV.mmkvWithID(name, mode, cryptKey)

    companion object {

        val init by lazy {
            MMKV.initialize(SApplication.context())
            MMKV.registerHandler(object : MMKVHandler {
                override fun onMMKVCRCCheckFail(p0: String?): MMKVRecoverStrategic {
                    return MMKVRecoverStrategic.OnErrorRecover
                }

                override fun wantLogRedirecting(): Boolean {
                    return true
                }

                override fun mmkvLog(level: MMKVLogLevel?, file: String?, line: Int, func: String?, message: String?) {
                    val log = "<" + file.toString() + ":" + line.toString() + "::" + func.toString() + "> " + message
                    when (level) {
                        MMKVLogLevel.LevelDebug -> {
                            ULog.d(log)
                        }
                        MMKVLogLevel.LevelInfo -> {
                            ULog.i(log)
                        }
                        MMKVLogLevel.LevelWarning -> {
                            ULog.w(log)
                        }
                        MMKVLogLevel.LevelError -> {
                            ULog.e(log)
                        }
                        MMKVLogLevel.LevelNone -> {
//                            ULog.e(log)
                        }
                    }
                }

                override fun onMMKVFileLengthError(p0: String?): MMKVRecoverStrategic {
                    return MMKVRecoverStrategic.OnErrorRecover
                }
            })
        }

        private val mmkvs: MutableMap<String, MMKVStore> by lazy {
            HashMap<String, MMKVStore>()
        }
//        fun instance(name: String) = instance(name, 1, null)
        fun instance(name: String, mode: Int = 1, cryptKey: String? = null): MMKVStore {
            init
            val kname = name + "mode" + mode.toString()
            if (!mmkvs.containsKey(kname)) {
                synchronized(mmkvs) {
                    if (!mmkvs.containsKey(kname)) {
                        mmkvs[kname] = MMKVStore(name, mode, cryptKey)
                    }
                }
            }
            return mmkvs[kname]!!
        }

        fun instanceSafe(name: String, mode: Int = 1): MMKVStore {
            return instance(name, mode, HEncryption.mLibKeyString)
        }

        fun getAllMMKVStore(): Map<String, MMKVStore> = mmkvs
    }

    fun put(key: String, value: Any) {
        when (value) {
            is String -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            is Boolean -> mmkv.encode(key, value)
            is Int -> mmkv.encode(key, value)
            is Long -> mmkv.encode(key, value)
            is Double -> mmkv.encode(key, value)
            is ByteArray -> mmkv.encode(key, value)
            else -> throw LibSaveDataException(
                SApplication.context().getString(R.string.libSaveErrorMessage)
            )
        }
    }

    fun <T : Parcelable> put(key: String, t: T) {
        mmkv.encode(key, t)
    }

    fun put(key: String, sets: Set<String>) {
        mmkv.encode(key, sets)
    }

    fun <T> get(key: String, defaultValue: T?, returnType: Class<T>): T? {
        when {
            returnType.isAssignableFrom(Int::class.java) -> {
                return mmkv.decodeInt(key, defaultValue as Int) as T
            }
            returnType.isAssignableFrom(Boolean::class.java) -> {
                return mmkv.decodeBool(key, defaultValue as Boolean) as T
            }
            returnType.isAssignableFrom(Long::class.java) -> {
                return mmkv.decodeLong(key, defaultValue as Long) as T
            }
            returnType.isAssignableFrom(Double::class.java) -> {
                return mmkv.decodeDouble(key, defaultValue as Double) as T
            }
            returnType.isAssignableFrom(Float::class.java) -> {
                return mmkv.decodeFloat(key, defaultValue as Float)as T
            }
            returnType.isAssignableFrom(ByteArray::class.java) -> {
                return mmkv.decodeBytes(key, defaultValue as ByteArray) as T
            }
            Set::class.java.isAssignableFrom(returnType) -> {
                return mmkv.decodeStringSet(key, defaultValue as Set<String>?) as T?
            }
            returnType.isAssignableFrom(String::class.java) -> {
                return mmkv.decodeString(key, defaultValue?.toString()) as T?
            }
            else -> {
                throw LibGetDataException(
                    SApplication.context().getString(R.string.libGetErrorMessage)
                )
            }
        }
    }

    fun <T : Parcelable> get(key: String, defaultValue: T?, returnType: Class<T>): T? {
        return mmkv.decodeParcelable(key, returnType as Class<Parcelable>) as T?
    }

    fun remove(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun contains(key: String): Boolean {
        return mmkv.containsKey(key)
    }

    fun clear() {
        mmkv.clearAll()
    }

//    val all: Map<String, *>
//        get() = mmkv.all
}
