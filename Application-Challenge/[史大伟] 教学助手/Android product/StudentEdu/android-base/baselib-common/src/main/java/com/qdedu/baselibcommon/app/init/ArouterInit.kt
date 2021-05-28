package com.qdedu.baselibcommon.app.init

import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.utils.DefaultLogger
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.app.init.Init

class ArouterInit(init : IInit): Init(init)  {

    override fun init() {
        super.init()
        if (SApplication.instance().sConfiger.debugStatic) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.setLogger(
                DefaultLogger(
                    SApplication.instance().sConfiger.logConfig.tag + "_ARouter::"
                )
            )
            ARouter.openLog() // 打印日志
//            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(SApplication.instance()) // 尽可能早，推荐在Application中初始化

    }

    override fun threadInit() {
        super.threadInit()

    }

    override fun notMainProcessInit() {
        super.notMainProcessInit()
        if (SApplication.instance().sConfiger.debugStatic) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.setLogger(
                DefaultLogger(
                    SApplication.instance().sConfiger.logConfig.tag + "_ARouter::"
                )
            )
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(SApplication.instance()) // 尽可能早，推荐在Application中初始化
    }

    override fun notMainProcessThreadInit() {
        super.notMainProcessThreadInit()
    }



}