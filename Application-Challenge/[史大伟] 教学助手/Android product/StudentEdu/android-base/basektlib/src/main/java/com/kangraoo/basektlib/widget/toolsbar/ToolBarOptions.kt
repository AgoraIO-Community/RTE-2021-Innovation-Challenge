package com.kangraoo.basektlib.widget.toolsbar

import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.widget.toolsbar.option.BaseLeftOption
import com.kangraoo.basektlib.widget.toolsbar.option.BaseRightOption

open abstract class ToolBarOptions {
    /**
     * toolbar的title
     */
    var titleString: String? = null

    var background: Int = R.color.color_2196F3

    var titleColor: Int = R.color.color_white

    var height: Int = UUi.getActionBarHeight(SApplication.context())

    /**
     * toolbar的右侧layout 是否展示
     */
    var isRightLayout = false

    /**
     * toolbar的左侧layout 是否展示
     */
    var isLeftLayout = false

    var leftOption: BaseLeftOption? = null

    var rightOption: BaseRightOption? = null

    abstract fun setNeedNavigate(need: Boolean)
}
