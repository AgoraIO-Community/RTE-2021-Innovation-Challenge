package com.kangraoo.basektlib.ui.action

import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.ui.IBaseView
import com.kangraoo.basektlib.ui.impl.IBasePageView

const val BASE_PAGE_NAME = "BasePageAction"
const val PAGE_COUNT_SIZE = 15
const val CURRENT_PAGE = 1

class BasePageAction<T> : IAction<IBaseView> {

    /**
     * 下拉刷新或第一次刷新
     */
    private var isRefreshLast = true

    /**
     * 开始
     */
    var currentPage = CURRENT_PAGE

    /**
     * 页数
     */
    var pageSize = PAGE_COUNT_SIZE

    lateinit var iBaseView: IBaseView
    lateinit var iPageBaseView: IBasePageView<T>

    override fun setBaseView(iBaseView: IBaseView) {
        this.iBaseView = iBaseView
        if (iBaseView is IBasePageView<*>) {
            iPageBaseView = iBaseView as IBasePageView<T>
        }
    }
    override fun initAction() {
    }

    override fun detach() {
        dataListener = null
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun getName() = BASE_PAGE_NAME

    var dataListener: DataListener<T>? = null

    interface DataListener<T> {
        fun request(basePageAction: BasePageAction<T>, currentPage: Int,pageSize : Int)
    }

    private fun makeCurrentPage() {
        currentPage++
        ULog.d("当前",currentPage)
        isRefreshLast = false
    }

    /**
     * 刷新
     */
    fun refreshData() {
        /**
         * dialog 根据业务来展示
         */
//        iBaseView?.showProgressingDialog()
        isRefreshLast = true
        currentPage = 1
        dataListener?.request(this, currentPage,pageSize)
    }
    /**
     * 刷新已经加载的数据
     */
    fun refreshLoadData(){
        isRefreshLast = true
        ULog.d("当前",currentPage,"总共",pageSize*currentPage)
        dataListener?.request(this, 1,pageSize*(currentPage--))
    }

    /**
     * 加载
     */
    fun loadMore() {
        isRefreshLast = false
        dataListener?.request(this, currentPage,pageSize)
    }

    fun dataSucces(data: List<T>?, total: Int) {
        iBaseView.dismissProgressDialog()
        iPageBaseView.loadMoreCompleted()
        iPageBaseView.refreshCompleted()
        if (total == 0) {
            if (isRefreshLast) {
                iPageBaseView.setData(arrayListOf(), isRefreshLast)
                iPageBaseView.emptyPage()
            }
        } else {
            if (data == null || data.isEmpty()) {
                if (isRefreshLast) {
                    iPageBaseView.setData(arrayListOf(), isRefreshLast)
                    iPageBaseView.emptyPage()
                }
            } else {
                ULog.i("page$currentPage,total$total")
                iPageBaseView.setData(data, isRefreshLast)
                if (pageSize * currentPage >= total) {
                    ULog.i(SApplication.context().getString(R.string.libLodingFinish))
                    iPageBaseView.lastData()
                }
                makeCurrentPage()
            }
        }
    }

    fun dataError(error: DataResult.Error) {
        iBaseView.dismissProgressDialog()
        iPageBaseView.loadMoreCompleted()
        iPageBaseView.refreshCompleted()
        if (isRefreshLast) {
            iPageBaseView.onLoadFail(error.exception)
        } else {
            ULog.i(SApplication.context().getString(R.string.LibLoadingFailed))
            iPageBaseView.moreLoadFail(error.exception)
        }
    }
}
