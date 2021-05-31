package com.qdedu.baselibcommon.arouter.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * description: 客服接口
 * author: liping
 * date: 2020/12/30 10:44
 */
interface IKefuService : IProvider {
    fun startKefuActivity(context: Context,formTitle:String)
}