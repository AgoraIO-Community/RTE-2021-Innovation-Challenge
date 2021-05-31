package com.qdedu.baselibcommon.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.kangraoo.basektlib.tools.UFragment
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.ui.BActivity
import com.qdedu.baselibcommon.R
import com.qdedu.baselibcommon.app.STATUS_BAR_COLOR
import com.qdedu.baselibcommon.app.TITLE
import com.qdedu.baselibcommon.app.TITLE_VISIBLE
import com.qdedu.baselibcommon.app.URL
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.qdedu.baselibcommon.bridge.BaseJsApi
import com.qdedu.baselibcommon.ui.fragment.AgentWebPageFrgment
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions

/**
 * 默认web展示
 */
class WebPageActivity : BActivity() {

    companion object{

        var map:MutableMap<String,Any> = HashMap()
        var mapPause:MutableMap<String,Array<*>> = HashMap()
        fun addWebPause(k: String, any:  Array<*>) {
            mapPause[k] = any
        }
        fun addJsObject(k: String, any: Any) {
            map[k] = any
        }

        fun start(
            context: Context?,
            pageUrl: String?,
            title: String? = null,
            statusBarColor: String? = null,
            titleVisible: Boolean
        ) {
            val intent = Intent(context, WebPageActivity::class.java)
            intent.putExtra(URL, pageUrl)
            intent.putExtra(TITLE, title)
            intent.putExtra(STATUS_BAR_COLOR, statusBarColor)
            intent.putExtra(TITLE_VISIBLE, titleVisible)
            context!!.startActivity(intent)
        }
    }

    private var url: String? = null
    private var title: String? = null
    private var statusBarColor: String? = null
    private var titleVisible = true


    override fun getLayoutId() = R.layout.public_common_activity_web_page

    override fun onViewCreated(savedInstanceState: Bundle?) {
        this.intent?.let {
            url = it.getStringExtra(URL)
            title = it.getStringExtra(TITLE)
            statusBarColor = it.getStringExtra(STATUS_BAR_COLOR)
            titleVisible = it.getBooleanExtra(TITLE_VISIBLE,true)
        }

        ULog.i(url,title,statusBarColor,titleVisible)

        immersionBar {
            statusBarDarkFont(true)
            if(statusBarColor!=null){
                statusBarColor( statusBarColor )
            }else{
                statusBarColor(R.color.color_white)
            }
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = title
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener() {
            override fun onNavigate(view: View) {
                onBackPressed()
            }

            override fun onRight(view: View) {
            }
        })

        if(!titleVisible){
            toolBarVisable()
        }

        UFragment.addFragmentToActivity(supportFragmentManager,
            AgentWebPageFrgment.newInstance(url!!),R.id.fl_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ServiceProvider.iShareService?.shareOnActivityResult(this,requestCode, resultCode, data)
    }
}