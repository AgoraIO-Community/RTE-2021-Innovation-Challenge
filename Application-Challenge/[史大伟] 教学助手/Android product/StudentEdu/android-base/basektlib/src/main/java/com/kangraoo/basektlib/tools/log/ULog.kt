package com.kangraoo.basektlib.tools.log

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/16
 * desc :
 */
object ULog : ILog {

//    private var iLog: ILog? = null
    private var iLogs: MutableList<ILog> = ArrayList()

//    fun setiLog(iLog: ILog?) {
//        this.iLog = iLog
//    }

    fun addIlog(iLog: ILog){
        if(!iLogs.contains(iLog)){
            iLogs.add(iLog)
        }
    }

    fun removeIlog(iLog: ILog?){
        iLogs.remove(iLog)
    }

    private val log: OrhanobutLog by lazy() {
        OrhanobutLog()
    }

    override fun d(vararg message: Any?) {
        log.d(*message)
        iLogs.forEach {iLog ->
            iLog.d(*message)
        }
    }

    override fun dt(tag: String, vararg message: Any?) {
        log.dt(tag, *message)
        iLogs.forEach {iLog ->
            iLog.dt(tag, *message)
        }
    }

    override fun e(vararg message: Any?) {
        log.e(*message)
        iLogs.forEach {iLog ->
            iLog.e(*message)
        }
    }

    override fun e(throwable: Throwable, vararg message: Any?) {
        log.e(throwable, *message)
        iLogs.forEach {iLog ->
            iLog.e(throwable, *message)
        }
    }

    override fun et(tag: String, vararg message: Any?) {
        log.et(tag, *message)
        iLogs.forEach {iLog ->
            iLog.et(tag, *message)
        }
    }

    override fun et(tag: String, throwable: Throwable, vararg message: Any?) {
        log.et(tag, throwable, *message)
        iLogs.forEach {iLog ->
            iLog.et(tag, throwable, *message)
        }
    }

    override fun w(vararg message: Any?) {
        log.w(*message)
        iLogs.forEach {iLog ->
            iLog.w(*message)
        }
    }

    override fun wt(tag: String, vararg message: Any?) {
        log.wt(tag, *message)
        iLogs.forEach {iLog ->
            iLog.wt(tag, *message)
        }
    }

    override fun i(vararg message: Any?) {
        log.i(*message)
        iLogs.forEach {iLog ->
            iLog.i(*message)
        }
    }

    override fun it(tag: String, vararg message: Any?) {
        log.it(tag, *message)
        iLogs.forEach {iLog ->
            iLog.it(tag, *message)
        }
    }

    override fun v(vararg message: Any?) {
        log.v(*message)
        iLogs.forEach {iLog ->
            iLog.v(*message)
        }
    }

    override fun vt(tag: String, vararg message: Any?) {
        log.vt(tag, *message)
        iLogs.forEach {iLog ->
            iLog.vt(tag, *message)
        }
    }

    override fun wtf(vararg message: Any?) {
        log.wtf(*message)
        iLogs.forEach {iLog ->
            iLog.wtf(*message)
        }
    }

    override fun wtft(tag: String, vararg message: Any?) {
        log.wtft(tag, *message)
        iLogs.forEach {iLog ->
            iLog.wtft(tag, *message)
        }
    }

    override fun dm(message: String, vararg args: Any?) {
        log.dm(message, *args)
        iLogs.forEach {iLog ->
            iLog.dm(message, *args)
        }
    }

    override fun dmt(tag: String, message: String, vararg args: Any?) {
        log.dmt(tag, message, *args)
        iLogs.forEach {iLog ->
            iLog.dmt(tag, message, *args)
        }
    }

    override fun em(message: String, vararg args: Any?) {
        log.em(message, *args)
        iLogs.forEach {iLog ->
            iLog.em(message, *args)
        }
    }

    override fun em(throwable: Throwable, message: String, vararg args: Any?) {
        log.em(throwable, message, *args)
        iLogs.forEach {iLog ->
            iLog.em(throwable, message, *args)
        }
    }

    override fun emt(tag: String, message: String, vararg args: Any?) {
        log.emt(tag, message, *args)
        iLogs.forEach {iLog ->
            iLog.emt(tag, message, *args)
        }
    }

    override fun emt(tag: String, throwable: Throwable, message: String, vararg args: Any?) {
        log.emt(tag, throwable, message, *args)
        iLogs.forEach {iLog ->
            iLog.emt(tag, throwable, message, *args)
        }
    }

    override fun wm(message: String, vararg args: Any?) {
        log.wm(message, *args)
        iLogs.forEach {iLog ->
            iLog.wm(message, *args)
        }
    }

    override fun wmt(tag: String, message: String, vararg args: Any?) {
        log.wmt(tag, message, *args)
        iLogs.forEach {iLog ->
            iLog.wmt(tag, message, *args)
        }
    }

    override fun im(message: String, vararg args: Any?) {
        log.im(message, *args)
        iLogs.forEach {iLog ->
            iLog.im(message, *args)
        }
    }

    override fun imt(tag: String, message: String, vararg args: Any?) {
        log.imt(tag, message, *args)
        iLogs.forEach {iLog ->
            iLog.imt(tag, message, *args)
        }
    }

    override fun vm(message: String, vararg args: Any?) {
        log.vm(message, *args)
        iLogs.forEach {iLog ->
            iLog.vm(message, *args)
        }
    }

    override fun vmt(tag: String, message: String, vararg args: Any?) {
        log.vmt(tag, message, *args)
        iLogs.forEach {iLog ->
            iLog.vmt(tag, message, *args)
        }
    }

    override fun wtfm(message: String, vararg args: Any?) {
        log.wtfm(message, *args)
        iLogs.forEach {iLog ->
            iLog.wtfm(message, *args)
        }
    }

    override fun wtfmt(tag: String, message: String, vararg args: Any?) {
        log.wtfmt(tag, message, *args)
        iLogs.forEach {iLog ->
            iLog.wtfmt(tag, message, *args)
        }
    }

    override fun json(json: String?) {
        log.json(json)
        iLogs.forEach {iLog ->
            iLog.json(json)
        }
    }

    override fun jsont(tag: String, json: String?) {
        log.jsont(tag, json)
        iLogs.forEach {iLog ->
            iLog.jsont(tag, json)
        }
    }

    override fun xml(xml: String) {
        log.xml(xml)
        iLogs.forEach {iLog ->
            iLog.xml(xml)
        }
    }

    override fun xmlt(tag: String, xml: String) {
        log.xmlt(tag, xml)
        iLogs.forEach {iLog ->
            iLog.xmlt(tag, xml)
        }
    }

    override fun o(any: Any?) {
        log.o(any)
        iLogs.forEach {iLog ->
            iLog.o(any)
        }
    }

    override fun ot(tag: String, any: Any?) {
        log.ot(tag, any)
        iLogs.forEach {iLog ->
            iLog.ot(tag, any)
        }
    }
}
