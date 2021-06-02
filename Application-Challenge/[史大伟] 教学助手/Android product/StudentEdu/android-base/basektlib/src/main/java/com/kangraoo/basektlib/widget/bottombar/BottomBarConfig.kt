package com.kangraoo.basektlib.widget.bottombar

import com.kangraoo.basektlib.R

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/01
 * desc :
 * version: 1.0
 */
class BottomBarConfig(
    val bottomBarUnSelect: Int,
    val bottomBarSelect: Int,
    val bottomBarFontSize: Int,
    val bottomBarImgSize: Int,
    val bottomBarTextSize: Int
) {
    private constructor(builder: Builder) : this(builder.bottomBarUnSelect, builder.bottomBarSelect, builder.bottomBarFontSize, builder.bottomBarImgSize, builder.bottomBarTextSize)

    class Builder {
        var bottomBarUnSelect: Int = R.color.color_2196F3
        var bottomBarSelect: Int = R.color.color_FF5722
        var bottomBarFontSize = 18
        var bottomBarImgSize = 18
        var bottomBarTextSize = 13

        fun build() = BottomBarConfig(this)
    }
    companion object {
        @JvmStatic inline fun build(block: BottomBarConfig.Builder.() -> Unit) = BottomBarConfig.Builder().apply(block).build()
    }
}
