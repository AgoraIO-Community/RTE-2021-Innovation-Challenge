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
import com.kangaroo.studentedu.ui.view.KaoQingListView
import com.kangaroo.studentedu.ui.presenter.KaoQingListPresenter
import kotlinx.android.synthetic.main.activity_kao_qing_list.*
import com.kangaroo.studentedu.ui.adapter.KaoQingListAdapter
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import android.app.Activity
import android.content.DialogInterface
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import android.graphics.Color
import android.util.TypedValue
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.gyf.immersionbar.ktx.immersionBar
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.widget.emptypage.AbsEmptyPage
import com.kangraoo.basektlib.widget.emptypage.DefaultEmptyPage
import com.kangraoo.basektlib.widget.emptypage.EmptyPageLayout
import com.kangraoo.basektlib.widget.emptypage.EmptyType
import com.kangraoo.basektlib.ui.mvp.BMvpActivity
import com.kangraoo.basektlib.widget.dialog.LibCheckDialog

/**
 * 自动生成：by WaTaNaBe on 2021-05-26 15:42
 * #考勤列表#
 */
class KaoQingListActivity : BMvpActivity<KaoQingListView ,KaoQingListPresenter>(),KaoQingListView{

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, KaoQingListActivity::class.java)
        }

    }

    var adapter: KaoQingListAdapter? = null

    override fun getLayoutId() = R.layout.activity_kao_qing_list


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.transparent)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "考勤列表"
        libToolBarOptions.setNeedNavigate(true)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener(){})

        adapter = KaoQingListAdapter()

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

        all.setOnCheckedChangeListener { buttonView, isChecked ->
            for(i in adapter!!.list.indices){
                adapter!!.list[i] = isChecked
            }
            adapter!!.notifyDataSetChanged()
        }
        val libCheckDialog = LibCheckDialog(visitActivity())

        libCheckDialog.title("提示")
        libCheckDialog.content("确定今天学生的考勤吗")
        libCheckDialog.sureVisable(View.VISIBLE)
        libCheckDialog.cancleVisable(View.VISIBLE)
        libCheckDialog.sure("确定")
        libCheckDialog.cancle("取消")
        libCheckDialog.onLibDialogListener =
            (object : LibCheckDialog.OnLibCheckDialogListener {
                override fun onSure() {
                    showToastMsg("点击确定")
                    libCheckDialog.dismiss()
                    showToastMsg(Tip.Success,"今天考勤已完成")
                    finish()
                }

                override fun onCancle() {
                }

                override fun onShow() {
                }

                override fun onDismiss(dialog: DialogInterface) {
                }

            })
        kaoqin.setOnClickListener {

            libCheckDialog.show()

        }
    }

    var emptyView:EmptyPageLayout? = null
    var netErrorView:EmptyPageLayout? = null

    override fun createPresenterInstance(): KaoQingListPresenter {
        return KaoQingListPresenter()
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


    override fun setData(data: List<String>, isRefreshLast: Boolean) {
        adapter!!.loadMoreModule.isEnableLoadMore = true
        if(isRefreshLast){
            val entitys: MutableList<String> = ArrayList()
            entitys.addAll(data)
            adapter!!.list.clear()
            entitys.forEach {
                adapter!!.list.add(false)
            }

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
