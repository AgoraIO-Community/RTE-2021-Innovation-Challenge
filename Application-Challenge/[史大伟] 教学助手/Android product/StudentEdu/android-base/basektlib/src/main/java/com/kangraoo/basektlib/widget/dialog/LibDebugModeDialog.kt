package com.kangraoo.basektlib.widget.dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.R2
import com.kangraoo.basektlib.app.SApplication.Companion.instance
import com.kangraoo.basektlib.data.model.SelectModel
import com.kangraoo.basektlib.tools.SSysStore
import com.kangraoo.basektlib.tools.UFont
import com.kangraoo.basektlib.widget.common.DialogPopupConfig
import com.kangraoo.basektlib.widget.dialog.adapter.AppDebugModelAdapter
import com.kangraoo.basektlib.widget.floatwindow.ConsoleManager

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/10/17
 * desc :
 * version: 1.0
 */
class LibDebugModeDialog(context: Context) :
    LibBaseBottomSheetDialog(context, DialogPopupConfig.build { width = ViewGroup.LayoutParams.MATCH_PARENT }) {
    @BindView(R2.id.debugSwitch)
    @JvmField
    var debugSwitch: Switch? = null

    @BindView(R2.id.fontDebug)
    @JvmField
    var fontDebug: TextView? = null

    @BindView(R2.id.recycle)
    @JvmField
    var recycle: RecyclerView? = null

    companion object {
        fun register(appModels: List<SelectModel<*>>?) {
            if (instance().sConfiger.debugStatic && appModels != null) {
                val selectModel: SelectModel<*>? = SSysStore.instance.sysDebugDataSelect
                if (selectModel != null) {
                    for (sm in appModels) {
                        if (sm.title == selectModel.title) {
                            sm.select = true
                            break
                        }
                    }
                }
                SSysStore.instance.putSysDebugDataList(appModels)
            }
        }
    }

    var appDebugModelAdapter: AppDebugModelAdapter? = null

    override fun onViewCreated(contentView: View?) {
        UFont.setTextViewFont(fontDebug!!, R.string.lib_icon_bug)
        recycle!!.layoutManager = LinearLayoutManager(context)
        val list: List<SelectModel<*>>? = SSysStore.instance.sysDebugDataList()
        if (list != null) {
            appDebugModelAdapter = AppDebugModelAdapter(context, list)
            recycle!!.adapter = appDebugModelAdapter
        }
    }

    override val windowLayoutId: Int
        protected get() = R.layout.lib_dialog_debug_model

    override fun onShow() {
        val check: Boolean = SSysStore.instance.sysDebugConsole
        debugSwitch!!.isChecked = check
        debugSwitch!!.setOnCheckedChangeListener { buttonView, isChecked ->
            SSysStore.instance.putSysDebugConsole(isChecked)
            if (isChecked) {
                ConsoleManager.instance.showFloatWindow()
            } else {
                ConsoleManager.instance.closeFloat()
            }
        }
    }
}
