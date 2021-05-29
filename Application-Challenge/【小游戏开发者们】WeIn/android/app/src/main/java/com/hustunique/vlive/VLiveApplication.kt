package com.hustunique.vlive

import android.app.Application
import com.hustunique.vlive.util.UserInfoManager

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/20
 */
class VLiveApplication : Application() {

    companion object {
        lateinit var application: VLiveApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        UserInfoManager.refreshUid()
    }
}