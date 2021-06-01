package com.qdedu.baselibcommon.arouter.service

import android.app.Activity
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author shidawei
 * 创建日期：2021/3/25
 * 描述：
 */
interface IHomeWorkService : IProvider {

    fun workListUi(activity: Activity)

}