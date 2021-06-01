package com.kangraoo.basektlib.app.init

import com.kangraoo.basektlib.tools.crash.AndroidCrash

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/08
 * desc : 奔溃初始化
 */
@Deprecated("请移步到buglyinit")
class CrashInit(init: IInit) : Init(init) {

    override fun init() {
        super.init()
    }

    override fun threadInit() {
        super.threadInit()
//        AndroidCrash.instance
    }

    override fun notMainProcessInit() {
        super.notMainProcessInit()
    }

    override fun notMainProcessThreadInit() {
        super.notMainProcessThreadInit()
//        AndroidCrash.instance
    }
}
