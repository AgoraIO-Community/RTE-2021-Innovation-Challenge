package com.qdedu.baselibcommon.app.init

import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.tools.HNotification
import com.kangraoo.basektlib.widget.dialog.LibDebugModeDialog
import com.qdedu.baselibcommon.R
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.qdedu.baselibcommon.bridge.BaseJsApi
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class SampleAppInit(init : IInit): BModuleAppInit(init) {
    override fun preMainAppHuanjinInit() {
        AppHuanJingFactory.dev.apply {

        }
        AppHuanJingFactory.online.apply {

        }
        AppHuanJingFactory.test.apply {

        }
        AppHuanJingFactory.uat.apply {

        }
        LibDebugModeDialog.register(AppHuanJingFactory.huanJingSelectList)
    }

    override fun afterMainInit() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.lib_color_skyblue, R.color.color_white) //全局设置主题颜色
            ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
        HNotification.setNotificationChannel()
    }
}