package com.kangraoo.basektlib.tools.view.statebuilder

import android.content.res.ColorStateList
import android.content.res.TypedArray
import com.kangraoo.basektlib.R
import java.util.*

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/03
 * desc :
 * version: 1.0
 */
class ColorStateBuilder : BaseStateBuilder<ColorStateBuilder, Int>() {
    fun build(): ColorStateList {
        val colors: MutableList<Int> = ArrayList()
        val states: MutableList<IntArray> = ArrayList()
        putState(colors, states, stateCheckable, android.R.attr.state_checkable)
        putState(colors, states, unStateCheckable, -android.R.attr.state_checkable)
        putState(colors, states, stateChecked, android.R.attr.state_checked)
        putState(colors, states, unStateChecked, -android.R.attr.state_checked)
        putState(colors, states, stateEnabled, android.R.attr.state_enabled)
        putState(colors, states, unStateEnabled, -android.R.attr.state_enabled)
        putState(colors, states, stateSelected, android.R.attr.state_selected)
        putState(colors, states, unStateSelected, -android.R.attr.state_selected)
        putState(colors, states, statePressed, android.R.attr.state_pressed)
        putState(colors, states, unStatePressed, -android.R.attr.state_pressed)
        putState(colors, states, stateFocused, -android.R.attr.state_focused)
        putState(colors, states, unStateFocused, -android.R.attr.state_focused)
        val size = colors.size
        if (size == 0) {
            throw NullPointerException()
        }
        val colorsState = IntArray(size)
        val statesState = arrayOfNulls<IntArray>(size)
        for (i in 0 until size) {
            colorsState[i] = colors[i]
            statesState[i] = states[i]
        }
        return ColorStateList(statesState, colorsState)
    }

    fun setColorTypeArray(typedArray: TypedArray): ColorStateBuilder {
        for (i in 0 until typedArray.indexCount) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.LibColorSelector_lib_checkable_text_color) {
                stateCheckable = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_uncheckable_text_color) {
                unStateCheckable = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_checked_text_color) {
                stateChecked = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_unchecked_text_color) {
                unStateChecked = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_enabled_text_color) {
                stateEnabled = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_unenabled_text_color) {
                unStateEnabled = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_selected_text_color) {
                stateSelected = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_unselected_text_color) {
                unStateSelected = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_pressed_text_color) {
                statePressed = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_unpressed_text_color) {
                unStatePressed = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_focused_text_color) {
                stateFocused = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibColorSelector_lib_unfocused_text_color) {
                unStateFocused = typedArray.getColor(attr, 0)
            }
        }
        return this
    }

    fun setSolidColorTypeArray(typedArray: TypedArray): ColorStateBuilder {
        for (i in 0 until typedArray.indexCount) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.LibSolidColorSelector_lib_checkable_solid_color) {
                stateCheckable = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_uncheckable_solid_color) {
                unStateCheckable = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_checked_solid_color) {
                stateChecked = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_unchecked_solid_color) {
                unStateChecked = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_enabled_solid_color) {
                stateEnabled = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_unenabled_solid_color) {
                unStateEnabled = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_selected_solid_color) {
                stateSelected = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_unselected_solid_color) {
                unStateSelected = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_pressed_solid_color) {
                statePressed = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_unpressed_solid_color) {
                unStatePressed = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_focused_solid_color) {
                stateFocused = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibSolidColorSelector_lib_unfocused_solid_color) {
                unStateFocused = typedArray.getColor(attr, 0)
            }
        }
        return this
    }

    fun setStrokeColorTypeArray(typedArray: TypedArray): ColorStateBuilder {
        for (i in 0 until typedArray.indexCount) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.LibStrokeColorSelector_lib_checkable_stroke_color) {
                stateCheckable = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_uncheckable_stroke_color) {
                unStateCheckable = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_checked_stroke_color) {
                stateChecked = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_unchecked_stroke_color) {
                unStateChecked = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_enabled_stroke_color) {
                stateEnabled = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_unenabled_stroke_color) {
                unStateEnabled = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_selected_stroke_color) {
                stateSelected = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_unselected_stroke_color) {
                unStateSelected = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_pressed_stroke_color) {
                statePressed = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_unpressed_stroke_color) {
                unStatePressed = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_focused_stroke_color) {
                stateFocused = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibStrokeColorSelector_lib_unfocused_stroke_color) {
                unStateFocused = typedArray.getColor(attr, 0)
            }
        }
        return this
    }

    private fun putState(
        colors: MutableList<Int>,
        states: MutableList<IntArray>,
        state: Int?,
        attr: Int
    ) {
        if (state != null) {
            states.add(intArrayOf(attr))
            colors.add(state)
        }
    }
}
