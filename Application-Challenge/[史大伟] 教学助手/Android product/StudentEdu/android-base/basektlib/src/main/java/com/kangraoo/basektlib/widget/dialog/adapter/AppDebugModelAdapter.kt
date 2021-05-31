package com.kangraoo.basektlib.widget.dialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.app.AppManager
import com.kangraoo.basektlib.data.model.SelectModel
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.SSysStore
import com.kangraoo.basektlib.tools.UClear
import com.kangraoo.basektlib.tools.store.MMKVStore
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast

class AppDebugModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.findViewById<TextView>(R.id.name)
    fun load(context: Context, model: SelectModel<*>) {
        name.text = model.title
        if (model.select) {
            name.setTextColor(ContextCompat.getColor(context, R.color.color_FF5722))
        } else {
            name.setTextColor(ContextCompat.getColor(context, R.color.color_111111))
        }
    }
}

class AppDebugModelAdapter(val context: Context, val selectModels: List<SelectModel<*>>) : RecyclerView.Adapter<AppDebugModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppDebugModelViewHolder {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.lib_item_debug, parent, false)
        return AppDebugModelViewHolder(contentView)
    }

    override fun getItemCount(): Int = selectModels.size

    override fun onBindViewHolder(holder: AppDebugModelViewHolder, position: Int) {
        holder.load(context, selectModels[position])
        holder.name.setOnClickListener {
            val selectModel = selectModels[position]
            if (!selectModel.select) {
                for (s in selectModels) {
                    s.select = false
                }
                selectModel.select = true
                TipToast.tip(Tip.Success, R.string.libDebugConfigFinish)

                UClear.cleanMemoryCache()
                UClear.cleanDiskCache()
                UClear.cleanInternalCache(context)
                UClear.cleanExternalCache(context)
                UClear.cleanDatabases(context)
                UClear.cleanSharedPreference(context)

                for (e in MMKVStore.getAllMMKVStore().values) {
                    e.clear()
                }
                SSysStore.instance.putSysDebugDataSelect(selectModel)
                HAction.mainHandler.postDelayed(Runnable {
//                    context.packageManager.getLaunchIntentForPackage(context.packageName)?.let {
//
//                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        it.putExtra("REBOOT", "reboot")
//                        context.startActivity(it)
//                    }
//                    exitProcess(1)

                    AppManager.restartApp(ActivityLifeManager.getCurrentActivity()!!)
                }, 500)
            }
        }
    }
}
