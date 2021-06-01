package com.kangraoo.basektlib.tools

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import java.util.*

object UColor {
    /**
     * 颜色选择器
     *
     * @param pressedColor 按下的颜色
     * @param normalColor 正常的颜色
     * @return 颜色选择器
     */
    fun getColorStateList(pressedColor: Int, normalColor: Int): ColorStateList? {
        // 其他状态默认为白色
        return ColorStateList(
            arrayOf(
                intArrayOf(
                    R.attr.state_enabled,
                    R.attr.state_pressed
                ), intArrayOf(R.attr.state_enabled), intArrayOf()
            ), intArrayOf(pressedColor, normalColor, Color.WHITE)
        )
    }

    /**
     * 加深颜色
     *
     * @param color 原色
     * @return 加深后的
     */
    fun colorDeep(color: Int): Int {
        val alpha = Color.alpha(color)
        var red = Color.red(color)
        var green = Color.green(color)
        var blue = Color.blue(color)
        val ratio = 0.8f
        red = (red * ratio).toInt()
        green = (green * ratio).toInt()
        blue = (blue * ratio).toInt()
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * @param color 背景颜色
     * @return 前景色是否深色
     */
    fun isTextColorDark(color: Int): Boolean {
        val a =
            Color.red(color) * 0.299f + Color.green(color) * 0.587f + Color.blue(
                color
            ) * 0.114f
        return a > 180
    }

    /**
     * 按条件的到随机颜色
     *
     * @param alpha 透明
     * @param lower 下边界
     * @param upper 上边界
     * @return 颜色值
     */
    fun getRandomColor(alpha: Int, lower: Int, upper: Int): Int {
        return RandomColor(alpha, lower, upper).color
    }

    /**
     * @return 获取随机色
     */
    fun getRandomColor(): Int {
        return RandomColor(255, 80, 200).color
    }
}

/**
 * 随机颜色
 */
class RandomColor {
    var alpha: Int
    set(value) {
        field = if (value> 255) 255 else if (value <0) 0 else value
    }
    var lower: Int
    set(value) {
        field = if (value <0) 0 else value
    }
    var upper: Int
    set(value) {
        field = if (value> 255) 255 else value
    }
    constructor(alpha: Int, lower: Int, upper: Int) {
        require(upper > lower) { "must be lower < upper" }
        this.alpha = alpha
        this.lower = lower
        this.upper = upper
    }

    // 随机数是前闭  后开
    val color: Int
        get() {
            // 随机数是前闭  后开
            val red = lower + Random().nextInt(upper - lower + 1)
            val green = lower + Random().nextInt(upper - lower + 1)
            val blue = lower + Random().nextInt(upper - lower + 1)
            return Color.argb(alpha, red, green, blue)
        }
}
