package com.dong.circlelive.utils

import android.content.Context
import okio.ByteString.Companion.encodeUtf8
import okio.buffer
import okio.source
import java.io.File
import java.io.InputStream

val File.md5
    get() = source().buffer().use { it.readByteString().md5().hex() }

val String.md5
    get() = encodeUtf8().md5().hex()

val InputStream.md5
    get() = source().buffer().readByteString().md5().hex()

fun getAssetsFileMd5(context: Context, assetsPath: String): String {
    return context.assets.open(assetsPath).use { it.md5 }
}