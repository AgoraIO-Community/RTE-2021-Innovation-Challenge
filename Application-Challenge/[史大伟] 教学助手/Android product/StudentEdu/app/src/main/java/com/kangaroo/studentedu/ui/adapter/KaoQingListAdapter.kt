package com.kangaroo.studentedu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.studentedu.R
import kotlinx.android.synthetic.main.item_kao_qing_list.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-26 15:42
 * #考勤列表#
 */
class KaoQingListAdapter :BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_kao_qing_list),LoadMoreModule {

    var list = arrayListOf<Boolean>()

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.radio.text = item
        holder.itemView.radio.setOnCheckedChangeListener { buttonView, isChecked ->
            list[holder.layoutPosition] = isChecked
        }
        holder.itemView.radio.isChecked = list[holder.layoutPosition]
    }
    
}
