package com.qdedu.baselibcommon.app.init

import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.app.init.Init
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

object UmengInit {

    fun init() {
        UMConfigure.preInit(SApplication.context(),AppHuanJingFactory.appModel.umappid,if(SApplication.instance().sConfiger.debugStatic)"dev" else null)
    }

    fun threadInit() {
        UMConfigure.init(SApplication.context(),AppHuanJingFactory.appModel.umappid,if(SApplication.instance().sConfiger.debugStatic)"dev" else null,UMConfigure.DEVICE_TYPE_PHONE,"")
        UMConfigure.setLogEnabled(SApplication.instance().sConfiger.debugStatic)
        // 选用MANUAL页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true)
    }
}