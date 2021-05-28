package com.kangraoo.basektlib.tools

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

object HNetwork {

    /**
     * 获取可用的网络信息
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     */
    fun checkNetwork(context: Context): Boolean {
        val networkInfo = getActiveNetworkInfo(context)
        return networkInfo != null && networkInfo.isAvailable // 当前网络可用
    }

    /**
     * 当前网络是否是wifi网络
     *
     * @return
     */
    fun isWifi(context: Context): Boolean {
        val activeNetInfo = getActiveNetworkInfo(context)
        return (activeNetInfo != null &&
                activeNetInfo.type == ConnectivityManager.TYPE_WIFI)
    }

    /**
     * 判断当前网络是否是移动数据连接网络
     */
    fun isMobileNet(context: Context): Boolean {
        val activeNetInfo = getActiveNetworkInfo(context)
        return (activeNetInfo != null &&
                activeNetInfo.type == ConnectivityManager.TYPE_MOBILE)
    }

    @SuppressLint("MissingPermission")
    fun getNetworkInfo(context: Context): String {
        var info = ""
        val connectivity =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivity != null) {
            val activeNetInfo = connectivity.activeNetworkInfo
            if (activeNetInfo != null) {
                info = if (activeNetInfo.type == ConnectivityManager.TYPE_WIFI) {
                    activeNetInfo.typeName
                } else {
                    val sb = StringBuilder()
                    val tm =
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
                    sb.append(activeNetInfo.typeName)
                    sb.append(" [")
                    if (tm != null) {
                        // Result may be unreliable on CDMA networks
                        sb.append(tm.networkOperatorName)
                        sb.append("#")
                    }
                    sb.append(activeNetInfo.subtypeName)
                    sb.append("]")
                    sb.toString()
                }
            }
        }
        return info
    }
}
