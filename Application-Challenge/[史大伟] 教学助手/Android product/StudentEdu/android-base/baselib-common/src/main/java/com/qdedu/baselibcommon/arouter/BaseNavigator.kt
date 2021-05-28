package com.qdedu.baselibcommon.arouter

import android.content.Context
import android.os.Bundle
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.URouter
import com.qdedu.baselibcommon.ui.activity.WebPageActivity
import com.qdedu.baselibcommon.data.AppHuanJingFactory

object BaseNavigator {

    fun navigationWebPage(
        context: Context,
        pageUrl: String,
        title: String = "",
        params: Map<String, String>? = null,
        statusBarColor: String? = null,
        titleVisible: Boolean = true
    ) {
        var url = if (!pageUrl.startsWith("http://") && !pageUrl.startsWith("https://")) {
            AppHuanJingFactory.appModel.h5Domains + pageUrl
        } else {
            pageUrl
        }
        if (params != null) {
            url += "?${HString.getNetString(params)}"
        }
        WebPageActivity.start(
            context,
            url,
            title,
            statusBarColor,
            titleVisible
        )
    }

//    fun navigationToActivity(context: Context,path:String) {
//        URouter.navigation(context,path)
//    }
    fun navigationToActivity(context: Context, path: String) {
        URouter.navigation(context, path)
    }

    fun navigationToActivity(context: Context,path:String,bundle: Bundle? = null,flags:Int? = null) {
        URouter.navigation(context, path, bundle, flags)
    }

    fun navigationToActivity(context: Context, path: String, bundle: Bundle) {
        URouter.navigation(context, path, bundle)
    }

    @Deprecated("")
    fun navigationToReadingBookList(context: Context, path: String, int: Int) {
        val bundle = Bundle()
        bundle.putInt("from", int)
        URouter.navigation(context, BaseRouterHub.ROTE_READING_SUPERIOR_BOOK, bundle)
    }
    @Deprecated("")
    fun navigationToReadingHome(context: Context) {
        URouter.navigation(context, BaseRouterHub.ROUTE_READING_HOME)
    }
    @Deprecated("")
    fun navigationToMyReading(context: Context) {
        URouter.navigation(context, BaseRouterHub.ROUTE_READING_WORKS)
    }

    @Deprecated("")
    fun navigationToKefu(context: Context, fromTitle: String) {
        ServiceProvider.kefuService?.startKefuActivity(context, fromTitle)
    }

}