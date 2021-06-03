package com.game.tingshuo.ui

import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.LogUtils
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.game.tingshuo.R
import com.game.tingshuo.app.BaseActivity
import com.game.tingshuo.databinding.ActivityWebBinding
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : BaseActivity<ActivityWebBinding>(){

    var mAgentWeb : AgentWeb? = null

    override fun getLayout(): Int {
        return R.layout.activity_web
    }

    override fun initEventAndData() {
        var url  = intent.getStringExtra("url")
        var ti = intent.getStringExtra("title")

        title!!.visibility= View.VISIBLE
        title!!.text=ti

        LogUtils.e("url=$url,title=$ti")
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(container, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(url)
    }

}