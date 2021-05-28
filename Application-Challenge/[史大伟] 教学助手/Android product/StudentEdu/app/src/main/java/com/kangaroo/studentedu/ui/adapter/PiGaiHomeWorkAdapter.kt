package com.kangaroo.studentedu.ui.adapter

import android.app.Activity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.data.model.HomeWork
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.qdedu.base_module_picselect.image.tools.picturePreview
import kotlinx.android.synthetic.main.item_dian_ping_detail.view.*
import kotlinx.android.synthetic.main.item_pi_gai_home_work.view.*
import kotlinx.android.synthetic.main.item_pi_gai_home_work.view.content
import kotlinx.android.synthetic.main.item_pi_gai_home_work.view.img

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 13:20
 * #批改作业#
 */
class PiGaiHomeWorkAdapter :BaseQuickAdapter<HomeWork, BaseViewHolder>(R.layout.item_pi_gai_home_work),LoadMoreModule {
    
    override fun convert(holder: BaseViewHolder, item: HomeWork) {
        holder.itemView.titlekecheng.text = item.title
        holder.itemView.content.text = "作业内容："+item.teacherContent
        holder.itemView.xiecontent.text = "学生作答："+item.content
        holder.itemView.student.text = "作答学生："+item.student
        holder.itemView.teacherimg.setImageResource(item.teacherImg)
        holder.itemView.img.setImageResource(item.img)
        holder.itemView.img.setOnClickListener {
            var list = arrayListOf<LocalMedia>()

            var p = UStorage.getWritePath("kk.jpg", StorageType.TYPE_IMAGE)!!
            AttachmentStore.save(context.resources.assets.open("teacherjiangke.jpg"),p)
            list.add(LocalMedia().apply { path = p })

            PictureSelector.create(context as Activity).picturePreview(0,list)
        }
        holder.itemView.teacherimg.setOnClickListener {
            var list = arrayListOf<LocalMedia>()

            var p = UStorage.getWritePath("kk.jpg",StorageType.TYPE_IMAGE)!!
            AttachmentStore.save(context.resources.assets.open("teacherjiangke.jpg"),p)
            list.add(LocalMedia().apply { path = p })

            PictureSelector.create(context as Activity).picturePreview(0,list)
        }
    }
    
}
