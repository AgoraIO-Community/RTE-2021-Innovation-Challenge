package com.qdedu.baselibcommon.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.qdedu.baselibcommon.R
import com.qdedu.baselibcommon.data.model.entity.BModule
import kotlinx.android.synthetic.main.public_common_item_start_module.view.*

/**
 * Created by hyy on 2018/09/12.
 */
class ModuleAdapter : BaseQuickAdapter<BModule, BaseViewHolder>(R.layout.public_common_item_start_module){
    override fun convert(holder: BaseViewHolder, item: BModule) {
        holder.itemView.moduleBtn.text = item.name
        holder.itemView.moduleBtn.setOnClickListener{
            item.action()
        }
    }

}