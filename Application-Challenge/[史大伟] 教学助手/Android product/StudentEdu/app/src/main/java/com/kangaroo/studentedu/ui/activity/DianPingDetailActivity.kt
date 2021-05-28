package com.kangaroo.studentedu.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.toolsbar.LibToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.ui.view.DianPingDetailView
import com.kangaroo.studentedu.ui.presenter.DianPingDetailPresenter
import kotlinx.android.synthetic.main.activity_dian_ping_detail.*
import com.kangaroo.studentedu.ui.adapter.DianPingDetailAdapter
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import android.app.Activity
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import android.graphics.Color
import android.util.TypedValue
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.gyf.immersionbar.ktx.immersionBar
import com.kangaroo.studentedu.data.model.DianPing
import com.kangraoo.basektlib.widget.emptypage.AbsEmptyPage
import com.kangraoo.basektlib.widget.emptypage.DefaultEmptyPage
import com.kangraoo.basektlib.widget.emptypage.EmptyPageLayout
import com.kangraoo.basektlib.widget.emptypage.EmptyType
import com.kangraoo.basektlib.ui.mvp.BMvpActivity
import com.qdedu.autopage.AutoPage

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 09:32
 * #课堂点评详情#
 */
class DianPingDetailActivity : BMvpActivity<DianPingDetailView ,DianPingDetailPresenter>(),DianPingDetailView{

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, DianPingDetailActivity::class.java)
        }

    }

    @JvmField
    @AutoPage
    var kecheng: String? = null

    var adapter: DianPingDetailAdapter? = null

    override fun getLayoutId() = R.layout.activity_dian_ping_detail


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.transparent)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "课堂点评详情"
        libToolBarOptions.setNeedNavigate(true)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener(){})
        _presenter.kecheng = kecheng
        adapter = DianPingDetailAdapter()

        refreshLayout.setEnableLoadMore(false)
        refreshLayout.setOnRefreshListener {
            adapter!!.loadMoreModule.isEnableLoadMore = false
            _presenter.basePageAction!!.refreshData()
        }

        recycler.layoutManager = LinearLayoutManager(visitActivity())
        visitActivity().dividerBuilder()
            .color(Color.TRANSPARENT)
            .size(1, TypedValue.COMPLEX_UNIT_DIP)
            .build()
            .addTo(recycler)


        adapter!!.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener {_presenter.basePageAction.loadMore() })

        adapter!!.loadMoreModule.isAutoLoadMore = true

        recycler.adapter = adapter


        adapter!!.loadMoreModule.isEnableLoadMore = false

        showProgressingDialog()
        _presenter.basePageAction.refreshData()

        emptyView = EmptyPageLayout(visitActivity(), DefaultEmptyPage())
        emptyView!!.setEmptyType(EmptyType.EmptyPageType())

        var emptyPage = DefaultEmptyPage()
        netErrorView = EmptyPageLayout(visitActivity(),emptyPage)
        netErrorView!!.setEmptyType(EmptyType.NetWorkErrorType())
        netErrorView!!.setButtonClickListener(object :AbsEmptyPage.OnRefreshDelegate{
            override fun onRefresh() {
                showProgressingDialog()
                _presenter.basePageAction.refreshData()
            }
        })

    }

    var emptyView:EmptyPageLayout? = null
    var netErrorView:EmptyPageLayout? = null

    override fun createPresenterInstance(): DianPingDetailPresenter {
        return DianPingDetailPresenter()
    }

    override fun refreshCompleted() {
        refreshLayout.finishRefresh()
    }

    override fun loadMoreCompleted() {
        adapter!!.loadMoreModule.loadMoreComplete()
    }

    override fun emptyPage() {
        adapter!!.setEmptyView(emptyView!!)
    }


    override fun setData(data: List<DianPing>, isRefreshLast: Boolean) {
        adapter!!.loadMoreModule.isEnableLoadMore = true
        if(isRefreshLast){
            val entitys: MutableList<DianPing> = ArrayList()
            entitys.addAll(data)
            adapter!!.setNewInstance(entitys)
        }else{
            adapter!!.addData(data)
        }
    }

    override fun lastData() {

        //如果不够一页,显示没有更多数据布局
        adapter!!.loadMoreModule.loadMoreEnd()
    }

    override fun moreLoadFail(e: Exception) {
        adapter!!.loadMoreModule.isEnableLoadMore = true

        adapter!!.loadMoreModule.loadMoreFail()
    }

    override fun onLoadFail(e: Exception) {

        adapter!!.setEmptyView(netErrorView!!)
    }

}
