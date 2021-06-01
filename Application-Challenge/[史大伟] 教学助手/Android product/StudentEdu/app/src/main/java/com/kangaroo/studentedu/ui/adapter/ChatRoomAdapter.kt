package com.kangaroo.studentedu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMChatRoom
import com.kangaroo.studentedu.R
import kotlinx.android.synthetic.main.item_chat_room.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-25 11:43
 * #聊天室列表#
 */
class ChatRoomAdapter :BaseQuickAdapter<EMChatRoom, BaseViewHolder>(R.layout.item_chat_room),LoadMoreModule {
    
    override fun convert(holder: BaseViewHolder, item: EMChatRoom) {
        holder.itemView.group.text = item.name
    }
    
}
