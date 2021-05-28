package com.kangraoo.basektlib.widget.toolsbar

import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.tools.UFont
import com.kangraoo.basektlib.widget.toolsbar.option.BaseLeftOption

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/09
 * desc :
 * version: 1.0
 */
class LibToolBarOptions : ToolBarOptions() {
    init {
        isLeftLayout = true
        leftOption = BaseLeftOption()
        leftOption!!.text1 = R.string.lib_icon_circle_left
        leftOption!!.text1Type = UFont.iconFont
        leftOption!!.isText1 = true
    }

    override fun setNeedNavigate(need: Boolean) {
        leftOption?.isText1 = need
    }
}
