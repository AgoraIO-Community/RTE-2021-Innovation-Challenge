package com.qdedu.baselibcommon.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.kangraoo.basektlib.ui.BFragment
import com.qdedu.baselibcommon.R
import com.qdedu.baselibcommon.app.URL
import com.qdedu.baselibcommon.ui.activity.WebPageActivity
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.public_common_fragment_web_page_frgment.*
import java.util.*

@Deprecated("换agentweb")
class WebPageFrgment : BFragment() {

    override fun getLayoutId() = R.layout.public_common_fragment_web_page_frgment
    private lateinit var url: String

    companion object {
        @JvmStatic
        fun newInstance(url:String):WebPageFrgment {
            val fragment = WebPageFrgment()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var map = WebPageActivity.map
        for ((key,value) in map){
            dsWebview.addJavascriptObject(value,key)
        }
        dsWebview.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(p0: WebView?, p1: Int) {
                super.onProgressChanged(p0, p1)
                if(p1==100){
                    progressBar?.visibility = (View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar?.visibility = (View.VISIBLE);//开始加载网页时显示进度条
                    progressBar?.progress = (p1);//设置进度值
                }
            }
        }
        dsWebview.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                p0!!.loadUrl(p1)

                return super.shouldOverrideUrlLoading(p0, p1)

            }
        }

        if (url.startsWith("https://wx.tenpay.com")) {
            val urL = java.net.URL(AppHuanJingFactory.appModel.h5Domains)
            val referer: MutableMap<String, String> =
                HashMap()
            referer["Referer"] = urL.protocol + "://" + urL.host
            dsWebview.loadUrl(url,referer)
        }else{
            dsWebview.loadUrl(url)
        }

    }



    override fun onFragmentPause() {
        dsWebview.apply {
            var map = WebPageActivity.mapPause
            for ((key,value) in map){
                callHandler(key,value)
            }
        }
        dsWebview.onPause()
        dsWebview.pauseTimers()
        super.onFragmentPause()
    }

    override fun onFragmentResume() {
        dsWebview.resumeTimers()
        dsWebview.onResume()
        super.onFragmentResume()
    }

    override fun onDestroyView() {
        dsWebview.loadUrl("about:blank")
        dsWebview.stopLoading()
        if (dsWebview.handler != null) dsWebview.handler.removeCallbacksAndMessages(null)
        dsWebview.removeAllViewsInLayout()
        dsWebview.removeAllViews()
        dsWebview.webChromeClient = null
        dsWebview.webViewClient = null
        dsWebview.tag = null
        dsWebview.clearHistory()
        (dsWebview.parent as ViewGroup).removeView(dsWebview)
        dsWebview.destroy()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }




}