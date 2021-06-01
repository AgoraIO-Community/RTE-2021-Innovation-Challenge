package com.kangraoo.basektlib.tools.net

import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HString.concatObject
import com.kangraoo.basektlib.tools.HString.getStringValue
import com.kangraoo.basektlib.tools.SSystem
import com.kangraoo.basektlib.tools.UReflection
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ULog.e
import com.kangraoo.basektlib.tools.log.ULog.o
import com.kangraoo.basektlib.tools.store.file.FileUtil.getMimeType
import java.io.File
import java.lang.reflect.Field
import java.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

/**
 * 把map转为请求map
 * Created by sdw on 16/5/4.
 */
object NetMap {
    private const val TAG = "NetMap"
    fun sortParams(map: Map<String, Any?>): Map<String, Any?> {
        val result: MutableMap<String, Any?> = TreeMap { lhs, rhs -> lhs.compareTo(rhs) }
        val keys = map.keys.iterator()
        while (keys.hasNext()) {
            val key = keys.next()
            var temp = map[key]
            if (temp != null) {
                if (temp is Map<*, *>) {
                    if (temp.size > 0) {
                        temp = sortParams(temp as Map<String, Any?>)
                        if ((temp as Map<*, *>?)!!.size > 0) {
                            result[key] = temp
                        }
                    }
                } else if (temp is List<*>) {
                    if (temp.size > 0) {
                        result[key] = temp
                    }
                } else {
                    result[key] = temp
                }
            }
        }
        return result
    }

    /**
     * 正常传值
     * @param params
     * @return
     */
    fun getMultipartParamsMap(params: Map<String, Any>): MultipartBody {
        val builder = MultipartBody.Builder()
        val customerMap: MutableMap<String, Any> = HashMap()
        for ((key, value) in params) {
            if (value is File) {
                val file = value
                val requestBody = file.asRequestBody(getMimeType(file.path).toMediaTypeOrNull())
//                val requestBody = RequestBody.create(MediaType.parse(getMimeType(file.path)), file)
                builder.addFormDataPart(key, file.name, requestBody)
            } else {
                customerMap[key] = value
            }
        }
        val paramMap = httpBuildQueryMap(customerMap)
        for ((key, value) in paramMap) {
            builder.addFormDataPart(key, value)
        }
        o(paramMap)
        builder.setType(MultipartBody.FORM)
        return builder.build()
    }

    /**
     * 对象转map ，对象里数值为null不做处理
     */
    @Throws(IllegalAccessException::class)
    fun paramDataToMap(obj: Any): Map<String, Any> {
        val fieldList: List<Field> = UReflection.getClassField(obj::class.java)
        val data: MutableMap<String, Any> = HashMap()
        for (f in fieldList) {
            f.isAccessible = true
            if (f.name == "serialVersionUID" || f.name == "\$change") {
                continue
            }
            if (!data.containsKey(f.name) && f[obj] != null) {
                data[f.name] = f[obj]
            }
        }
        return data
    }

    fun paramDataToMapNoThrows(obj: Any): Map<String, Any> {
        return try {
            paramDataToMap(obj)
        } catch (e: IllegalAccessException) {
            e(e, e.message)
            HJson.toMap<String, Any>(HJson.toSimpJson(obj))
        }
    }

    fun httpBuildQueryMap(mp: Map<String, Any?>?): Map<String, String> {
        val ret: MutableMap<String, String> = LinkedHashMap()
        if (mp != null) {
            if (mp.size > 0) {
                val entries = mp.entries
                for (entry in entries) {
                    putValue(ret, entry)
                }
            }
        }
        return ret
    }

    private fun putValue(ret: MutableMap<String, String>, entry: Map.Entry<String, Any?>) {
        val value = entry.value
        if (value is Collection<*>) {
            val baseParam: MutableList<String> = ArrayList()
            baseParam.add(entry.key)
            val valueMap = buildQueryMapFromCollection(baseParam, value)
            ret.putAll(valueMap)
        } else if (value is Map<*, *>) {
            val baseParam: MutableList<String> = ArrayList()
            baseParam.add(entry.key)
            val valueMap = buildQueryMapFromMap(baseParam, value as Map<Any, Any>)
            ret.putAll(valueMap)
        } else if (value is Array<*>) {
            val baseParam: MutableList<String> = ArrayList()
            baseParam.add(entry.key)
            val valueMap =
                buildQueryMapFromCollection(baseParam, Arrays.asList(*value as Array<String?>))
            ret.putAll(valueMap)
        } else if (value is File) {
            if (value != null) {
                ret[entry.key] = value.name
            }
        } else {
            if (value != null) {
                ret[entry.key] = getStringValue(value)
            }
        }
    }

    private fun buildQueryMapFromCollection(
        baseParam: List<String>,
        coll: Collection<*>
    ): Map<String, String> {
        var coll: Collection<*> = coll
        val builtQueryMap: MutableMap<String, String> = LinkedHashMap()
        if (coll !is List<*>) {
            coll = ArrayList(coll)
        }
        val arrColl = coll as List<*>
        for (i in arrColl.indices) {
            val value = arrColl[i]
            if (value is Map<*, *>) {
                val baseParam2: MutableList<String> = ArrayList(baseParam)
                baseParam2.add(i.toString())
                val valueMap = buildQueryMapFromMap(baseParam2, value as Map<Any, Any>)
                builtQueryMap.putAll(valueMap)
            } else if (value is List<*>) {
                val baseParam2: MutableList<String> = ArrayList(baseParam)
                baseParam2.add(i.toString())
                val valueMap = buildQueryMapFromCollection(baseParam2, value)
                builtQueryMap.putAll(valueMap)
            } else if (value is Array<*>) {
                val baseParam2: MutableList<String> = ArrayList(baseParam)
                baseParam2.add(i.toString())
                val valueMap =
                    buildQueryMapFromCollection(baseParam2, Arrays.asList(*value as Array<String?>))
                builtQueryMap.putAll(valueMap)
            } else if (value is File) {
                if (value != null) {
                    builtQueryMap[concatObject("", getBaseParamString(baseParam), "[", i, "]")] =
                        value.name
                }
            } else {
                if (value != null) {
                    builtQueryMap[concatObject("", getBaseParamString(baseParam), "[", i, "]")] =
                        getStringValue(value)
                }
            }
        }
        return builtQueryMap
    }

    private fun buildQueryMapFromMap(
        baseParam: List<String>,
        valueMap: Map<Any, Any>
    ): Map<String, String> {
        val builtQueryMap: MutableMap<String, String> = LinkedHashMap()
        val entries = valueMap.entries
        for ((key1, value) in entries) {
            val key = key1.toString()
            if (value is Map<*, *>) {
                val baseParam2: MutableList<String> = ArrayList(baseParam)
                baseParam2.add(key)
                val valueMap2 = buildQueryMapFromMap(baseParam2, value as Map<Any, Any>)
                builtQueryMap.putAll(valueMap2)
            } else if (value is List<*>) {
                val baseParam2: MutableList<String> = ArrayList(baseParam)
                baseParam2.add(key)
                val valueMap2 = buildQueryMapFromCollection(baseParam2, value)
                builtQueryMap.putAll(valueMap2)
            } else if (value is Array<*>) {
                val baseParam2: MutableList<String> = ArrayList(baseParam)
                baseParam2.add(key)
                val valueMap2 =
                    buildQueryMapFromCollection(baseParam2, Arrays.asList(*value as Array<String?>))
                builtQueryMap.putAll(valueMap2)
            } else if (value is File) {
                if (value != null) {
                    builtQueryMap[concatObject("", getBaseParamString(baseParam), "[", key, "]")] =
                        value.name
                }
            } else {
                if (value != null) {
                    builtQueryMap[concatObject("", getBaseParamString(baseParam), "[", key, "]")] =
                        getStringValue(value)
                }
            }
        }
        return builtQueryMap
    }

    //    private static String toStr(Object value){
    //        if(value instanceof String){
    //            return HString.getStringValue(value);
    //        }
    //        if(value instanceof Double||value instanceof Float) {
    //            NumberFormat nf = NumberFormat.getInstance();
    //            nf.setGroupingUsed(false);
    //            return nf.format(value);
    //        }
    //        return HString.getStringValue(value);
    //    }
    private fun getBaseParamString(baseParam: List<String>): String {
        val sb = StringBuilder()
        for (i in baseParam.indices) {
            val s = baseParam[i]
            if (i == 0) {
                sb.append(s)
            } else {
                sb.append("[").append(s).append("]")
            }
        }
        return sb.toString()
    }

    fun baseNetMap(): MutableMap<String, String> {
        val headerMap: MutableMap<String, String> = HashMap()
        headerMap["platform"] = "android"
        headerMap["os-source"] = "android"
        headerMap["version-source"] = SSystem.getVersionName(SApplication.context()) ?: ""
        headerMap["version"] = SSystem.getVersionName(SApplication.context())!!
        headerMap["versionCode"] = SSystem.getVersionCode(SApplication.context())!!.toString()
        return headerMap
    }

    fun apiNetMap(): MutableMap<String, String> {
        val headerMap: MutableMap<String, String> = HashMap()
        headerMap["Content-Type"] = "application/x-www-form-urlencoded; charset=UTF-8"
        headerMap["Connection"] = "keep-alive"
        headerMap["Accept"] = "*/*"
        headerMap.putAll(baseNetMap())
        return headerMap
    }

    fun httpNetMap(): MutableMap<String, String> {
        val headerMap: MutableMap<String, String> = HashMap()
        headerMap.putAll(baseNetMap())
        return headerMap
    }
}
