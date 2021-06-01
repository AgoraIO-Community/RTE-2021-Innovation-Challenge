package com.qdedu.baselibcommon.tools

import android.content.Context
import com.kangraoo.basektlib.tools.log.ULog
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.qdedu.baselibcommon.arouter.service.IDataCenterService
import com.umeng.analytics.MobclickAgent
import java.util.*

object UUmeng {
    fun sendEvent(
        context: Context?,
        eventId: String,
        eventValue: HashMap<String, String>
    ) {
        var copyEventValue = eventValue
        if (copyEventValue==null) {
            copyEventValue = HashMap()
        }
        val dataService = ServiceProvider.iDataCenterService
        if (dataService!=null) {
            copyEventValue["user_id"] = dataService.userId()
            copyEventValue["user_grade"] = dataService.userGrade()
            copyEventValue["user_gender"] = dataService.userGender()
        }


        ULog.im("上报埋点事件 ---> 事件id: %s, 事件value: %s", eventId, copyEventValue.toString())
        if (eventId!=null) {
            MobclickAgent.onEvent(context, eventId, copyEventValue)
        }

    }
    /**
     * 非activity埋点
     */
    fun pageStart(pageName: String?) {
        MobclickAgent.onPageStart(pageName)
    }
    /**
     * 非activity埋点
     */
    fun pageEnd(pageName: String?) {
        MobclickAgent.onPageEnd(pageName)
    }

    /**
     * 统计应用自身的账号
     */
    fun profileSignIn(userId: String?) {
        MobclickAgent.onProfileSignIn(userId)
    }

    /**
     * 账号登出
     */
    fun profileSignOff() {
        MobclickAgent.onProfileSignOff()
    }
}