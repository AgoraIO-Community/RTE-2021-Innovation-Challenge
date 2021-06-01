package com.qdedu.baselibcommon.bridge

import android.text.TextUtils
import android.webkit.JavascriptInterface
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.SSystem
import com.kangraoo.basektlib.tools.URouter
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.ui.BActivity
import com.qdedu.baselibcommon.ui.activity.WebPageActivity
import com.qdedu.baselibcommon.arouter.BaseNavigator
import com.qdedu.baselibcommon.arouter.BaseRouterHub
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.qdedu.baselibcommon.arouter.service.IDataCenterService
import com.qdedu.baselibcommon.arouter.service.LINK
import com.qdedu.baselibcommon.bridge.entity.*
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import com.qdedu.baselibcommon.tools.UUmeng
import org.json.JSONException
import org.json.JSONObject

open class BaseJsApi {
    protected fun logFunctionName(functionName: String = "", data: String = "") {
        ULog.i("JS调用 -> $functionName","JS携带参数 -> $data")
    }

    /**
     * 打开新的页面.
     */
    @JavascriptInterface
    fun initWebPage(data: Any) {
        val dataString = data.toString()
        logFunctionName(functionName = "initWebPage", data = dataString)
        val entity = HJson.fromJson<JSDataEntity>(data.toString())
        val toolbar = entity?.toolbar
        if (toolbar!=null) {
            val topActivity = ActivityLifeManager.getCurrentActivity()
            if(topActivity!=null){
                if(topActivity is WebPageActivity){
                    val titleEntity = toolbar.title
                    if(titleEntity!=null){
                        if (!TextUtils.isEmpty(titleEntity.content)) {
                            topActivity.title = (titleEntity.content)
                        }
                        val visible = toolbar.visible
                        if (visible!=null&&topActivity is BActivity) {
                            topActivity.runOnUiThread{
                                topActivity.titleVisible(visible=="true")
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取UserInfo.
     */
    @JavascriptInterface
    fun getUserInfo(data: Any): String {
        val dataString = data.toString()
        logFunctionName(functionName = "getUserInfo", data = dataString)
        return try {
            val dataCenterService = ServiceProvider.iDataCenterService
            val jsonObject = JSONObject()
            jsonObject.put("userId", dataCenterService?.userId())
            jsonObject.put("token", dataCenterService?.appToken())
            val userObject = JSONObject(dataCenterService?.userInfo())
            jsonObject.put("user", userObject)
            jsonObject.toString()
        } catch (e: JSONException) {
            ""
        }
    }

    /**
     * 友盟埋点.
     */
    @JavascriptInterface
    fun umEvent(data: Any) {
        val dataString = data.toString()
        logFunctionName(functionName = "友盟埋点", data = dataString)
        val umEvent = HJson.fromJson<UMEventEntity>(dataString)
        if (TextUtils.isEmpty(umEvent?.eventId)) {
            return
        }

        val eventValue = HashMap<String, String>()
        if (!TextUtils.isEmpty(umEvent?.courseId)) {
            eventValue["course_id"] = umEvent!!.courseId!!
        }
        if (!TextUtils.isEmpty(umEvent?.courseName)) {
            eventValue["course_name"] = umEvent!!.courseName!!
        }
        if (!TextUtils.isEmpty(umEvent?.courseTag)) {
            eventValue["course_tag"] = umEvent!!.courseTag!!
        }
        if (umEvent?.buyTag!=null) {
            eventValue["buy_tag"] = umEvent.buyTag!!.toString()
        }
        if (!TextUtils.isEmpty(umEvent?.fromTag)) {
            eventValue["from_tag"] = umEvent!!.fromTag!!
        }
        if (!TextUtils.isEmpty(umEvent?.time)) {
            eventValue["time"] = umEvent!!.time!!
        }
        if (umEvent?.payType!=null) {
            eventValue["pay_tag"] = when (umEvent.payType) {
                1 -> "微信支付"
                2 -> "支付宝支付"
                else -> "未知"
            }
        }

        UUmeng.sendEvent(ActivityLifeManager.getCurrentActivity()!!,umEvent!!.eventId!!, eventValue)
    }

    /**
     * 分享.
     */
    @JavascriptInterface
    fun showShareDialog(data: Any) {
        val dataString = data.toString()
        logFunctionName(functionName = "showShareDialog", data = dataString)
        val shareEntity = HJson.fromJson<JSShareEntity>(dataString)
        val shareService = ServiceProvider.iShareService!!
        when (shareEntity!!.type) {
            LINK -> {
                shareService.shareUrl(
                    ActivityLifeManager.getCurrentActivity()!!,
                    shareEntity.url,
                    shareEntity.title,
                    shareEntity.des
                )
            }
        }
    }

    /**
     * 1: 去朗读.
     * 4: 大家在读.
     */
    @JavascriptInterface
    fun openRecord(data: Any) {
        val dataString = data.toString()
        logFunctionName(functionName = "openRecord", data = dataString)
        val readingEntity = HJson.fromJson<JSReadingEntity>(dataString)
        when (readingEntity?.from) {
            1 -> {
                ServiceProvider.readingService?.openReadText(ActivityLifeManager.getCurrentActivity()!!,readingEntity.id!!.toLong(),readingEntity!!.from_pager!!)
            }
            4 -> {
                ServiceProvider.readingService?.openReadHome(ActivityLifeManager.getCurrentActivity()!!, 1)
            }
        }
    }

    /**
     * 打开新的页面.
     */
    @JavascriptInterface
    fun openNewPage(data: Any) {
        val dataString = data.toString()
        logFunctionName(functionName = "openNewPage", data = dataString)
        val newPage = HJson.fromJson<JSNewPageEntity>(dataString)
        if(newPage!!.data!=null){
            BaseNavigator.navigationWebPage(ActivityLifeManager.getCurrentActivity()!!,newPage!!.data!!, titleVisible = !newPage!!.hidetoolbar)
        }

        if (newPage.closenumpage > 0) {
            var taskId = ActivityLifeManager.getCurrentActivity()!!.taskId
            for (i in 0 until newPage.closenumpage) {
                ActivityLifeManager.popActivityFinish(taskId)
            }
        }
    }

    /**
     * 关闭当前页面.
     */
    @JavascriptInterface
    fun closeCurrPage(data: Any) {
        val dataString = data.toString()
        logFunctionName(functionName = "closeCurrentPage", data = dataString)
        ActivityLifeManager.getCurrentActivity()!!.finish()
    }

    /**
     * 关闭一定数量页面.
     */
    @JavascriptInterface
    fun closeNumOfPage(data: Any) {
        val dataString = data.toString()
        logFunctionName(functionName = "closeNumOfPage", data = dataString)
        val closeNum = HJson.fromJson<JSDataEntity>(dataString)
        if (closeNum!!.num!! > 0) {
            var taskId = ActivityLifeManager.getCurrentActivity()!!.taskId
            for (i in 0 until closeNum.num!!) {
                ActivityLifeManager.popActivityFinish(taskId)
            }
        }
    }

    /**
     * 打开系统浏览器.
     */
    @JavascriptInterface
    fun openBrowser(data: Any) {
        val dataString = data.toString()
        logFunctionName(functionName = "openBrowser", data = dataString)
        val openUrlEntity = HJson.fromJson<JSDataEntity>(dataString)
        if (!TextUtils.isEmpty(openUrlEntity!!.url)) {
            SSystem.openSystemWebView(ActivityLifeManager.getCurrentActivity()!!, openUrlEntity.url)
        }
    }


}