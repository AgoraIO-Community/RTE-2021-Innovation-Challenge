package com.kangaroo.studentedu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.studentedu.R
import kotlinx.android.synthetic.main.item_bu_zhi_home_work.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 13:20
 * #布置作业#
 */
class BuZhiHomeWorkAdapter :BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_bu_zhi_home_work),LoadMoreModule {
    
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.zuoye.text = item
    }
    
}
