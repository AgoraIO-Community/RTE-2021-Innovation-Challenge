package com.kangraoo.basektlib.tools.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater.Factory2
import android.view.View

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/04
 * desc :
 * version: 1.0
 */
open class ViewFactory(private val factory2: Factory2) : Factory2 {
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return factory2.onCreateView(parent, name, context, attrs)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return factory2.onCreateView(name, context, attrs)
    }
}
