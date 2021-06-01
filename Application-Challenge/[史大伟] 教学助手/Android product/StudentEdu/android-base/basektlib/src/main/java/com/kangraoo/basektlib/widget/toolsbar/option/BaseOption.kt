package com.kangraoo.basektlib.widget.toolsbar.option

import android.graphics.Typeface
import android.view.ViewGroup
import com.kangraoo.basektlib.R

open class BaseOption {
    var image1: Int? = null
    var image2: Int? = null

    var text1: Int? = null
    var text2: Int? = null

    var text1Type: Typeface? = null
    var text2Type: Typeface? = null

    var isImage1: Boolean = false
    var isImage2: Boolean = false

    var isText1: Boolean = false
    var isText2: Boolean = false

    var text1Size: Int = 15
    var text2Size: Int = 15

    var text1Color: Int = R.color.color_333333
    var text2Color: Int = R.color.color_333333

    var image1width: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    var image2width: Int = ViewGroup.LayoutParams.WRAP_CONTENT

    var image1height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    var image2height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
}
