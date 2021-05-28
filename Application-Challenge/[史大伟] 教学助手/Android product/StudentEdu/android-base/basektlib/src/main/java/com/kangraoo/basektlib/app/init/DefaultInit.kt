package com.kangraoo.basektlib.app.init

import android.app.Activity
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.SSystem
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.json.UMosh
import com.squareup.moshi.Moshi
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.ScreenUtils

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/08
 * desc : 默认的初始化
 */
class DefaultInit : IInit {

    override fun init() {
        HJson.init(UMosh(Moshi.Builder().build()))
        maininit()
    }

    private fun maininit() {
        val autosize = AutoSizeConfig.getInstance()

        autosize.designWidthInDp = SApplication.instance().sConfiger.width
        autosize.designHeightInDp = SApplication.instance().sConfiger.height
        autosize.onAdaptListener = object : onAdaptListener {
            override fun onAdaptBefore(target: Any?, activity: Activity?) {
                UUi.init(SApplication.context())
                val a1 = ScreenUtils.getScreenSize(activity)[0]
                val a2 = ScreenUtils.getScreenSize(activity)[1]
                if(a1 < a2){
                    autosize.designWidthInDp = SApplication.instance().sConfiger.width
                    autosize.designHeightInDp = SApplication.instance().sConfiger.height
                } else {
                    autosize.designWidthInDp = SApplication.instance().sConfiger.height
                    autosize.designHeightInDp = SApplication.instance().sConfiger.width
                }
            }

            override fun onAdaptAfter(target: Any?, activity: Activity?) {

            }

        }
    }

    override fun threadInit() {
    }

    override fun notMainProcessInit() {
        HJson.init(UMosh(Moshi.Builder().build()))
        maininit()
    }

    override fun notMainProcessThreadInit() {
    }
}
