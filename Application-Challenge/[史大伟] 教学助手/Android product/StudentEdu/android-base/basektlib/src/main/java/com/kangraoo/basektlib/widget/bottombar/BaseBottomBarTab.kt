package com.kangraoo.basektlib.widget.bottombar

import android.content.Context
import android.os.Build
import android.widget.FrameLayout
import com.kangraoo.basektlib.R

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/01/18
 * desc :
 * version: 1.0
 */
abstract class BaseBottomBarTab constructor(context: Context) : FrameLayout(context) {

    var tabPosition = -1

    init {
        // 设置背景 波纹超出边界
        val typedArray = context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackgroundBorderless))
        val drawable = typedArray.getDrawable(0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = drawable
        } else {
            setBackgroundDrawable(drawable)
        }
        // 回收
        typedArray.recycle()
    }
}
