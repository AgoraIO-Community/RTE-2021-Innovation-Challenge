package com.dong.circlelive

import android.os.Bundle
import cn.leancloud.AVUser
import com.dong.circlelive.base.BaseActivity
import com.dong.circlelive.login.LogicActivity
import com.dong.circlelive.main.MainActivity

/**
 * Create by dooze on 5/6/21  8:55 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LauncherActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (emClient.isLoggedInBefore && AVUser.currentUser() != null) {
            start(MainActivity::class.java)
        } else {
            start(LogicActivity::class.java)
        }
        finish()
    }
}