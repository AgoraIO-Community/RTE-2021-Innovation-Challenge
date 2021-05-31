package com.dong.circlelive.base

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dong.circlelive.live.OnAdapterItemClickListener
import com.dong.circlelive.live.OnAdapterItemLongClickListener

/**
 * Create by dooze on 2021/5/17  2:46 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
abstract class BaseAdapter<T>(protected var items: List<T> = emptyList()) : RecyclerView.Adapter<BaseViewHolder<T>>() {

    protected open lateinit var layoutInflater: LayoutInflater

    var itemClickListener: OnAdapterItemClickListener? = null

    var itemLongClickListener: OnAdapterItemLongClickListener? = null

    open fun refresh(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T? {
        return items.getOrNull(position)
    }

    abstract override fun getItemViewType(position: Int): Int

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

abstract class BaseDiffAdapter<T>(items: List<T>, val callback: DiffCallback) : BaseAdapter<T>(items) {

    override fun refresh(items: List<T>) {
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = this@BaseDiffAdapter.items.size

            override fun getNewListSize(): Int = items.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return callback.areItemsTheSame(oldItemPosition, newItemPosition)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return callback.areContentsTheSame(oldItemPosition, newItemPosition)
            }
        }).dispatchUpdatesTo(this)
    }

}

interface DiffCallback {

    fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean

    fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
}