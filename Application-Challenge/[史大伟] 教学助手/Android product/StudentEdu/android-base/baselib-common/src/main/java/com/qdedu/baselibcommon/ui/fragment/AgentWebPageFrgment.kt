package com.qdedu.baselibcommon.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.just.agentwebX5.AgentWebX5
import com.just.agentwebX5.DefaultWebClient
import com.just.agentwebX5.WebDefaultSettingsManager
import com.kangraoo.basektlib.ui.BFragment
import com.qdedu.baselibcommon.R
import com.qdedu.baselibcommon.ui.activity.WebPageActivity
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import kotlinx.android.synthetic.main.public_common_fragment_agent_web_page_frgment.*
import wendu.dsbridgex.DWebView
import com.qdedu.baselibcommon.app.URL

class AgentWebPageFrgment : BFragment() {
    override fun getLayoutId() = R.layout.public_common_fragment_agent_web_page_frgment
    private lateinit var url: String

    companion object {
        @JvmStatic
        fun newInstance(url:String):AgentWebPageFrgment {
            val fragment = AgentWebPageFrgment()
            val bundle = Bundle()
            bundle.putString(URL, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            url = bundle.getString(URL)!!
        }
    }
    private var dsWebview: DWebView? = null
    private var mAgentWeb: AgentWebX5? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dsWebview = DWebView(activity)
        var map = WebPageActivity.map
        for ((key,value) in map){
            dsWebview?.addJavascriptObject(value,key)
        }

        val agentWebBuilder = AgentWebX5.with(this)
            .setAgentWebParent(
                web_page_fl_parent,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            .setIndicatorColorWithHeight(ContextCompat.getColor(visitActivity(),R.color.lib_color_moccasin), 2)
            .setWebSettings(WebDefaultSettingsManager.getInstance())
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .setSecurityType(AgentWebX5.SecurityType.strict)
            .interceptUnkownScheme()
        if (url.startsWith("https://wx.tenpay.com")) {
            val urL = java.net.URL(AppHuanJingFactory.appModel.h5Domains)
            agentWebBuilder.additionalHttpHeaders("Referer",urL.protocol + "://" + urL.host)
        }
        mAgentWeb = agentWebBuilder.setWebView(dsWebview)
            .createAgentWeb()
            .ready().go(url)
    }



    override fun onFragmentResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onFragmentResume()
    }

    override fun onFragmentPause() {
        dsWebview?.apply {
            var map = WebPageActivity.mapPause
            for ((key,value) in map){
                callHandler(key,value)
            }
        }
        mAgentWeb?.webLifeCycle?.onPause()
        super.onFragmentPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()

    }
}