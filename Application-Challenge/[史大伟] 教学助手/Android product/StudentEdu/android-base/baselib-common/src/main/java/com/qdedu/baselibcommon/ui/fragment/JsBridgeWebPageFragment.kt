package com.qdedu.baselibcommon.ui.fragment

import android.os.Bundle
import android.view.View
import com.kangraoo.basektlib.ui.BFragment
import com.qdedu.baselibcommon.R
import com.qdedu.baselibcommon.app.URL
import com.tencent.smtt.sdk.WebSettings
import kotlinx.android.synthetic.main.public_common_fragment_js_bridge_web_page.*
@Deprecated("换agentweb")
class JsBridgeWebPageFragment : BFragment() {


    override fun getLayoutId() = R.layout.public_common_fragment_js_bridge_web_page
    private lateinit var url: String

    companion object {
        @JvmStatic
        fun newInstance(url: String):JsBridgeWebPageFragment {
            val fragment = JsBridgeWebPageFragment()
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
        val webSettings: WebSettings = webview.getSettings()

        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true //屏幕自适应

        webSettings.loadWithOverviewMode = true //屏幕自适应

        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE //不使用缓存

        webSettings.domStorageEnabled = true

        webview.loadUrl(url)

    }



    override fun onFragmentResume() {
        super.onFragmentResume()
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}