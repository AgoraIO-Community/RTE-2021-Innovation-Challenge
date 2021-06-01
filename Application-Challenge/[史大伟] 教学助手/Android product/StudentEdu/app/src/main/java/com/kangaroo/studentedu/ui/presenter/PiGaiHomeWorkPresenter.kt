package com.kangaroo.studentedu.ui.presenter

import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.data.model.DianPing
import com.kangaroo.studentedu.data.model.HomeWork
import com.kangaroo.studentedu.tools.UUser
import com.kangaroo.studentedu.ui.view.PiGaiHomeWorkView
import com.kangraoo.basektlib.ui.mvp.BasePresenter
import com.kangraoo.basektlib.ui.action.BASE_PAGE_NAME
import com.kangraoo.basektlib.ui.action.BasePageAction
import com.kangraoo.basektlib.data.DataResult
import kotlinx.coroutines.launch

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 13:20
 * #批改作业#
 */
class PiGaiHomeWorkPresenter : BasePresenter<PiGaiHomeWorkView>(){
	var basePageAction:BasePageAction<HomeWork> = BasePageAction()
    init {
        addAction(BASE_PAGE_NAME,basePageAction)
        basePageAction.dataListener = object : BasePageAction.DataListener<HomeWork>{
            override fun request(
                basePageAction: BasePageAction<HomeWork>,
                currentPage: Int,
                pageSize : Int
            ) {
                var list = arrayListOf<HomeWork>()
                list.add(HomeWork("第一次"+UUser.getKe()+"作业",R.mipmap.teacherjiangke,"老师讲课，非一般的感觉","第一次"+UUser.getKe()+"作业","","看图说话",R.mipmap.teacherjiangke,"xiaoming"))
                list.add(HomeWork("第一次"+UUser.getKe()+"作业",R.mipmap.teacherjiangke,"老师讲课，感觉用了飘柔","第一次"+UUser.getKe()+"作业","","看图说话",R.mipmap.teacherjiangke,"sdw"))
                basePageAction.dataSucces(list,list.size)

            }
        }
    }
}
