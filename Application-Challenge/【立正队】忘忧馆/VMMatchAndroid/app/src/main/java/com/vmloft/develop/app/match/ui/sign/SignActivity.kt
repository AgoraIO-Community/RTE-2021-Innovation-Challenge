package com.vmloft.develop.app.match.ui.sign


import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.app.match.R
import com.vmloft.develop.app.match.common.Constants
import com.vmloft.develop.app.match.databinding.ActivitySignGuideBinding
import com.vmloft.develop.app.match.router.AppRouter
import com.vmloft.develop.library.common.base.BVMActivity
import com.vmloft.develop.library.common.base.BViewModel
import com.vmloft.develop.library.common.router.CRouter
import com.vmloft.develop.library.common.utils.errorBar
import com.vmloft.develop.library.tools.utils.VMSystem
import kotlinx.android.synthetic.main.activity_sign_guide.*

import org.koin.androidx.viewmodel.ext.android.getViewModel


/**
 * Create by lzan13 on 2020/6/4 17:10
 * 描述：注册登录入口界面
 */
@Route(path = AppRouter.appSignGuide)
class SignActivity : BVMActivity<SignViewModel>() {

    lateinit var devicesId: String

    override fun initVM(): SignViewModel = getViewModel()

    override fun layoutId(): Int = R.layout.activity_sign_guide

    override fun initUI() {
        super.initUI()
        (mBinding as ActivitySignGuideBinding).viewModel = mViewModel

        setTopIcon(R.drawable.ic_close)
        setTopTitle(R.string.sign_welcome_to_join)

        setTopEndIcon(R.drawable.ic_info) { CRouter.go(AppRouter.appSettingsAbout) }

        signPrivacyPolicyTV.setOnClickListener { CRouter.goWeb(Constants.userAgreementUrl) }
        signUserAgreementTV.setOnClickListener { CRouter.goWeb(Constants.privatePolicyUrl) }
        signByDevicesIdBtn.setOnClickListener {
            if (signPrivacyPolicyCB.isChecked) {
                mViewModel.signIn(devicesId, "123456")
            } else {
                errorBar(R.string.sign_privacy_policy_hint)
            }
        }
        signByPasswordBtn.setOnClickListener { CRouter.go(AppRouter.appSignIn) }
    }

    override fun initData() {
        devicesId = VMSystem.deviceId()
    }

    override fun onModelRefresh(model: BViewModel.UIModel) {
        if (model.type == "signIn" || model.type == "signUpByDevicesId") {
            // 这里直接调用下 IM 的登录，不影响页面的继续
            mViewModel.signInIM()

            CRouter.goMain()
            finish()
        }
    }

    override fun onModelError(model: BViewModel.UIModel) {
        if (model.code == 404) {
            // 账户不存在，去注册一下
            mViewModel.signUpByDevicesId(devicesId, "123456")
        } else {
            super.onModelError(model)
        }
    }
}
