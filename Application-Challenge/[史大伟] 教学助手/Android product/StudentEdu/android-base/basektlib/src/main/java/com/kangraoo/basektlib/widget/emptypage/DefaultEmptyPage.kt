package com.kangraoo.basektlib.widget.emptypage

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HNetwork.checkNetwork
import com.kangraoo.basektlib.tools.UFont
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast
import com.kangraoo.basektlib.widget.listener.OnOnceClickListener

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/08/03
 * desc :
 */
class DefaultEmptyPage : AbsEmptyPage() {
    lateinit var tipMsg: TextView
    lateinit var anchor: TextView
    lateinit var refresh: TextView
    lateinit var anchorImg: ImageView
    override val layoutId: Int
        get() = R.layout.lib_widget_empty_page

    override fun init(view: View) {
        anchor = findViewById(view, R.id.anchor)
        anchorImg = findViewById(view, R.id.anchorImg)
        tipMsg = findViewById(view, R.id.tipMsg)
        refresh = findViewById(view, R.id.refresh)
        refresh.setOnClickListener(object : OnOnceClickListener() {
            override fun onOnceClick(v: View) {
                if (onRefreshDelegate != null) {
                    if (!checkNetwork(SApplication.context())) {
                        TipToast.tip(Tip.Error, R.string.libNetCheck)
                        return
                    }
                    onRefreshDelegate!!.onRefresh()
                }
            }
        })
    }

    override fun setEmptyType(type: EmptyType) {
        tipMsg.text = type.message
        if (type is EmptyType.DiyImgPageType) {
            anchorImg.visibility = View.VISIBLE
            anchor.visibility = View.GONE
            anchorImg.setImageResource(type.image)
        } else {
            anchorImg.visibility = View.GONE
            anchor.visibility = View.VISIBLE
            UFont.setTextViewFont(anchor, type.image)
        }
        if (type.button) {
            refresh.visibility = View.VISIBLE
        } else {
            refresh.visibility = View.GONE
        }
    }

    override fun buttonStyle(background: Int, textColor: Int) {
        refresh.setBackgroundResource(background)
        refresh.setTextColor(ContextCompat.getColor(SApplication.context(), textColor))
    }

    override fun buttonText(text: String) {
        refresh.text = text
    }
}
