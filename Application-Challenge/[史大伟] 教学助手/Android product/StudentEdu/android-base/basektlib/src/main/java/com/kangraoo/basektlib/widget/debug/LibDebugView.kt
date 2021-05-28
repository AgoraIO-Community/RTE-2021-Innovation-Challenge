package com.kangraoo.basektlib.widget.debug

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.app.SApplication.Companion.instance
import com.kangraoo.basektlib.data.model.SelectModel
import com.kangraoo.basektlib.tools.SSysStore
import com.kangraoo.basektlib.tools.UFont.setTextViewFont
import com.kangraoo.basektlib.widget.dialog.LibDebugModeDialog
import com.kangraoo.basektlib.widget.listener.OnOnceClickListener

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/07
 * desc :
 * version: 1.0
 */
class LibDebugView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    lateinit var title: TextView
    lateinit var fontDebug: TextView
    lateinit var libDebugModeDialog: LibDebugModeDialog
    private fun reData() {
        if (SApplication.instance().sConfiger.debugStatic) {
            val selectModel: SelectModel<*>? = SSysStore.instance.sysDebugDataSelect
            if (selectModel != null) {
                title.text = selectModel.title
            } else {
                title.text = "DEBUG"
            }
        }
    }

    init {
        if (instance().sConfiger.debugStatic) {
            visibility = View.VISIBLE
            val view =
                LayoutInflater.from(context).inflate(R.layout.lib_system_debug, this, true)
            libDebugModeDialog = LibDebugModeDialog(getContext())
            view.setOnClickListener(object : OnOnceClickListener() {
                override fun onOnceClick(var1: View) {
                    libDebugModeDialog.show()
                }
            })
            title = view.findViewById(R.id.title)
            fontDebug = view.findViewById(R.id.fontDebug)
            setTextViewFont(fontDebug, R.string.lib_icon_bug)
            reData()
        } else {
            visibility = View.GONE
        }
    }
}
