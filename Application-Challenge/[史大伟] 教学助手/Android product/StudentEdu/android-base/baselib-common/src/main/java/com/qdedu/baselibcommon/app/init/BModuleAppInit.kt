package com.qdedu.baselibcommon.app.init

import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.app.init.Init
import com.qdedu.base_module_web.init.WebInit

/**
 * @author shidawei
 * 创建日期：2021/4/14
 * 描述：
 */
abstract class BModuleAppInit(init : IInit): Init(init) ,IAppInit {


    override fun init() {
        super.init()
        preMainAppHuanjinInit()
        WebInit.init()
        afterMainInit()
    }

    override fun threadInit() {
        super.threadInit()
        preThreadAppHuanjinInit()
        afterThreadInit()
    }

    override fun notMainProcessInit() {
        super.notMainProcessInit()
        preMainAppHuanjinInit()
        WebInit.init()
        afterMainInit()
    }

    override fun notMainProcessThreadInit() {
        super.notMainProcessThreadInit()
        preThreadAppHuanjinInit()
        afterThreadInit()
    }

}