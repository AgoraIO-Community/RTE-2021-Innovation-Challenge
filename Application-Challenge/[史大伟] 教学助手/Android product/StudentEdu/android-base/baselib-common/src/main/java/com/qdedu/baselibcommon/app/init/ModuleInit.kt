package com.qdedu.baselibcommon.app.init

import com.qdedu.baselibcommon.arouter.ServiceProvider

object ModuleInit{

    fun init() {
        ServiceProvider.readerInit?.init()
        ServiceProvider.readingInit?.init()
        ServiceProvider.kefuInit?.init()
        ServiceProvider.shareInit?.init()
        ServiceProvider.buglyInit?.init()
        ServiceProvider.videoInit?.init()
        ServiceProvider.aliapmInit?.init()
        ServiceProvider.testInit?.init()
        ServiceProvider.youdaoInit?.init()
    }

    fun threadInit() {
        ServiceProvider.readerInit?.threadInit()
        ServiceProvider.readingInit?.threadInit()
        ServiceProvider.kefuInit?.threadInit()
        ServiceProvider.shareInit?.threadInit()
        ServiceProvider.buglyInit?.threadInit()
        ServiceProvider.videoInit?.threadInit()
        ServiceProvider.aliapmInit?.threadInit()
        ServiceProvider.testInit?.threadInit()
        ServiceProvider.youdaoInit?.threadInit()

    }

    fun notMainProcessInit() {
        ServiceProvider.readerInit?.notMainProcessInit()
        ServiceProvider.readingInit?.notMainProcessInit()
        ServiceProvider.kefuInit?.notMainProcessInit()
        ServiceProvider.shareInit?.notMainProcessInit()
        ServiceProvider.buglyInit?.notMainProcessInit()
        ServiceProvider.videoInit?.notMainProcessInit()
        ServiceProvider.aliapmInit?.notMainProcessInit()
        ServiceProvider.testInit?.notMainProcessInit()
        ServiceProvider.youdaoInit?.notMainProcessInit()

    }

    fun notMainProcessThreadInit() {
        ServiceProvider.readerInit?.notMainProcessThreadInit()
        ServiceProvider.readingInit?.notMainProcessThreadInit()
        ServiceProvider.kefuInit?.notMainProcessThreadInit()
        ServiceProvider.shareInit?.notMainProcessThreadInit()
        ServiceProvider.buglyInit?.notMainProcessThreadInit()
        ServiceProvider.videoInit?.notMainProcessThreadInit()
        ServiceProvider.aliapmInit?.notMainProcessThreadInit()
        ServiceProvider.testInit?.notMainProcessThreadInit()
        ServiceProvider.youdaoInit?.notMainProcessThreadInit()

    }
}