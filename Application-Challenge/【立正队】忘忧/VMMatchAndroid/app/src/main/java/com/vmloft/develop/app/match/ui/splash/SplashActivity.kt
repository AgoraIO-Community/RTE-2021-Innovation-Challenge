package com.vmloft.develop.app.match.ui.splash

import com.vmloft.develop.app.match.R
import com.vmloft.develop.app.match.common.SPManager
import com.vmloft.develop.app.match.common.SignManager
import com.vmloft.develop.app.match.router.AppRouter
import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.common.router.CRouter

/**
 * Create by lzan13 2021/5/17
 * 描述：闪屏页，做承接调整用
 */
class SplashActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_splash

    override fun initUI() {
        super.initUI()
        if (SPManager.instance.isGuideShow()) {
            CRouter.go(AppRouter.appGuide)
        } else if (!SignManager.instance.isSingIn()) {
            CRouter.go(AppRouter.appSignGuide)
        } else {
            CRouter.goMain()
        }
        finish()
    }

    override fun initData() {

    }
}