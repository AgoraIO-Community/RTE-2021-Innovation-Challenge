package com.kangraoo.basektlib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.widget.common.DialogPopupConfig

class LibDialogBuilder {
    var iDialogPopup: DialogPopupConfig = DialogPopupConfig.build { }
    lateinit var context: Context
    var windowLayoutId: Int? = null
    var windowView: View? = null
    var windowAlpha: Float = 1.0f
    var onLibDialogListener: OnLibDialogListener? = null
    var gravity = Gravity.CENTER

    fun build(): Dialog = object : Dialog(context, R.style.libBaseDialog), DialogInterface.OnDismissListener {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val contentView = if (windowLayoutId != null) {
                LayoutInflater.from(context).inflate(windowLayoutId!!, null)
            } else {
                windowView
            }
            setContentView(contentView!!)
            setCanceledOnTouchOutside(iDialogPopup.enableOutsideTouch)
            val lp = window!!.attributes
            lp.width = iDialogPopup.windowWidth
            lp.height = iDialogPopup.windowHeight
            lp.alpha = windowAlpha
            window!!.attributes = lp
            if (!iDialogPopup.isDarkModel) {
                window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
            if (iDialogPopup.animationStyle != 0) {
                window!!.setWindowAnimations(iDialogPopup.animationStyle)
            }
            setCancelable(iDialogPopup.cancelable)
            setOnDismissListener(this)
        }

        override fun onDismiss(dialog: DialogInterface) {
            if (onLibDialogListener != null) {
                onLibDialogListener!!.onDismiss(dialog)
            }
        }
        override fun show() {
            super.show()
            window!!.setGravity(gravity)
            if (onLibDialogListener != null) {
                onLibDialogListener!!.onShow()
            }
        }

        fun onDestory() {
            if (isShowing) {
                dismiss()
            }
            if (onLibDialogListener != null) {
                onLibDialogListener = null
            }
        }
    }

    companion object {
        @JvmStatic inline fun build(block: LibDialogBuilder.() -> Unit) = LibDialogBuilder().apply(block).build()
    }
}
