package com.kangraoo.basektlib.tools.log

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/16
 * desc :
 */
interface ILog {

    fun d(vararg message: Any?)

    fun e(vararg message: Any?)

    fun e(throwable: Throwable, vararg message: Any?)

    fun w(vararg message: Any?)

    fun i(vararg message: Any?)

    fun v(vararg message: Any?)

    fun wtf(vararg message: Any?)

    fun dm(message: String, vararg args: Any?)

    fun em(message: String, vararg args: Any?)

    fun em(throwable: Throwable, message: String, vararg args: Any?)

    fun wm(message: String, vararg args: Any?)

    fun im(message: String, vararg args: Any?)

    fun vm(message: String, vararg args: Any?)

    fun wtfm(message: String, vararg args: Any?)

    /**
     * Formats the given json content and print it
     */
    fun json(json: String?)

    /**
     * Formats the given xml content and print it
     */
    fun xml(xml: String)

    fun o(any: Any?)

    //with tag
    fun dt(tag: String, vararg message: Any?)

    fun et(tag: String,vararg message: Any?)

    fun et(tag: String,throwable: Throwable, vararg message: Any?)

    fun wt(tag: String,vararg message: Any?)

    fun it(tag: String,vararg message: Any?)

    fun vt(tag: String,vararg message: Any?)

    fun wtft(tag: String,vararg message: Any?)

    fun dmt(tag: String,message: String, vararg args: Any?)

    fun emt(tag: String,message: String, vararg args: Any?)

    fun emt(tag: String,throwable: Throwable, message: String, vararg args: Any?)

    fun wmt(tag: String,message: String, vararg args: Any?)

    fun imt(tag: String,message: String, vararg args: Any?)

    fun vmt(tag: String,message: String, vararg args: Any?)

    fun wtfmt(tag: String,message: String, vararg args: Any?)

    fun jsont(tag: String,json: String?)

    fun xmlt(tag: String,xml: String)

    fun ot(tag: String,any: Any?)
}
