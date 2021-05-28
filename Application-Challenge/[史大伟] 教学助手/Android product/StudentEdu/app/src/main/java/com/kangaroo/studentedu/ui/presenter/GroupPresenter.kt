package com.kangaroo.studentedu.ui.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMGroup
import com.kangaroo.studentedu.ui.view.GroupView
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.tools.task.TaskManager
import com.kangraoo.basektlib.ui.action.BASE_PAGE_NAME
import com.kangraoo.basektlib.ui.action.BasePageAction
import com.kangraoo.basektlib.ui.mvp.BasePresenter
import kotlinx.coroutines.launch


/**
 * 自动生成：by WaTaNaBe on 2021-05-25 11:23
 * #群组列表#
 */
class GroupPresenter : BasePresenter<GroupView>(){
	var basePageAction:BasePageAction<EMGroup> = BasePageAction()
    init {
        addAction(BASE_PAGE_NAME, basePageAction)
        basePageAction.dataListener = object : BasePageAction.DataListener<EMGroup>{
            override fun request(
                basePageAction: BasePageAction<EMGroup>,
                currentPage: Int,
                pageSize: Int
            ) {
                TaskManager.taskExecutor.execute(Runnable {
                    val grouplist =
                        EMClient.getInstance().groupManager().joinedGroupsFromServer //需异步处理
                    view.visitActivity().runOnUiThread {
                        basePageAction.dataSucces(grouplist, grouplist.size)
                    }
                })

            }
        }
    }
}
