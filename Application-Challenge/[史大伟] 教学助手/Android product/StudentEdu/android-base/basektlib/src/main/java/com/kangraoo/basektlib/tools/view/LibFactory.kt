package com.kangraoo.basektlib.tools.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater.Factory2
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.kangraoo.basektlib.widget.animview.LibLoadView
import com.kangraoo.basektlib.widget.bottombar.LibBottomBar
import com.kangraoo.basektlib.widget.toolsbar.LibToolBar

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/04
 * desc :
 * version: 1.0
 */
class LibFactory(var delegate: AppCompatDelegate) : Factory2 {
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val view = delegate.createView(parent, name, context, attrs) // 调用系统的方法
        if (view == null) {
            if (TextUtils.isEmpty(name)) {
                return null
            }
            when (name) {
                "LibToolBar" -> return LibToolBar(context, attrs)
                "LibBottomBar" -> return LibBottomBar(context)
                "LibLoadView" -> return LibLoadView(context)
            }
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }
}
