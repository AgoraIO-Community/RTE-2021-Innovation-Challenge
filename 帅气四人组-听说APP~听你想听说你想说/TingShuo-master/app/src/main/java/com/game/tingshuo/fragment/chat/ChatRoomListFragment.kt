package com.game.tingshuo.fragment.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.game.tingshuo.R
import com.game.tingshuo.adapter.chat.ChatRoomListAdapter
import com.game.tingshuo.app.BaseFragment
import com.game.tingshuo.databinding.FragmentChatRoomListBinding
import kotlinx.android.synthetic.main.fragment_chat_room_list.*


/**
 * 聊天室房间列表页面
 */
class ChatRoomListFragment : BaseFragment<FragmentChatRoomListBinding>() {
    private val chatRoomAdapter: ChatRoomListAdapter by lazy {
        ChatRoomListAdapter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_chat_room_list
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        //header
        title?.text = "房间列表"
        back?.visibility = View.GONE

        chat_room_list?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = chatRoomAdapter

            //TODO: adapter绑定数据源
        }
    }

}