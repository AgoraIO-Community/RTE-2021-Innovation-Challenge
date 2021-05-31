package com.kangraoo.basektlib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import butterknife.ButterKnife
import butterknife.Unbinder
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.widget.common.DialogPopupConfig

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/01
 * desc :
 * version: 1.0
 */
abstract class LibBaseDialog @JvmOverloads constructor(context: Context, private val iDialogPopup: DialogPopupConfig = DialogPopupConfig.build { }) :
    Dialog(context, R.style.libBaseDialog), DialogInterface.OnDismissListener {
    var unbinder: Unbinder? = null

    protected abstract val windowLayoutId: Int
    protected abstract fun onViewCreated(contentView: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = layoutInflater.inflate(windowLayoutId, null)
        setContentView(contentView)
        unbinder = ButterKnife.bind(this, contentView)
        setCanceledOnTouchOutside(iDialogPopup.enableOutsideTouch)
        val lp = window!!.attributes
        lp.width = iDialogPopup.windowWidth
        lp.height = iDialogPopup.windowHeight
        lp.alpha = windowAlpha()
        window!!.attributes = lp
        if (!iDialogPopup.isDarkModel) {
            window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        if (iDialogPopup.animationStyle != 0) {
            window!!.setWindowAnimations(iDialogPopup.animationStyle)
        }
        setCancelable(iDialogPopup.cancelable)
        onViewCreated(contentView)
        setOnDismissListener(this)
    }

    protected open fun windowAlpha(): Float {
        return 1.0f
    }

    override fun show() {
        super.show()
        window!!.setGravity(gravity)
        onShow()
        if (onLibDialogListener != null) {
            onLibDialogListener!!.onShow()
        }
    }

    var gravity = Gravity.CENTER

    override fun onDismiss(dialog: DialogInterface) {
        if (onLibDialogListener != null) {
            onLibDialogListener!!.onDismiss(dialog)
        }
    }

    fun lazyDismissForTime(time: Long) {
        HAction.mainHandler.postDelayed({
            if (!isDestory && isShowing) {
                dismiss()
            }
        }, time)
    }

    protected abstract fun onShow()
    private var isDestory = false
    open fun onDestory() {
        isDestory = true
        if (isShowing) {
            dismiss()
        }
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder!!.unbind()
            unbinder = null
        }
        if (onLibDialogListener != null) {
            onLibDialogListener = null
        }
        setOnDismissListener(null)
    }

    var onLibDialogListener: OnLibDialogListener? = null
}
