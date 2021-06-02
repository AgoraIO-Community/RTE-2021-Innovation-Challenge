package com.qdedu.baselibcommon.arouter.service

import android.app.Activity
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Creator:
 * GuFanFan.
 * Date:
 * Created on 02-07-2020.
 * Description:
 * -.
 */
interface IUpdateService : IProvider {

    suspend fun checkNewVersion(activity: Activity, showToast: Boolean)

}