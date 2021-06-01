package com.kangraoo.basektlib.tools

import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.StringRes
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication

/**
 * 字体图标工具 必须将SApplication 中的默认图标打开
 * Created by shidawei on 2017/4/8.
 */
object UFont {
    val iconFont: Typeface by lazy {
        Typeface.createFromAsset(SApplication.instance().assets, "fonts/defaultIcomoon.ttf")
    }

    /**
     * 设置星星
     * @param startext
     * @param starNum
     * @param totalStar
     */
    @JvmStatic
    fun setStar(startext: TextView, starNum: Int, totalStar: Int) {
        var totalStar = totalStar
        val sb = StringBuilder()
        val context = startext.context
        if (starNum > totalStar) totalStar = starNum
        for (i in 0 until totalStar) {
            if (i < starNum) sb.append(context.getString(R.string.lib_icon_star_full)) else sb.append(context.getString(R.string.lib_icon_star_empty))
            if (i != starNum - 1) sb.append("")
        }
        startext.typeface = iconFont
        startext.text = sb.toString()
    }

    /**
     * 添加图标
     * @param tv
     * @param str
     */
    @JvmStatic
    fun setTextViewFont(tv: TextView, @StringRes str: Int) {
        tv.typeface = iconFont
        tv.text = tv.context.getString(str)
    }

    /**
     * 添加多个图标
     * @param tv
     * @param str
     */
    @JvmStatic
    fun setTextViewFonts(tv: TextView, @StringRes str: IntArray) {
        val sb = StringBuilder()
        val context = tv.context
        for (r in str) {
            sb.append(context.getString(r))
        }
        tv.typeface = iconFont
        tv.text = sb.toString()
    }
}
