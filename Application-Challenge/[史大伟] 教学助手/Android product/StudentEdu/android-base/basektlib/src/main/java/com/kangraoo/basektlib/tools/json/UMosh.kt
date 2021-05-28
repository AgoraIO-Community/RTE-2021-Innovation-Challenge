package com.kangraoo.basektlib.tools.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.internal.Util
import java.io.InputStream
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import okio.Buffer
import okio.BufferedSource

class UMosh(val moshiBuild: Moshi) {
//    val moshiBuild = Moshi.Builder().build()
    // 使用Kotlin-Reflect包时，这里改一下:
    // val moshiBuild = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    /**
     * 普通序列化
     */
    fun <T> fromJson(json: String, type: Type): T? = getAdapter<T>(type).fromJson(json)
    fun <T> fromJson(buffer: BufferedSource, type: Type): T? = getAdapter<T>(type).fromJson(buffer)
    fun <T> fromJson(ins: InputStream, type: Type): T? = getAdapter<T>(type).fromJson(Buffer().readFrom(ins))
    fun <T> fromJson(reader: JsonReader, type: Type): T? = getAdapter<T>(type).fromJson(reader)

    /**
     * 获取type序列化
     */
    inline fun <reified T> fromJson(json: String): T? = getAdapter<T>(T::class.java).fromJson(json)
    inline fun <reified T> fromJson(buffer: BufferedSource): T? = getAdapter<T>(T::class.java).fromJson(buffer)
    inline fun <reified T> fromJson(ins: InputStream): T? = getAdapter<T>(T::class.java).fromJson(Buffer().readFrom(ins))
    inline fun <reified T> fromJson(reader: JsonReader): T? = getAdapter<T>(T::class.java).fromJson(reader)

//    自动获取type序列化,性能较差
    inline fun <reified T> fromSimpJson(json: String): T? = getAdapter<T>().fromJson(json)
    inline fun <reified T> fromSimpJson(buffer: BufferedSource): T? = getAdapter<T>().fromJson(buffer)
    inline fun <reified T> fromSimpJson(ins: InputStream): T? = getAdapter<T>().fromJson(Buffer().readFrom(ins))
    inline fun <reified T> fromSimpJson(reader: JsonReader): T? = getAdapter<T>().fromJson(reader)

    // 高效序列化为list
    inline fun <reified T> toList(json: String): MutableList<T> =
        fromJson(json, Types.newParameterizedType(MutableList::class.java, T::class.java))
            ?: mutableListOf()

    inline fun <reified T> toList(buffer: BufferedSource): MutableList<T> =
        fromJson(buffer, Types.newParameterizedType(MutableList::class.java, T::class.java))
            ?: mutableListOf()

    inline fun <reified T> toList(ins: InputStream): MutableList<T> =
        fromJson(ins, Types.newParameterizedType(MutableList::class.java, T::class.java))
            ?: mutableListOf()

    inline fun <reified T> toList(reader: JsonReader): MutableList<T> =
        fromJson(reader, Types.newParameterizedType(MutableList::class.java, T::class.java))
            ?: mutableListOf()

    // 高效序列化为map
    inline fun <reified K, reified V> toMap(json: String): MutableMap<K, V> = fromJson(
        json,
        Types.newParameterizedType(MutableMap::class.java, K::class.java, V::class.java)
    ) ?: mutableMapOf()

    inline fun <reified K, reified V> toMap(buffer: BufferedSource): MutableMap<K, V> =
        fromJson(
            buffer,
            Types.newParameterizedType(MutableMap::class.java, K::class.java, V::class.java)
        ) ?: mutableMapOf()

    inline fun <reified K, reified V> toMap(ins: InputStream): MutableMap<K, V> = fromJson(
        ins,
        Types.newParameterizedType(MutableMap::class.java, K::class.java, V::class.java)
    ) ?: mutableMapOf()

    inline fun <reified K, reified V> toMap(reader: JsonReader): MutableMap<K, V> = fromJson(
        reader,
        Types.newParameterizedType(MutableMap::class.java, K::class.java, V::class.java)
    ) ?: mutableMapOf()

    // 高效序列化为map
    inline fun <reified T> toSet(json: String): MutableSet<T> = fromJson(
        json,
        Types.newParameterizedType(MutableSet::class.java, T::class.java)
    ) ?: mutableSetOf()

    inline fun <reified T> toSet(buffer: BufferedSource): MutableSet<T> =
        fromJson(
            buffer,
            Types.newParameterizedType(MutableSet::class.java, T::class.java)
        ) ?: mutableSetOf()

    inline fun <reified T> toSet(ins: InputStream): MutableSet<T> = fromJson(
        ins,
        Types.newParameterizedType(MutableSet::class.java, T::class.java)
    ) ?: mutableSetOf()

    inline fun <reified T> toSet(reader: JsonReader): MutableSet<T> = fromJson(
        reader,
        Types.newParameterizedType(MutableSet::class.java, T::class.java)
    ) ?: mutableSetOf()

    inline fun <reified T> toJson(t: T): String {
        return getAdapter<T>(T::class.java).toJson(t) ?: ""
    }
    // 反序列化，消耗性能
    inline fun <reified T> toSimpJson(t: T) = getAdapter<T>().toJson(t) ?: ""

    inline fun <reified K, reified V> mapToJson(any: Map<K, V>): String {
        val type = Types.newParameterizedType(Map::class.java, K::class.java, V::class.java)
        return getAdapter<Map<K, V>>(type).toJson(any) ?: ""
    }

    inline fun <reified T> listToJson(any: List<T>): String {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        return getAdapter<List<T>>(type).toJson(any) ?: ""
    }

    inline fun <reified T> setToJson(any: Set<T>): String {
        val type = Types.newParameterizedType(Set::class.java, T::class.java)
        return getAdapter<Set<T>>(type).toJson(any) ?: ""
    }

    /**
     * 得到jsonAdapter<Model>
     */
    fun <T> getAdapter(type: Type): JsonAdapter<T> = moshiBuild.adapter(type)

    /**
     * Reified实化类型参数
     */
    inline fun <reified T> getAdapter(): JsonAdapter<T> = moshiBuild.adapter(object :TypeToken<T>(){}.type)
}

abstract class TypeToken<T> {
    val type: Type get() = run {
        val superclass = javaClass.genericSuperclass
        Util.canonicalize((superclass as ParameterizedType).actualTypeArguments[0])
    }
}
