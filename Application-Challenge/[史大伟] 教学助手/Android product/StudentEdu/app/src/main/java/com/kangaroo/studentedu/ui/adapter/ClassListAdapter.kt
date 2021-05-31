package com.kangaroo.studentedu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.studentedu.R
import kotlinx.android.synthetic.main.item_class_list.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 11:16
 * #班级详情#
 */
class ClassListAdapter :BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_class_list),LoadMoreModule {
    
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.student.text = item
    }
    
}
