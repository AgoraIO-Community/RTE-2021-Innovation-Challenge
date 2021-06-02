package com.kangraoo.basektlib.widget.floatwindow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.tools.view.statebuilder.ShapeBuilder

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/12/16
 * desc :
 * version: 1.0
 */
class ConsoleTestAdapter(val context: Context) : RecyclerView.Adapter<ConsoleTestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsoleTestViewHolder {
        return ConsoleTestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lib_item_console_test, parent, false))
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(holder: ConsoleTestViewHolder, position: Int) {
        holder.load(context, models[position])
        holder.debugBtn.setOnClickListener {
            if (onConsoleItemClickListener != null) onConsoleItemClickListener!!.consoleClick(models[position], it)
        }
    }

    fun refresh(listKeys: List<String>) {
        models = ArrayList(listKeys)
        notifyDataSetChanged()
    }

    fun clean() {
        models.clear()
        notifyDataSetChanged()
    }

    var onConsoleItemClickListener: OnConsoleItemClickListener? = null

    var models: ArrayList<String> = ArrayList<String>()
}
interface OnConsoleItemClickListener {
    fun consoleClick(string: String, view: View)
}

class ConsoleTestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var debugBtn: TextView = itemView.findViewById<TextView>(R.id.debugBtn)
    init {
        debugBtn.background =
            ShapeBuilder().setCornersRadius(UUi.dp2px(SApplication.context(), 5f).toFloat())
            .setRipple(true, ContextCompat.getColor(SApplication.context(), R.color.color_000000_alpha50))
            .setSolidColor(ContextCompat.getColor(SApplication.context(), R.color.color_EEEEEE_alpha50))
            .setStrokeColor(ContextCompat.getColor(SApplication.context(), R.color.color_white))
            .setStrokeWidth(SApplication.context().resources.getDimension(R.dimen.lib_line))
            .build()
    }
    fun load(context: Context, model: String) {
        debugBtn.text = model
    }
}
