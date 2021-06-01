package com.kangaroo.studentedu.ui.adapter

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.data.model.DianPing
import com.kangraoo.basektlib.tools.UFont
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.qdedu.base_module_picselect.image.tools.picturePreview
import kotlinx.android.synthetic.main.item_dian_ping_detail.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 09:32
 * #课堂点评详情#
 */
class DianPingDetailAdapter :BaseQuickAdapter<DianPing, BaseViewHolder>(R.layout.item_dian_ping_detail),LoadMoreModule {
    
    override fun convert(holder: BaseViewHolder, item: DianPing) {
        holder.itemView.name.text = "学生："+item.name
        holder.itemView.content.text = "评价内容："+item.content
        UFont.setStar(holder.itemView.star,item.star,5)
        holder.itemView.title.text = "课程："+item.title
        holder.itemView.img.setImageResource(item.img)

        holder.itemView.img.setOnClickListener {
            var list = arrayListOf<LocalMedia>()

            var p = UStorage.getWritePath("kk.jpg",StorageType.TYPE_IMAGE)!!
            AttachmentStore.save(context.resources.assets.open("teacherjiangke.jpg"),p)
            list.add(LocalMedia().apply { path = p })

            PictureSelector.create(context as Activity).picturePreview(0,list)
        }
    }
    
}
