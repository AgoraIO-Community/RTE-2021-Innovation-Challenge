package com.hustunique.vlive.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/20
 */
object JsonUtil {

    val moshi by lazy {
        Moshi.Builder().build()
    }

    fun jsonMap(editor: (MutableMap<String, String>) -> Unit): String {
        val adapter = moshi.adapter<Map<String, String>>(
            Types.newParameterizedType(
                Map::class.java,
                String::class.java,
                String::class.java
            )
        )
        val map = HashMap<String, String>()
        editor(map)
        return adapter.toJson(map)
    }

    fun jsonReqBody(editor: (MutableMap<String, String>) -> Unit): RequestBody =
        jsonMap(editor).toRequestBody()

}