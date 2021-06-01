package com.kangraoo.basektlib.tools.net

import android.text.TextUtils
import com.alibaba.sdk.android.httpdns.HttpDns
import com.alibaba.sdk.android.httpdns.HttpDnsService
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.log.ULog
import java.net.InetAddress
import okhttp3.Dns

class OkHttpDns private constructor() : Dns {

    companion object {
        val instance: OkHttpDns by lazy {
            OkHttpDns()
        }
    }

    var httpdns: HttpDnsService? = null
    init {
        if (SApplication.instance().sConfiger.dnsAliEnable) {
            httpdns = HttpDns.getService(SApplication.context())
            httpdns?.setLogEnabled(SApplication.instance().sConfiger.debugStatic)
            httpdns?.setLogger {
                ULog.d("resultILogger:$it")
            }
        }
    }

    override fun lookup(hostname: String): List<InetAddress> {
        if (httpdns != null) {
            // 通过异步解析接口获取ip
            val ip = httpdns!!.getIpByHostAsync(hostname)
            if (ip != null) {
                // 如果ip不为null，直接使用该ip进行网络请求
                val inetAddresses = InetAddress.getAllByName(ip).toList()
                ULog.d("阿里云dns解析 inetAddresses:$inetAddresses")
                return inetAddresses
            } else {
                ULog.e("阿里云dns解析失败,不存在该ip")
            }
        }
        ULog.d("默认系统dns解析")
        // 如果返回null，走系统DNS服务解析域名
        return Dns.SYSTEM.lookup(hostname)
    }


//    设置预解析域名
//
//    在您初始化程序时，可以选择性地预先向HTTPDNS SDK中注册您后续可能会使用到的域名，以便SDK提前解析，减少后续解析域名时请求的时延。您只需调用以下接口：

    fun preResolveHosts(hostList: ArrayList<String>){
        httpdns?.setPreResolveHosts(hostList)
    }

}
