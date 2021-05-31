package com.kangraoo.basektlib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import butterknife.BindView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.R2
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.data.model.SelectAnyModel
import com.kangraoo.basektlib.data.model.SelectModel
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.tools.UUi.dp2px
import com.kangraoo.basektlib.tools.view.statebuilder.ShapeBuilder
import com.kangraoo.basektlib.widget.common.DialogPopupConfig
import com.kangraoo.basektlib.widget.listener.OnOnceClickListener

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/05/17
 * desc :
 * version: 1.0
 */
class LibListSelectDialog @JvmOverloads constructor(
    context: Context,
    var dataList: List<SelectAnyModel>? = null,
    iDialogPopup: DialogPopupConfig? = DialogPopupConfig.build {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        animationStyle = R.style.libBottomPopwindowAnimStyle
    }
) : LibBaseDialog(context, iDialogPopup!!) {

    var onLibListDialogListener: OnLibListDialogListener? = null

    interface OnLibListDialogListener {
        fun onSureListener(dialog: Dialog?, v: View?, selectModel: SelectAnyModel?)
    }

    @JvmField
    @BindView(R2.id.tvTitle)
     var tvTitle: TextView? = null
    @JvmField
    @BindView(R2.id.llList)
     var llList: LinearLayout? = null
    @JvmField
    @BindView(R2.id.svView)
     var svView: ScrollView? = null
    @JvmField
    @BindView(R2.id.tvCancle)
     var tvCancle: TextView? = null
    @JvmField
    @BindView(R2.id.ll)
     var ll: LinearLayout? = null
    override fun onViewCreated(contentView: View) {
        if (tempTitle != null) {
            tvTitle!!.text = tempTitle
        }
        if (tempCancle != null) {
            tvCancle!!.text = tempCancle
        }
        tvCancle!!.setOnClickListener(object : OnOnceClickListener() {
            override fun onOnceClick(v: View) {
                dismiss()
            }
        })
        data()
    }
    private var tempCancle: String? = null

    override val windowLayoutId: Int
        protected get() = R.layout.lib_dialog_list_select

    override fun onShow() {}
    override fun onDismiss(dialog: DialogInterface) {}

    private var tempTitle: String? = null

    fun setTitle(title: String?) {
        if (this.tvTitle != null) {
            this.tvTitle!!.text = title
        }
        tempTitle = title.toString()
    }

    fun setCancle(cancle: String?) {
        if (this.tvCancle != null) {
            this.tvCancle!!.text = cancle
        }
        tempCancle = cancle.toString()
    }

    var dataHeight = 0f
    var viewHeight = 46f
    private fun dataToView(data: List<SelectAnyModel>?) {
        this.dataList = data
        data()
    }

    fun data() {
        if (dataList != null) {
            dataHeight = 0f
            val lineParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                context.resources.getDimensionPixelSize(R.dimen.lib_line)
            )
            val textParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, viewHeight)
            )
            var i = 0
            for (t in dataList!!) {
                val textView = TextView(context)
                textView.layoutParams = textParams
                textView.text = t.title
                if (t.select) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.color_FF5722))
                } else {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.color_111111))
                }
                textView.gravity = Gravity.CENTER_VERTICAL
                textView.textSize = 16f
                textView.setPadding(dp2px(context, 18f), 0, dp2px(context, 18f), 0)
                textView.tag = i
                textView.setSingleLine(true)
                textView.ellipsize = TextUtils.TruncateAt.END
                textView.setOnClickListener(object : OnOnceClickListener() {
                    override fun onOnceClick(v: View) {
                        if (onLibListDialogListener != null) {
                            onLibListDialogListener!!.onSureListener(this@LibListSelectDialog, v, t)
                        }
                    }
                })
                textView.background = ShapeBuilder().setSolidColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_white
                    )
                ).setRipple(true, ContextCompat.getColor(context, R.color.color_CCCCCC))!!.build()
                val line = TextView(context)
                line.layoutParams = lineParams
                line.setBackgroundColor(ContextCompat.getColor(context, R.color.color_EEEEEE))

//                View line = LayoutInflater.from(getContext()).inflate(R.layout.lib_divider, (ViewGroup)null);
                llList!!.addView(textView)
                llList!!.addView(line)
                i++
                dataHeight += viewHeight + context.resources.getDimensionPixelSize(R.dimen.lib_line)
            }
        }
    }

    fun dataHeight():Int{
        return UUi.px2dp(SApplication.context(),UUi.getHeight(SApplication.context()).toFloat())/2
    }

    fun show(data: List<SelectAnyModel>?) {
        super.show()
        llList!!.removeAllViews()
        dataToView(data)
        val svParms = svView!!.layoutParams as LinearLayout.LayoutParams
        svParms.height = dp2px(context, dataHeight)
        if (dataHeight > dataHeight()) {
            svParms.height = dp2px(context, dataHeight().toFloat())
        }
        svView!!.layoutParams = svParms
    }


    override fun show() {
//        ll.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.lib_push_bottom_in));
        super.show()
        val svParms = svView!!.layoutParams as LinearLayout.LayoutParams
        svParms.height = dp2px(context, dataHeight)
        if (dataHeight > dataHeight()) {
            svParms.height = dp2px(context, dataHeight().toFloat())
        }
        svView!!.layoutParams = svParms
    }
}
