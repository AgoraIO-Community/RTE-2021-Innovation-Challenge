package com.dong.circlelive.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Create by dooze on 2021/5/17  2:44 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
abstract class BaseViewHolder<T>(view: View, private val adapter: BaseAdapter<T>) : RecyclerView.ViewHolder(view) {


    fun addClickListener(view: View) {
        view.setOnClickListener {
            adapter.itemClickListener?.onAdapterItemClick(it, adapterPosition)
        }
    }

    fun addLongClickListener(view: View) {
        view.setOnLongClickListener {
            adapter.itemLongClickListener?.onAdapterItemLongClick(it, adapterPosition) ?: true
        }
    }

    abstract fun bind(item: T)
}