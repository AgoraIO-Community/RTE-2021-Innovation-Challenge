package com.kangraoo.basektlib.app.init

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/08
 * desc : 初始化接口
 */
interface IInit {
    /**
     * 主线程初始化
     */
    fun init()

    /**
     * 多线程初始化
     */
    fun threadInit()
    /**
     * 非主进程初始化
     */
    fun notMainProcessInit()
    /**
     * 非主进程多线程初始化
     */
    fun notMainProcessThreadInit()
}
