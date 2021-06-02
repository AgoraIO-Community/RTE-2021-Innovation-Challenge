package com.kangraoo.basektlib.tools.view.statebuilder

import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import com.kangraoo.basektlib.R

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/03
 * desc :
 * version: 1.0
 */
class StateListDrawableBuilder : BaseStateBuilder<StateListDrawableBuilder, Drawable>() {
    fun build(): Drawable? {
        val stateListDrawable = StateListDrawable()
        putState(stateListDrawable, stateCheckable, android.R.attr.state_checkable)
        putState(stateListDrawable, stateChecked, android.R.attr.state_checked)
        putState(stateListDrawable, stateEnabled, android.R.attr.state_enabled)
        putState(stateListDrawable, stateSelected, android.R.attr.state_selected)
        putState(stateListDrawable, statePressed, android.R.attr.state_pressed)
        putState(stateListDrawable, stateFocused, android.R.attr.state_focused)
        putState(stateListDrawable, stateHovered, android.R.attr.state_hovered)
        putState(stateListDrawable, stateActivated, android.R.attr.state_activated)
        putState(stateListDrawable, unStateCheckable, -android.R.attr.state_checkable)
        putState(stateListDrawable, unStateChecked, -android.R.attr.state_checked)
        putState(stateListDrawable, unStateEnabled, -android.R.attr.state_enabled)
        putState(stateListDrawable, unStateSelected, -android.R.attr.state_selected)
        putState(stateListDrawable, unStatePressed, -android.R.attr.state_pressed)
        putState(stateListDrawable, unStateFocused, -android.R.attr.state_focused)
        putState(stateListDrawable, unStateHovered, -android.R.attr.state_hovered)
        putState(stateListDrawable, unStateActivated, -android.R.attr.state_activated)
        return buildRipple(stateListDrawable)
    }

    var shapeBuilder: ShapeBuilder? = null
    fun setShapeTypeArray(shapeArray: TypedArray?): StateListDrawableBuilder {
        shapeBuilder = ShapeBuilder().setShapeTypeArray(
            shapeArray!!
        )
        return this
    }

    fun setDrawableButtonTypeArray(buttonArray: TypedArray): StateListDrawableBuilder {
        for (i in 0 until buttonArray.indexCount) {
            val attr = buttonArray.getIndex(i)
            if (attr == R.styleable.LibButtonDrawable_lib_checked_button_drawable) {
                stateChecked = setSelectorDrawable(buttonArray, attr)
            } else if (attr == R.styleable.LibButtonDrawable_lib_unchecked_button_drawable) {
                unStateChecked = setSelectorDrawable(buttonArray, attr)
            }
        }
        return this
    }

    fun setDrawableTypeArray(drawableArray: TypedArray): StateListDrawableBuilder {
        for (i in 0 until drawableArray.indexCount) {
            val attr = drawableArray.getIndex(i)
            if (attr == R.styleable.LibDrawableSelector_lib_checkable_drawable) {
                stateCheckable = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_uncheckable_drawable) {
                unStateCheckable = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_checked_drawable) {
                stateChecked = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_unchecked_drawable) {
                unStateChecked = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_enabled_drawable) {
                stateEnabled = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_unenabled_drawable) {
                unStateEnabled = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_selected_drawable) {
                stateSelected = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_unselected_drawable) {
                unStateSelected = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_pressed_drawable) {
                statePressed = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_unpressed_drawable) {
                unStatePressed = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_focused_drawable) {
                stateFocused = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_unfocused_drawable) {
                unStateFocused = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_focused_hovered) {
                stateHovered = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_unfocused_hovered) {
                unStateHovered = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_focused_activated) {
                stateActivated = setSelectorDrawable(drawableArray, attr)
            } else if (attr == R.styleable.LibDrawableSelector_lib_unfocused_activated) {
                unStateActivated = setSelectorDrawable(drawableArray, attr)
            }
        }
        return this
    }

    private fun setSelectorDrawable(typedArray: TypedArray, attr: Int): Drawable? {
        var color = 0
        var resDrawable: Drawable? = null
        try {
            color = typedArray.getColor(attr, 0)
            if (color == 0) {
                resDrawable = typedArray.getDrawable(attr)
            }
        } catch (e: Exception) {
            resDrawable = typedArray.getDrawable(attr)
        }
        if (resDrawable == null && color != 0) {
            resDrawable = if (shapeBuilder != null) {
                val tmpDrawable = shapeBuilder!!.buildNoRipple()
                tmpDrawable.setColor(color)
                tmpDrawable
            } else {
                ColorDrawable(color)
            }
        }
        return resDrawable
    }

    private fun putState(stateListDrawable: StateListDrawable, state: Drawable?, attr: Int) {
        if (state != null) {
            stateListDrawable.addState(intArrayOf(attr), state)
        }
    }
}
