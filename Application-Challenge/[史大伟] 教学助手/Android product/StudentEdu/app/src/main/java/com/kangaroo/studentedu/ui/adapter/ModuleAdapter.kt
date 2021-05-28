package com.kangaroo.studentedu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMGroup
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.data.model.Module
import kotlinx.android.synthetic.main.item_layout.view.*

/**
 * @author shidawei
 * 创建日期：2021/5/26
 * 描述：
 */
class ModuleAdapter: BaseQuickAdapter<Module, BaseViewHolder>(R.layout.item_layout) {

    override fun convert(holder: BaseViewHolder, item: Module) {
        holder.itemView.text.text = item.name
        holder.itemView.img.setImageResource(item.bitmap)
        holder.itemView.layout.setOnClickListener(item.onClickListener)
    }

}
