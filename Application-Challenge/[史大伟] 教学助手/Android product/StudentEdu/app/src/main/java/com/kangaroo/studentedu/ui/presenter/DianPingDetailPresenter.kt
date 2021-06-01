package com.kangaroo.studentedu.ui.presenter

import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.data.model.DianPing
import com.kangaroo.studentedu.ui.view.DianPingDetailView
import com.kangraoo.basektlib.ui.mvp.BasePresenter
import com.kangraoo.basektlib.ui.action.BASE_PAGE_NAME
import com.kangraoo.basektlib.ui.action.BasePageAction
import com.kangraoo.basektlib.data.DataResult
import kotlinx.coroutines.launch

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 09:32
 * #课堂点评详情#
 */
class DianPingDetailPresenter : BasePresenter<DianPingDetailView>(){
    var kecheng: String? = null

    var basePageAction:BasePageAction<DianPing> = BasePageAction()
    init {
        addAction(BASE_PAGE_NAME,basePageAction)
        basePageAction.dataListener = object : BasePageAction.DataListener<DianPing>{
            override fun request(
                basePageAction: BasePageAction<DianPing>,
                currentPage: Int,
                pageSize : Int
            ) {
                var list = arrayListOf<DianPing>()
                list.add(DianPing(5,"sdw", R.mipmap.teacherjiangke,kecheng+"的老师讲的好棒啊！！","第一节"+kecheng))
                list.add(DianPing(4,"xiaoming", R.mipmap.teacherjiangke,kecheng+"的老师还要多多互动啊，都没让我回答！！","第一节"+kecheng))
                basePageAction.dataSucces(list,list.size)

            }
        }
    }
}
