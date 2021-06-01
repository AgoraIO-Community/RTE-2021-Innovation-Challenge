package com.kangaroo.studentedu.ui.presenter

import android.view.View
import com.kangaroo.studentedu.tools.UUser
import com.kangaroo.studentedu.ui.view.DianPingView
import com.kangraoo.basektlib.ui.mvp.BasePresenter
import com.kangraoo.basektlib.ui.action.BASE_PAGE_NAME
import com.kangraoo.basektlib.ui.action.BasePageAction
import com.kangraoo.basektlib.data.DataResult
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.coroutines.launch

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 09:17
 * #课堂点评#
 */
class DianPingPresenter : BasePresenter<DianPingView>(){
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
                if(UUser.getType()== UUser.TEACHER){
                    list.add("高2三班")
                }else{
                    list.add("数学课")
                    list.add("语文课")
                    list.add("体育课")
                }
                basePageAction.dataSucces(list,list.size)
            }
        }
    }
}
