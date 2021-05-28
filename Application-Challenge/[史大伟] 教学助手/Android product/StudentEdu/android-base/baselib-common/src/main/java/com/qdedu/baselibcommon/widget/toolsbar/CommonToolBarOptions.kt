package com.qdedu.baselibcommon.widget.toolsbar

import com.kangraoo.basektlib.widget.toolsbar.ToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.option.BaseLeftOption
import com.kangraoo.basektlib.widget.toolsbar.option.BaseRightOption
import com.qdedu.baselibcommon.R

class CommonToolBarOptions: ToolBarOptions() {

    init {
        background = R.color.color_white
        titleColor = R.color.color_333333
        isLeftLayout = true
        isRightLayout = true
        leftOption = BaseLeftOption()
        rightOption = BaseRightOption()
        leftOption!!.image1 = R.mipmap.public_common_icon_back_black
        leftOption?.isImage1 = true
    }

    override fun setNeedNavigate(need: Boolean) {
        leftOption?.isImage1 = need
    }

    fun setRightText(text:Int){
        rightOption?.text1 = text
    }

    fun isRightText(boolean: Boolean){
        rightOption?.isText1 = boolean
    }
    fun isLeftText(boolean: Boolean){
        leftOption?.isText1 = boolean
    }

    fun setLeftText(text:Int){
        leftOption?.text1 = text
    }

    fun setRightImage(id: Int){
        rightOption?.image1 = id
    }

    fun isRightImage(boolean: Boolean){
        rightOption?.isImage1 = boolean
    }

    fun setRightImageWH(w:Int,h:Int){
        rightOption?.image1width = w
        rightOption?.image1height = h
    }

}