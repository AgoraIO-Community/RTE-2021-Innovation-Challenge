package com.kangaroo.studentedu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.studentedu.R
import kotlinx.android.synthetic.main.item_dian_ping.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 09:17
 * #课堂点评#
 */
class DianPingAdapter :BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dian_ping),LoadMoreModule {
    
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.kecheng.text = item
    }
    
}
