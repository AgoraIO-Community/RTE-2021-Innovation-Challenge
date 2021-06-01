package com.qdedu.baselibcommon.app.init

import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.app.init.Init
import com.qdedu.base_module_web.init.WebInit
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

abstract class BaseAppInit(init : IInit): Init(init) ,IAppInit {

//    /**
//     * 初始化环境配置
//     */
//    abstract fun preMainAppHuanjinInit()
//    open fun preThreadAppHuanjinInit(){}
//    abstract fun afterMainInit()
//    open fun afterThreadInit(){}

    override fun preThreadAppHuanjinInit() {
        super.preThreadAppHuanjinInit()
    }

    override fun afterThreadInit() {
        super.afterThreadInit()
    }

    override fun init() {
        super.init()
        preMainAppHuanjinInit()
        UmengInit.init()
        ModuleInit.init()
        WebInit.init()
        afterMainInit()
    }

    override fun threadInit() {
        super.threadInit()
        preThreadAppHuanjinInit()
        UmengInit.threadInit()
        ModuleInit.threadInit()
        afterThreadInit()
    }

    override fun notMainProcessInit() {
        super.notMainProcessInit()
        preMainAppHuanjinInit()
        UmengInit.init()
        ModuleInit.notMainProcessInit()
        WebInit.init()
        afterMainInit()
    }

    override fun notMainProcessThreadInit() {
        super.notMainProcessThreadInit()
        preThreadAppHuanjinInit()
        UmengInit.threadInit()
        ModuleInit.notMainProcessThreadInit()
        afterThreadInit()
    }

}