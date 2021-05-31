package com.kangaroo.studentedu.app

import android.app.Activity
import android.media.AudioFormat
import android.os.Environment
import com.kangaroo.studentedu.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.tools.HNotification
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.net.OkHttpDns
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import com.kangraoo.basektlib.widget.dialog.LibDebugModeDialog
import com.qdedu.baselibcommon.app.init.BaseAppInit
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.qdedu.baselibcommon.bridge.BaseJsApi
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import com.qdedu.baselibcommon.data.ShareEntity
import com.qdedu.baselibcommon.tools.UUmeng
import com.qdedu.baselibcommon.ui.activity.WebPageActivity
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zlw.main.recorderlib.RecordManager
import com.zlw.main.recorderlib.recorder.RecordConfig

class AppInit(init: IInit) : BaseAppInit(init) {

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
        UUmeng.profileSignIn("demouser")
        ServiceProvider.buglyService?.putUserData("demouser")
        OkHttpDns.instance.preResolveHosts(arrayListOf("v.juhe.cn"))
        WebPageActivity.addJsObject("", BaseJsApi())
        // 设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.transparent, R.color.color_999999) // 全局设置主题颜色
            ClassicsHeader(context) // .setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        // 设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> // 指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
        HNotification.setNotificationChannel()

    }

    override fun afterThreadInit() {
        super.afterThreadInit()
        /**
         * 参数1： Application 实例
         * 参数2： 是否打印日志
         */
        RecordManager.getInstance().apply {
            init(SApplication.instance(), SApplication.instance().sConfiger.debugStatic)
            changeFormat(RecordConfig.RecordFormat.MP3)
            changeRecordConfig(recordConfig.setSampleRate(16000))//比率
            changeRecordConfig(
                recordConfig.setEncodingConfig(
                AudioFormat.ENCODING_PCM_8BIT))//编码
            changeRecordDir(UStorage.getDirectoryByDirType(StorageType.TYPE_AUDIO))
        }
    }
}
