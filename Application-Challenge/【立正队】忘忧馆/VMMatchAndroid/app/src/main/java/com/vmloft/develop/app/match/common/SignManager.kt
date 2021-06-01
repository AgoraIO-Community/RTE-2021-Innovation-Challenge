package com.vmloft.develop.app.match.common

import com.vmloft.develop.library.common.event.LDEventBus
import com.vmloft.develop.app.match.im.IMManager
import com.vmloft.develop.app.match.request.bean.User
import com.vmloft.develop.library.common.utils.JsonUtils


/**
 * Create by lzan13 on 2020/6/19 14:08
 * 描述：用户管理
 */
class SignManager {

    private var mToken: String = ""

    // 当前登录账户
    private var mCurrUser: User? = null
    private var mPrevUser: User? = null

    companion object {
        val instance: SignManager by lazy {
            SignManager()
        }
    }

    /**
     * 判断是否登录
     */
    fun isSingIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    /**
     * token 处理
     */
    fun setToken(token: String) {
        mToken = token
        SPManager.instance.putToken(mToken)
    }

    fun getToken(): String? {
        if (mToken.isNullOrEmpty()) {
            mToken = SPManager.instance.getToken()
        }
        return mToken
    }

    /**
     * 当前登录用户处理
     * 设置当前登录用户，用于登陆成功后保存用户信息
     */
    fun setCurrUser(user: User?) {
        mCurrUser = user
        mPrevUser = user
        val userJson: String = JsonUtils.toJson(user, User::class.java)
        SPManager.instance.putCurrUser(userJson)
        SPManager.instance.putPrevUser(userJson)
        user?.let {
            LDEventBus.post(Constants.userInfoEvent, it)
        }
    }

    fun getCurrUser(): User? {
        if (mCurrUser == null) {
            var userJson: String = SPManager.instance.getCurrUser()
            mCurrUser = JsonUtils.formJson(userJson, User::class.java)
        }
        return mCurrUser
    }

    /**
     * 获取上一次登录用户
     */
    fun getPrevUser(): User? {
        if (mPrevUser == null) {
            var userJson: String = SPManager.instance.getPrevUser()
            mPrevUser = JsonUtils.formJson(userJson, User::class.java)
        }
        return mPrevUser
    }

    /**
     * 退出登录后晴空下当前用户数据
     */
    fun signOut() {
        setToken("")
        setCurrUser(null)

        IMManager.instance.exit()
    }

}