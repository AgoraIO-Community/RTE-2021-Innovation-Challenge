package com.hustunique.vlive.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.SparseArray
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hustunique.vlive.remote.BaseRsp
import com.hustunique.vlive.remote.NetRsp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.nio.ByteBuffer
import java.nio.channels.Channels

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 4/27/21
 */

private const val TAG = "Common"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vlive")

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}

fun Activity.readUncompressedAsset(@Suppress("SameParameterValue") assetName: String): ByteBuffer =
    assets.openFd(assetName).use { fd ->
        val input = fd.createInputStream()
        val dst = ByteBuffer.allocate(fd.length.toInt())

        val src = Channels.newChannel(input)
        src.read(dst)
        src.close()

        return dst.apply { rewind() }
    }


fun Activity.readCompressedAsset(assetName: String): ByteBuffer =
    assets.open(assetName).use { input ->
        val bytes = ByteArray(input.available())
        input.read(bytes)
        ByteBuffer.wrap(bytes)
    }

fun <T> SparseArray<T>.putIfAbsent(key: Int, value: T, onPut: (T) -> Unit = {}): T {
    if (get(key) == null) {
        put(key, value)
        onPut(value)
    }
    return value
}

public suspend fun <T> netReq(
    block: suspend CoroutineScope.() -> BaseRsp<T>
): NetRsp<T> = withContext(Dispatchers.IO) {
    try {
        val ret = block()
        NetRsp(ret.data, ret.success, ret.msg)
    } catch (e: HttpException) {
        Log.e(TAG, "[Net error handler]", e)
        NetRsp(successful = false, msg = e.message())
    } catch (e: ConnectException) {
        Log.e(TAG, "[Net error handler]", e)
        NetRsp(successful = false, msg = e.message)
    } catch (e: Exception) {
        Log.e(TAG, "[Net error handler]", e)
        NetRsp(successful = false, msg = e.message)
    }
}

