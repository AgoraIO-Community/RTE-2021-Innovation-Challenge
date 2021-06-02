package com.kangaroo.studentedu.ui.presenter

import com.hyphenate.chat.EMChatRoom
import com.hyphenate.chat.EMClient
import com.kangaroo.studentedu.ui.view.ChatRoomView
import com.kangraoo.basektlib.ui.mvp.BasePresenter
import com.kangraoo.basektlib.ui.action.BASE_PAGE_NAME
import com.kangraoo.basektlib.ui.action.BasePageAction
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.tools.task.TaskManager
import kotlinx.coroutines.launch

/**
 * 自动生成：by WaTaNaBe on 2021-05-25 11:43
 * #聊天室列表#
 */
class ChatRoomPresenter : BasePresenter<ChatRoomView>(){
	var basePageAction:BasePageAction<EMChatRoom> = BasePageAction()
    init {
        addAction(BASE_PAGE_NAME,basePageAction)
        basePageAction.dataListener = object : BasePageAction.DataListener<EMChatRoom>{
            override fun request(
                basePageAction: BasePageAction<EMChatRoom>,
                currentPage: Int,
                pageSize : Int
            ) {
                TaskManager.taskExecutor.execute(Runnable {
                    val chatRoomList = EMClient.getInstance().chatroomManager().fetchPublicChatRoomsFromServer(0, 10).data
                    view.visitActivity().runOnUiThread {
                        basePageAction.dataSucces(chatRoomList, chatRoomList.size)
                    }
                })
            }
        }
    }
}
