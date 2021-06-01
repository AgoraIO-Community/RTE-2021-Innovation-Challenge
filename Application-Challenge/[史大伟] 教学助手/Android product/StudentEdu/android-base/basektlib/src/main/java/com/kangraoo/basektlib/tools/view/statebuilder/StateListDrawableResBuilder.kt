package com.kangraoo.basektlib.tools.view.statebuilder

import android.R
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.core.content.ContextCompat
import com.kangraoo.basektlib.app.SApplication

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/03
 * desc :
 * version: 1.0
 */
class StateListDrawableResBuilder : BaseStateBuilder<StateListDrawableResBuilder, Int>() {
    fun build(): Drawable? {
        val stateListDrawable = StateListDrawable()
        putState(stateListDrawable, stateCheckable, R.attr.state_checkable)
        putState(stateListDrawable, stateChecked, R.attr.state_checked)
        putState(stateListDrawable, stateEnabled, R.attr.state_enabled)
        putState(stateListDrawable, stateSelected, R.attr.state_selected)
        putState(stateListDrawable, statePressed, R.attr.state_pressed)
        putState(stateListDrawable, stateFocused, R.attr.state_focused)
        putState(stateListDrawable, stateHovered, R.attr.state_hovered)
        putState(stateListDrawable, stateActivated, R.attr.state_activated)
        putState(stateListDrawable, unStateCheckable, -R.attr.state_checkable)
        putState(stateListDrawable, unStateChecked, -R.attr.state_checked)
        putState(stateListDrawable, unStateEnabled, -R.attr.state_enabled)
        putState(stateListDrawable, unStateSelected, -R.attr.state_selected)
        putState(stateListDrawable, unStatePressed, -R.attr.state_pressed)
        putState(stateListDrawable, unStateFocused, -R.attr.state_focused)
        putState(stateListDrawable, unStateHovered, -R.attr.state_hovered)
        putState(stateListDrawable, unStateActivated, -R.attr.state_activated)
        return buildRipple(stateListDrawable)
    }

    private fun putState(stateListDrawable: StateListDrawable, state: Int?, attr: Int) {
        if (state != null) {
            stateListDrawable.addState(
                intArrayOf(attr),
                ContextCompat.getDrawable(SApplication.context(), state)
            )
        }
    }
}
