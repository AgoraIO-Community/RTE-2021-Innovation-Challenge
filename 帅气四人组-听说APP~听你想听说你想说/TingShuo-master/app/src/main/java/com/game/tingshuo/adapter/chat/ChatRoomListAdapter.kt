package com.game.tingshuo.adapter.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.game.tingshuo.R
import com.game.tingshuo.bean.chat.ChatRoomInfo
import com.game.tingshuo.util.GlideUtils

class ChatRoomListAdapter: RecyclerView.Adapter<ChatRoomViewHolder>(){
    private val datas = ArrayList<ChatRoomInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat_room,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.setContent(datas[position])
    }

}

class ChatRoomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val groupIv = itemView.findViewById<ImageView>(R.id.chat_room_img)
    private val groupTitle = itemView.findViewById<TextView>(R.id.chat_room_title)

    fun setContent(info: ChatRoomInfo) {
        GlideUtils.load(itemView.context, groupIv, info.groupImg, R.drawable.group_img_placeholder)
        groupTitle.text = info.groupTitle
    }
}