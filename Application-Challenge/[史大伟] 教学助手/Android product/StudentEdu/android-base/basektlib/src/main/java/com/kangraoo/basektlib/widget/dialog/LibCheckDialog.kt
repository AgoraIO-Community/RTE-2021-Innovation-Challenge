package com.kangraoo.basektlib.widget.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import butterknife.BindView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.R2
import com.kangraoo.basektlib.widget.common.DialogPopupConfig

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/06
 * desc :
 * version: 1.0
 */
class LibCheckDialog @JvmOverloads constructor(context: Context, override val windowLayoutId: Int = R.layout.lib_dialog_check, iDialogPopup: DialogPopupConfig = DialogPopupConfig.build { }, val styleBuilder: StyleBuilder = StyleBuilder.build { }) : LibBaseDialog(context, iDialogPopup) {

    constructor(context: Context, iDialogPopup: DialogPopupConfig = DialogPopupConfig.build { }) : this(context, R.layout.lib_dialog_check, iDialogPopup)

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
//
//    @BindView(R2.id.ll)
//    lateinit var ll: LinearLayout

    interface OnLibCheckDialogListener : OnLibDialogListener {
        fun onSure()
        fun onCancle()
    }

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
        if (tempCancleVisable != null) {
            cancle!!.visibility = tempCancleVisable!!
        }
        if (tempSureVisable != null) {
            sure!!.visibility = tempSureVisable!!
        }
        cancle!!.setOnClickListener {
            if (onLibDialogListener != null && onLibDialogListener is OnLibCheckDialogListener) {
                (onLibDialogListener as OnLibCheckDialogListener).onCancle()
            }
            dismiss() }
        sure!!.setOnClickListener {
            if (onLibDialogListener != null && onLibDialogListener is OnLibCheckDialogListener) {
                (onLibDialogListener as OnLibCheckDialogListener).onSure()
            }
        }

        if (styleBuilder.cancleTextColorResource != null) cancle!!.setTextColor(ContextCompat.getColor(context, styleBuilder.cancleTextColorResource!!))
        if (styleBuilder.cancleBackgroundResource != null) cancle!!.setBackgroundResource(styleBuilder.cancleBackgroundResource!!)
        if (styleBuilder.sureTextColorResource != null) sure!!.setTextColor(ContextCompat.getColor(context, styleBuilder.sureTextColorResource!!))
        if (styleBuilder.sureBackgroundResource != null) sure!!.setBackgroundResource(styleBuilder.sureBackgroundResource!!)
    }

    private var tempTitle: String? = null
    private var tempContent: String? = null
    private var tempCancle: String? = null
    private var tempSure: String? = null

    private var tempCancleVisable: Int? = null
    private var tempSureVisable: Int? = null

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

    fun cancleVisable(visibility: Int) {
        if (this.cancle != null) {
            this.cancle!!.visibility = visibility
        }
        tempCancleVisable = visibility
    }

    fun sureVisable(visibility: Int) {
        if (this.sure != null) {
            this.sure!!.visibility = visibility
        }
        tempSureVisable = visibility
    }

    override fun onShow() {}

    class StyleBuilder {
        var cancleTextColorResource: Int? = null
        var cancleBackgroundResource: Int? = null
        var sureTextColorResource: Int? = null
        var sureBackgroundResource: Int? = null
        companion object {
            @JvmStatic inline fun build(block: LibCheckDialog.StyleBuilder.() -> Unit) = LibCheckDialog.StyleBuilder().apply(
                block
            ).build()
        }
        fun build() = this
    }
}
