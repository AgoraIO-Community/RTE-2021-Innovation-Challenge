package com.qdedu.baselibcommon.app.init

/**
 * @author shidawei
 * 创建日期：2021/4/14
 * 描述：
 */
interface IAppInit {

    /**
     * 初始化环境配置
     */
    fun preMainAppHuanjinInit()
    fun preThreadAppHuanjinInit(){}
    fun afterMainInit()
    fun afterThreadInit(){}
}