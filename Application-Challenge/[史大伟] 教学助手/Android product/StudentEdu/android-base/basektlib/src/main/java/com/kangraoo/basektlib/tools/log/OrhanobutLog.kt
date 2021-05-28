package com.kangraoo.basektlib.tools.log

import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.json.HJson
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/16
 * desc :
 */
internal class OrhanobutLog : ILog {

    init {
        val sConfiger = SApplication.instance().sConfiger
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(sConfiger.logConfig.showThreadInfo) // (Optional) Whether to show thread info or not. Default true
            .methodCount(sConfiger.logConfig.methodCount) // 决定打印多少行（每一行代表一个方法）默认：2
            .methodOffset(sConfiger.logConfig.methodOffset) // 设置方法的偏移量
            .tag(sConfiger.logConfig.tag) // (Optional) Custom tag for each log. Default PRETTY_LOGGER
            .build()
        Logger.clearLogAdapters()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return sConfiger.debugStatic
            }
        })
    }

    override fun d(vararg message: Any?) {
        dt(Thread.currentThread().stackTrace[4].fileName, *message)
    }

    override fun dt(tag: String, vararg message: Any?) {
        Logger.t(tag).d(HString.concatObject(" ", *message))
    }

    override fun e(vararg message: Any?) {
        et(Thread.currentThread().stackTrace[4].fileName, *message)
    }

    override fun e(throwable: Throwable, vararg message: Any?) {
        et(Thread.currentThread().stackTrace[4].fileName, throwable, *message)
    }

    override fun et(tag: String, vararg message: Any?) {
        Logger.t(tag).e(HString.concatObject(" ", *message))
    }

    override fun et(tag: String, throwable: Throwable, vararg message: Any?) {
        Logger.t(tag).e(throwable, HString.concatObject(" ", *message))
    }

    override fun w(vararg message: Any?) {
        wt(Thread.currentThread().stackTrace[4].fileName, *message)
    }

    override fun wt(tag: String, vararg message: Any?) {
        Logger.t(tag).w(HString.concatObject(" ", *message))
    }

    override fun i(vararg message: Any?) {
        it(Thread.currentThread().stackTrace[4].fileName, *message)
    }

    override fun it(tag: String, vararg message: Any?) {
        Logger.t(tag).i(HString.concatObject(" ", *message))
    }

    override fun v(vararg message: Any?) {
        vt(Thread.currentThread().stackTrace[4].fileName, *message)
    }

    override fun vt(tag: String, vararg message: Any?) {
        Logger.t(tag).v(HString.concatObject(" ", *message))
    }

    override fun wtf(vararg message: Any?) {
        wtft(Thread.currentThread().stackTrace[4].fileName, *message)
    }

    override fun wtft(tag: String, vararg message: Any?) {
        Logger.t(tag).wtf(HString.concatObject(" ", *message))
    }

    override fun dm(message: String, vararg args: Any?) {
        dmt(Thread.currentThread().stackTrace[4].fileName, message, *args)
    }

    override fun dmt(tag: String, message: String, vararg args: Any?) {
        Logger.t(tag).d(message, *args)
    }

    override fun em(message: String, vararg args: Any?) {
        emt(Thread.currentThread().stackTrace[4].fileName, message, *args)
    }

    override fun em(throwable: Throwable, message: String, vararg args: Any?) {
        em(Thread.currentThread().stackTrace[4].fileName, throwable, message, *args)
    }

    override fun emt(tag: String, message: String, vararg args: Any?) {
        Logger.t(tag).e(message, *args)
    }

    override fun emt(tag: String, throwable: Throwable, message: String, vararg args: Any?) {
        Logger.t(tag).e(throwable, message, *args)
    }

    override fun wm(message: String, vararg args: Any?) {
        wmt(Thread.currentThread().stackTrace[4].fileName, message, *args)
    }

    override fun wmt(tag: String, message: String, vararg args: Any?) {
        Logger.t(tag).w(message, *args)
    }

    override fun im(message: String, vararg args: Any?) {
        imt(Thread.currentThread().stackTrace[4].fileName, message, *args)
    }

    override fun imt(tag: String, message: String, vararg args: Any?) {
        Logger.t(tag).i(message, *args)
    }

    override fun vm(message: String, vararg args: Any?) {
        vmt(Thread.currentThread().stackTrace[4].fileName, message, *args)
    }

    override fun vmt(tag: String, message: String, vararg args: Any?) {
        Logger.t(tag).v(message, *args)
    }

    override fun wtfm(message: String, vararg args: Any?) {
        wtfmt(Thread.currentThread().stackTrace[4].fileName, message, *args)
    }

    override fun wtfmt(tag: String, message: String, vararg args: Any?) {
        Logger.t(tag).wtf(message, *args)
    }

    override fun json(json: String?) {
        jsont(Thread.currentThread().stackTrace[4].fileName, json)
    }

    override fun jsont(tag: String, json: String?) {
        Logger.t(tag).json(json)
    }

    override fun xml(xml: String) {
        xmlt(Thread.currentThread().stackTrace[4].fileName, xml)
    }

    override fun xmlt(tag: String, xml: String) {
        Logger.t(tag).xml(xml)
    }

    override fun o(any: Any?) {
        ot(Thread.currentThread().stackTrace[4].fileName, any)
    }

    override fun ot(tag: String, any: Any?) {
        jsont(tag, if(any!=null)HJson.toSimpJson(any)else null)
    }
}
