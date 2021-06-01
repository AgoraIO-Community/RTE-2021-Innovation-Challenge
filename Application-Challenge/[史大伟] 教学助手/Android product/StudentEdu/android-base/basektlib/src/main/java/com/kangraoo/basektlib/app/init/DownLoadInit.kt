package com.kangraoo.basektlib.app.init

import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.log.ULog
import com.liulishuo.okdownload.core.Util
import java.lang.Exception

class DownLoadInit(init: IInit) : Init(init) {

    override fun init() {
        super.init()
    }

    override fun threadInit() {
        super.threadInit()
        if (SApplication.instance().sConfiger.debugStatic) {
            Util.setLogger(object : Util.Logger {
                override fun i(tag: String?, msg: String?) {
                    ULog.i(tag, msg)
                }

                override fun w(tag: String?, msg: String?) {
                    ULog.w(tag, msg)
                }

                override fun e(tag: String?, msg: String?, e: Exception?) {
                    ULog.e(e, tag, msg)
                }

                override fun d(tag: String?, msg: String?) {
                    ULog.d(tag, msg)
                }
            })
        }
    }

    override fun notMainProcessInit() {
        super.notMainProcessInit()
        if (SApplication.instance().sConfiger.debugStatic) {
            Util.setLogger(object : Util.Logger {
                override fun i(tag: String?, msg: String?) {
                    ULog.i(tag, msg)
                }

                override fun w(tag: String?, msg: String?) {
                    ULog.w(tag, msg)
                }

                override fun e(tag: String?, msg: String?, e: Exception?) {
                    ULog.e(e, tag, msg)
                }

                override fun d(tag: String?, msg: String?) {
                    ULog.d(tag, msg)
                }
            })
        }
    }

    override fun notMainProcessThreadInit() {
        super.notMainProcessThreadInit()
    }
}
