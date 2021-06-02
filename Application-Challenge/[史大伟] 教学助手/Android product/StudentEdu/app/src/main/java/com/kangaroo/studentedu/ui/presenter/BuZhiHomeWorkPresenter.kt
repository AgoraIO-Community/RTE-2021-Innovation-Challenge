package com.kangaroo.studentedu.ui.presenter

import com.kangaroo.studentedu.ui.view.BuZhiHomeWorkView
import com.kangraoo.basektlib.ui.mvp.BasePresenter
import com.kangraoo.basektlib.ui.action.BASE_PAGE_NAME
import com.kangraoo.basektlib.ui.action.BasePageAction
import com.kangraoo.basektlib.data.DataResult
import kotlinx.coroutines.launch

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 13:20
 * #布置作业#
 */
class BuZhiHomeWorkPresenter : BasePresenter<BuZhiHomeWorkView>(){
	var basePageAction:BasePageAction<String> = BasePageAction()
    init {
        addAction(BASE_PAGE_NAME,basePageAction)
        basePageAction.dataListener = object : BasePageAction.DataListener<String>{
            override fun request(
                basePageAction: BasePageAction<String>,
                currentPage: Int,
                pageSize : Int
            ) {
                var list = arrayListOf<String>()
                list.add("高二3班作业")
                basePageAction.dataSucces(list,list.size)
            }
        }
    }
}
