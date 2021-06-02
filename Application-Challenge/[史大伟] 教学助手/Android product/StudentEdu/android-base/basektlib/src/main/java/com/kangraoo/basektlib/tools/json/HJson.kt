package com.kangraoo.basektlib.tools.json

import java.lang.reflect.Type
import java.util.*

object HJson {

    lateinit var uMosh: UMosh
    fun init(uMosh: UMosh) {
        this.uMosh = uMosh
    }

    inline fun <reified T> toJson(t: T): String {
        return uMosh.toJson(t)
    }

    inline fun <reified T> fromJson(json: String): T? = uMosh.fromJson(json)

    fun <T> fromJson(json: String, type: Type): T? = uMosh.fromJson(json, type)

    /**
     * 快速json 性能差
     */
    inline fun <reified T> fromSimpJson(json: String) = uMosh.fromSimpJson<T>(json)

    inline fun <reified T> toList(json: String) = uMosh.toList<T>(json)

    inline fun <reified K, reified V> toMap(json: String) = uMosh.toMap<K, V>(json)

    inline fun <reified T> toSet(json: String) = uMosh.toSet<T>(json)

    /**
     * 快速出json 性能差
     */
    inline fun <reified T> toSimpJson(t: T) = uMosh.toSimpJson(t)

    inline fun <reified K, reified V> mapToJson(any: Map<K, V>) = uMosh.mapToJson(any)

    inline fun <reified T> listToJson(any: List<T>) = uMosh.listToJson(any)

    inline fun <reified T> setToJson(any: Set<T>) = uMosh.setToJson(any)
}
