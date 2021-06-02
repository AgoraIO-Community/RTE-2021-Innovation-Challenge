package com.kangraoo.basektlib.widget.dialog

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.R2

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/02
 * desc :
 * version: 1.0
 */
class LibEditDialog(context: Context) : LibBaseDialog(context) {
    @JvmField
    @BindView(R2.id.title)
    var title: TextView? = null

    @JvmField
    @BindView(R2.id.content)
    var content: TextView? = null

    @JvmField
    @BindView(R2.id.cancle)
    var cancle: TextView? = null

    @JvmField
    @BindView(R2.id.sure)
    var sure: TextView? = null

    @JvmField
    @BindView(R2.id.ll)
    var ll: LinearLayout? = null

    @JvmField
    @BindView(R2.id.input)
    var input: EditText? = null

    override fun onViewCreated(contentView: View) {
        if (tempTitle != null) {
            title!!.text = tempTitle
        }
        if (tempContent != null) {
            content!!.text = tempContent
        }
        if (tempCancle != null) {
            cancle!!.text = tempCancle
        }
        if (tempSure != null) {
            sure!!.text = tempSure
        }
        if (tempInput != null) {
            input!!.hint = tempInput
        }
        sure!!.setOnClickListener {
            if (onLibDialogListener != null && onLibDialogListener is OnLibEditDialogListener) {
                (onLibDialogListener as OnLibEditDialogListener).onSure(input!!)
            }
        }
        cancle!!.setOnClickListener { dismiss() }
        input!!.visibility = View.VISIBLE
    }

    override val windowLayoutId: Int
        get() = R.layout.lib_dialog_check

    fun title(title: CharSequence?) {
        if (this.title != null) {
            this.title!!.text = title
        }
        tempTitle = title.toString()
    }

    fun content(content: CharSequence?) {
        if (this.content != null) {
            this.content!!.text = content
        }
        tempContent = content.toString()
    }

    fun cancle(cancle: String?) {
        if (this.cancle != null) {
            this.cancle!!.text = cancle
        }
        tempCancle = cancle.toString()
    }

    fun sure(sure: String?) {
        if (this.sure != null) {
            this.sure!!.text = sure
        }
        tempSure = sure.toString()
    }

    private var tempTitle: String? = null
    private var tempContent: String? = null
    private var tempCancle: String? = null
    private var tempSure: String? = null
    private var tempInput: String? = null

    fun inputHite(hite: CharSequence?) {
        if (this.input != null) {
            this.input!!.hint = hite
        }
        tempInput = hite.toString()
    }

    override fun onShow() {}
    interface OnLibEditDialogListener : OnLibDialogListener {
        fun onSure(text: EditText)
    }
}
