package com.vmloft.develop.app.match.ui.main.mine.info

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.app.match.R
import com.vmloft.develop.app.match.common.SignManager
import com.vmloft.develop.app.match.databinding.ActivityPersonalInfoEditBinding
import com.vmloft.develop.app.match.request.bean.User
import com.vmloft.develop.app.match.router.AppRouter
import com.vmloft.develop.library.common.base.BVMActivity
import com.vmloft.develop.library.common.base.BViewModel
import com.vmloft.develop.library.common.utils.errorBar
import com.vmloft.develop.library.tools.utils.VMReg
import com.vmloft.develop.library.tools.utils.VMStr

import kotlinx.android.synthetic.main.activity_personal_info_edit.*

import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Create by lzan13 on 2020/08/03 22:56
 * 描述：编辑用户名
 */
@Route(path = AppRouter.appEditUsername)
class EditUsernameActivity : BVMActivity<InfoViewModel>() {

    private var username: String? = null

    override fun initVM(): InfoViewModel = getViewModel()

    override fun layoutId(): Int = R.layout.activity_personal_info_edit

    override fun initUI() {
        super.initUI()
        (mBinding as ActivityPersonalInfoEditBinding).viewModel = mViewModel

        setTopTitle(R.string.info_username)

        setTopEndBtnEnable(false)
        setTopEndBtnListener(VMStr.byRes(R.string.btn_save)) { save() }
        infoSingleET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                username = s.toString().trim()
                verifyInputBox()
            }
        })
        infoDescTV.text = VMStr.byRes(R.string.info_username_hint)
    }

    override fun initData() {

    }

    override fun onModelRefresh(model: BViewModel.UIModel) {
        SignManager.instance.setCurrUser(model.data as User)
        finish()
    }

    /**
     * 校验输入框内容
     */
    private fun verifyInputBox() { // 将用户名转为消息并修剪
        // 检查输入框是否为空
        setTopEndBtnEnable(!TextUtils.isEmpty(username))
    }

    /**
     * 保存用户名
     */
    private fun save() {
        if (!VMReg.isCommonReg(username, "^[a-zA-Z0-9_.@]{4,16}\$")) {
            return errorBar("账户只能包含 a-z A-Z 0-9 _ . @ 且长度在 4-16 之间")
        }
        mViewModel.updateUsername(username!!)
    }
}