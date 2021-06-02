package com.kangaroo.studentedu.ui.presenter

import com.hyphenate.chat.EMClient
import com.kangaroo.studentedu.ui.view.KaoQingListView
import com.kangraoo.basektlib.ui.mvp.BasePresenter
import com.kangraoo.basektlib.ui.action.BASE_PAGE_NAME
import com.kangraoo.basektlib.ui.action.BasePageAction
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.tools.task.TaskManager
import kotlinx.coroutines.launch

/**
 * 自动生成：by WaTaNaBe on 2021-05-26 15:42
 * #考勤列表#
 */
class KaoQingListPresenter : BasePresenter<KaoQingListView>(){
	var basePageAction:BasePageAction<String> = BasePageAction()
    init {
        addAction(BASE_PAGE_NAME,basePageAction)
        basePageAction.dataListener = object : BasePageAction.DataListener<String>{
            override fun request(
                basePageAction: BasePageAction<String>,
                currentPage: Int,
                pageSize : Int
            ) {
                TaskManager.taskExecutor.execute(Runnable {
                    val student = EMClient.getInstance().contactManager().allContactsFromServer //需异步处理
                    view.visitActivity().runOnUiThread {
                        basePageAction.dataSucces(student, student.size)
                    }
                })
            }
        }
    }
}
