package com.kangaroo.studentedu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.studentedu.R
import kotlinx.android.synthetic.main.item_class_list.view.*

/**
 * @author shidawei
 * 创建日期：2021/5/28
 * 描述：
 */
class SpinnerAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_class_list),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.student.text = item
    }

}
