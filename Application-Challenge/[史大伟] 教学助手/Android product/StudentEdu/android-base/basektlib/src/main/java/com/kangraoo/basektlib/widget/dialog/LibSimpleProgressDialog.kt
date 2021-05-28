package com.kangraoo.basektlib.widget.dialog

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import butterknife.BindView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.R2
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.SSystem.isMainThread
import com.kangraoo.basektlib.widget.animview.LibSimpleLoadView
import com.kangraoo.basektlib.widget.common.DialogPopupConfig

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/01
 * desc :
 * version: 1.0
 */
class LibSimpleProgressDialog(context: Context) :
    LibBaseDialog(context, DialogPopupConfig.build { width = ViewGroup.LayoutParams.WRAP_CONTENT; dark = false; enableOutsideTouch = false }), IProgressDialog {
    @JvmField
    @BindView(R2.id.loadView)
    var loadView: LibSimpleLoadView? = null

    @JvmField
    @BindView(R2.id.progressMessage)
    var progressMessage: TextView? = null
    override fun onViewCreated(contentView: View) {
        showMessage()
    }

    private fun showMessage() {
        if (TextUtils.isEmpty(message)) {
            progressMessage!!.visibility = View.GONE
        } else {
            progressMessage!!.visibility = View.VISIBLE
            progressMessage!!.text = message
        }
    }

    var message: String? = null

    fun message(message: String?) {
        this.message = message
        if (progressMessage != null) {
            if (isMainThread()) {
                showMessage()
            } else {
                HAction.mainHandler.post(Runnable {
                    showMessage()
                })
            }
        }
    }

    fun message(@StringRes messageResId: Int) {
        message(context.getString(messageResId))
    }

    override fun onShow() {}
    override val windowLayoutId: Int
        get() = R.layout.lib_dialog_simple_progress

    override fun onDismiss(dialog: DialogInterface) {
        loadView!!.stopAnimators()
    }

    override fun showProgress() {
        message("")
        if (!isShowing) {
            show()
        }
    }

    override fun showProgress(@StringRes message: Int) {
        message(message)
        if (!isShowing) {
            show()
        }
    }

    override fun showProgress(message: String) {
        message(message)
        if (!isShowing) {
            show()
        }
    }

    override fun dismissProgress() {
        if (isShowing) {
            dismiss()
        }
    }

    override fun destoryProgress() {
        onDestory()
    }

    override fun show() {
        if (isMainThread()) {
            super.show()
        } else {
            HAction.mainHandler.post(Runnable { show() })
        }
    }
}
