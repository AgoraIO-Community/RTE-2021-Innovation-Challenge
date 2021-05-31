package com.qdedu.baselibcommon.arouter.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IReadingService  : IProvider {

    fun initData(bigEnding:Boolean,terminalTypeId:Int?)

    fun bigEnding():Boolean

    fun terminalTypeId():Int?

    fun openReadText(context: Context, id: Long, fromPager: Int)

    fun openReadHome(context: Context, tabPosition: Int)

    fun openReadDetail(context: Context, id: Long)

}