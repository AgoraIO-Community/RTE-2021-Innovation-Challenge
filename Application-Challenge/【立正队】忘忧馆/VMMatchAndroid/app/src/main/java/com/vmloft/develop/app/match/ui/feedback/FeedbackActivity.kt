package com.vmloft.develop.app.match.ui.feedback

import android.text.Editable
import android.text.TextWatcher
import android.view.View

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.app.match.R
import com.vmloft.develop.app.match.request.bean.Attachment
import com.vmloft.develop.app.match.request.bean.Category
import com.vmloft.develop.app.match.router.AppRouter
import com.vmloft.develop.library.common.base.BVMActivity
import com.vmloft.develop.library.common.base.BViewModel
import com.vmloft.develop.library.common.common.CConstants
import com.vmloft.develop.library.common.image.IMGChoose
import com.vmloft.develop.library.common.image.IMGLoader
import com.vmloft.develop.library.common.request.RPaging
import com.vmloft.develop.library.common.router.CRouter
import com.vmloft.develop.library.common.utils.errorBar
import com.vmloft.develop.library.common.utils.showBar
import com.vmloft.develop.library.tools.utils.VMReg
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.VMSystem

import kotlinx.android.synthetic.main.activity_feedback.*

import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Create by lzan13 on 2020/6/17 17:10
 * 描述：问题反馈
 */
@Route(path = AppRouter.appFeedback)
class FeedbackActivity : BVMActivity<FeedbackViewModel>() {

    // 内容
    private lateinit var mContent: String

    // 联系方式
    private lateinit var mContact: String

    // 截图
    private var picture: Any? = null

    override fun initVM(): FeedbackViewModel = getViewModel()

    override fun layoutId(): Int = R.layout.activity_feedback

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_feedback)

        setTopEndBtnEnable(false)
        setTopEndBtnListener(VMStr.byRes(R.string.btn_submit)) { planSubmit() }

        // 选择图片
        feedbackPictureBtn.setOnClickListener {
            IMGChoose.singlePicture(this) {
                picture = it
                feedbackPictureBtn.visibility = View.GONE
                IMGLoader.loadCover(feedbackPictureIV, picture, true, 8)
            }
        }
        feedbackPictureIV.setOnClickListener {
            CRouter.goDisplaySingle(picture.toString())
        }
        feedbackPictureCloseIV.setOnClickListener {
            picture = null
            feedbackPictureBtn.visibility = View.VISIBLE
        }
        // 监听输入框变化
        feedbackContentET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                mContent = s.toString().trim()
                verifyInputBox()
            }
        })

    }

    override fun initData() {

    }

    override fun onModelRefresh(model: BViewModel.UIModel) {
        if (model.type == "uploadPicture") {
            submit(model.data as Attachment)
        } else if (model.type == "feedback") {
            showBar(R.string.feedback_submit_hint)
            VMSystem.runInUIThread({ finish() }, CConstants.timeSecond)
        }
    }

    /**
     * 校验输入框内容
     */
    private fun verifyInputBox() {
        // 检查输入框是否为空
        setTopEndBtnEnable(!VMStr.isEmpty(mContent))
    }

    /**
     * 准备
     */
    private fun planSubmit() {
        if (picture == null) {
            submit(null)
        } else {
            mViewModel.uploadPicture(picture!!)
        }
    }

    /**
     * 创建
     */
    private fun submit(attachment: Attachment?) {
        if (!VMReg.isCommonReg(mContent, "^[\\s\\S]{1,800}\$")) {
            return errorBar(R.string.input_not_null)
        }
        val title = mContent.substring(0, if (mContent.length > 10) 10 else mContent.length - 1)

        mViewModel.feedback(title, mContent, attachment?.id ?: "")
    }


}
