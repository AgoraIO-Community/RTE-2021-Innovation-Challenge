package com.kangaroo.studentedu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMGroup
import com.kangaroo.studentedu.R
import kotlinx.android.synthetic.main.item_group.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-25 11:23
 * #群组列表#
 */
class GroupAdapter :BaseQuickAdapter<EMGroup, BaseViewHolder>(R.layout.item_group),LoadMoreModule {
    
    override fun convert(holder: BaseViewHolder, item: EMGroup) {
        holder.itemView.group.text = item.groupName
    }
    
}
