package com.kangraoo.basektlib.widget.emptypage

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/05
 * desc :
 * version: 1.0
 */
class EmptyPageManager(context: Context, iEmptyPage: IEmptyPage, var viewGroup: ViewGroup) {
    var emptyPageLayout: EmptyPageLayout
    var showEmptyPage: Boolean

    init {
        val params = ViewGroup.LayoutParams(viewGroup.getChildAt(0).layoutParams)
        emptyPageLayout = EmptyPageLayout(context, iEmptyPage)
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        emptyPageLayout.layoutParams = params
        emptyPageLayout.visibility = View.GONE
        viewGroup.addView(emptyPageLayout)
        showEmptyPage = false
    }

    fun showEmptyPage(type: EmptyType) {
        showEmptyPage = true
        val size = viewGroup.childCount
        for (i in 0 until size) {
            val view = viewGroup.getChildAt(i)
            if (view is EmptyPageLayout) {
                view.setEmptyType(type)
                view.setVisibility(View.VISIBLE)
            } else {
                view.visibility = View.GONE
            }
        }
    }

    fun hideEmptyPage() {
        if (!showEmptyPage) {
            return
        }
        showEmptyPage = false
        val size = viewGroup.childCount
        for (i in 0 until size) {
            val view = viewGroup.getChildAt(i)
            if (view is EmptyPageLayout) {
                view.setVisibility(View.GONE)
            } else {
                view.visibility = View.VISIBLE
            }
        }
    }
}
