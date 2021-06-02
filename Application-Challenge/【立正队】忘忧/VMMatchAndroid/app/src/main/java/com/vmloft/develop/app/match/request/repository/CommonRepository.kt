package com.vmloft.develop.app.match.request.repository

import com.vmloft.develop.app.match.common.SPManager
import com.vmloft.develop.app.match.request.api.APIRequest
import com.vmloft.develop.app.match.request.bean.*
import com.vmloft.develop.library.common.request.BaseRepository
import com.vmloft.develop.library.common.request.RResult
import com.vmloft.develop.app.match.request.db.AppDatabase
import com.vmloft.develop.library.common.common.CConstants
import com.vmloft.develop.library.common.request.RPaging
import okhttp3.MultipartBody


/**
 * Create by lzan13 on 2020/08/09 15:08
 * 描述：一些通用数据相关处理
 */
class CommonRepository : BaseRepository() {

    /**
     * 上传附件
     */
    suspend fun upload(part: MultipartBody.Part): RResult<Attachment> {
        return safeRequest(call = { requestUpload(part) })
    }

    private suspend fun requestUpload(part: MultipartBody.Part): RResult<Attachment> =
        executeResponse(APIRequest.commonAPI.upload(part))

    /**
     * 获取分类集合
     */
    suspend fun getCategoryList(server: Boolean = false): RResult<RPaging<Category>> {
        // 根据参数优先从本地获取，这里检查下缓存时间，大于 1 天就重新从服务器获取
        val time = SPManager.instance.getCategoryTime()
        val intervalTime = System.currentTimeMillis() - time
        if (!server && intervalTime < CConstants.timeDay) {
            val list = AppDatabase.getInstance().categoryDao().all()
            if (list.isNotEmpty()) {
                return RResult.Success("", RPaging(list.size, list.size, CConstants.defaultPage, CConstants.defaultLimitBig, list))
            }
        }
        // 从服务器获取后保存到本地数据库
        val result = safeRequest(call = { requestCategoryList() })
        if (result is RResult.Success) {
            SPManager.instance.setCategoryTime(System.currentTimeMillis())
            // 先清空原来的数据
            AppDatabase.getInstance().categoryDao().delete()
            // 重新插入
            AppDatabase.getInstance().categoryDao().insert(*result.data!!.data.toTypedArray())
        }
        return result
    }

    private suspend fun requestCategoryList(): RResult<RPaging<Category>> =
        executeResponse(APIRequest.commonAPI.getCategoryList())

    /**
     * 获取职业集合
     */
    suspend fun getProfessionList(server: Boolean = false): RResult<RPaging<Profession>> {
        // 根据参数优先从本地获取，这里检查下缓存时间，大于 1 天就重新从服务器获取
        // 根据参数优先从本地获取
        val time = SPManager.instance.getProfessionTime()
        val intervalTime = System.currentTimeMillis() - time
        if (!server && intervalTime < CConstants.timeDay) {
            val list = AppDatabase.getInstance().professionDao().all()
            if (list.isNotEmpty()) {
                return RResult.Success("", RPaging(list.size, list.size, CConstants.defaultPage, CConstants.defaultLimitBig, list))
            }
        }
        // 从服务器获取后保存到本地数据库
        val result = safeRequest(call = { requestProfessionList() })
        if (result is RResult.Success) {
            SPManager.instance.setProfessionTime(System.currentTimeMillis())
            // 先清空原来的数据
            AppDatabase.getInstance().professionDao().delete()
            // 重新插入
            AppDatabase.getInstance().professionDao().insert(*result.data!!.data.toTypedArray())
        }
        return result
    }

    private suspend fun requestProfessionList(): RResult<RPaging<Profession>> =
        executeResponse(APIRequest.commonAPI.getProfessionList())


    /**
     * 提交反馈
     */
    suspend fun feedback(contact: String, content: String, attachment: String): RResult<Any> {
        return safeRequest(call = { requestFeedback(contact, content, attachment) })
    }

    private suspend fun requestFeedback(contact: String, content: String, attachment: String): RResult<Any> =
        executeResponse(APIRequest.commonAPI.feedback(contact, content, attachment))

}