package com.vmloft.develop.app.match.request.repository

import com.vmloft.develop.app.match.request.api.APIRequest
import com.vmloft.develop.library.common.request.BaseRepository
import com.vmloft.develop.library.common.request.RResult
import com.vmloft.develop.app.match.request.bean.User


/**
 * Create by lzan13 on 2020/08/03 09:08
 * 描述：首页相关请求
 */
class MainRepository : BaseRepository() {

    /**
     * 获取当前用户信息
     */
    suspend fun loadCurrUser(): RResult<User> {
        return safeRequest(call = { requestLoadCurrUser() })
    }

    private suspend fun requestLoadCurrUser(): RResult<User> =
        executeResponse(APIRequest.userInfoAPI.current())

}