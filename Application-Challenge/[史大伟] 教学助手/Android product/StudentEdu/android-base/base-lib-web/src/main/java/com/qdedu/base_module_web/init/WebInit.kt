package com.qdedu.base_module_web.init

import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.log.ULog
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsListener
import wendu.dsbridgex.DWebView
import java.util.*


object WebInit{

     fun init() {

        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.setTbsListener(
            object : TbsListener {
                override fun onDownloadFinish(i: Int) {
                    ULog.d("QbSdk", "onDownloadFinish -->下载X5内核完成：$i")
                }

                override fun onInstallFinish(i: Int) {
                    ULog.d("QbSdk", "onInstallFinish -->安装X5内核进度：$i")
                }

                override fun onDownloadProgress(i: Int) {
                    ULog.d("QbSdk", "onDownloadProgress -->下载X5内核进度：$i")
                }
            })
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                ULog.d("开启TBS===X5加速成功")
            }

            override fun onCoreInitFinished() {
                ULog.d("开启TBS===X5加速失败")
            }
        }
        val settings = HashMap<String, Any>()
        settings[TbsCoreSettings.TBS_SETTINGS_USE_PRIVATE_CLASSLOADER] = true
        settings[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        settings[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(settings)
        //x5内核初始化接口
        QbSdk.initX5Environment(SApplication.context(), cb)

        if(SApplication.instance().sConfiger.debugStatic){
            // set debug mode
            DWebView.setWebContentsDebuggingEnabled(true)
        }
    }





}