package com.kangraoo.basektlib.tools.view.statebuilder

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/03
 * desc :
 * version: 1.0
 */
open class BaseStateBuilder<T : BaseStateBuilder<T, M>, M> : BaseBuilder<T>() {
    @JvmField
    protected var statePressed: M? = null
    @JvmField
    protected var stateEnabled: M? = null
    @JvmField
    protected var stateFocused: M? = null
    @JvmField
    protected var stateCheckable: M? = null
    @JvmField
    protected var stateChecked: M? = null
    @JvmField
    protected var stateSelected: M? = null
    @JvmField
    protected var stateHovered: M? = null
    @JvmField
    protected var stateActivated: M? = null
    @JvmField
    protected var unStatePressed: M? = null
    @JvmField
    protected var unStateEnabled: M? = null
    @JvmField
    protected var unStateFocused: M? = null
    @JvmField
    protected var unStateCheckable: M? = null
    @JvmField
    protected var unStateChecked: M? = null
    @JvmField
    protected var unStateSelected: M? = null
    @JvmField
    protected var unStateHovered: M? = null
    @JvmField
    protected var unStateActivated: M? = null
    fun setStatePressed(statePressed: M): T {
        this.statePressed = statePressed
        return this as T
    }

    fun setStateEnabled(stateEnabled: M): T {
        this.stateEnabled = stateEnabled
        return this as T
    }

    fun setStateFocused(stateFocused: M): T {
        this.stateFocused = stateFocused
        return this as T
    }

    fun setStateCheckable(stateCheckable: M): T {
        this.stateCheckable = stateCheckable
        return this as T
    }

    fun setStateChecked(stateChecked: M): T {
        this.stateChecked = stateChecked
        return this as T
    }

    fun setStateSelected(stateSelected: M): T {
        this.stateSelected = stateSelected
        return this as T
    }

    fun setUnStatePressed(unStatePressed: M): T {
        this.unStatePressed = unStatePressed
        return this as T
    }

    fun setUnStateEnabled(unStateEnabled: M): T {
        this.unStateEnabled = unStateEnabled
        return this as T
    }

    fun setUnStateFocused(unStateFocused: M): T {
        this.unStateFocused = unStateFocused
        return this as T
    }

    fun setUnStateCheckable(unStateCheckable: M): T {
        this.unStateCheckable = unStateCheckable
        return this as T
    }

    fun setUnStateChecked(unStateChecked: M): T {
        this.unStateChecked = unStateChecked
        return this as T
    }

    fun setUnStateSelected(unStateSelected: M): T {
        this.unStateSelected = unStateSelected
        return this as T
    }

    fun setStateHovered(stateHovered: M): T {
        this.stateHovered = stateHovered
        return this as T
    }

    fun setStateActivated(stateActivated: M): T {
        this.stateActivated = stateActivated
        return this as T
    }

    fun setUnStateHovered(unStateHovered: M): T {
        this.unStateHovered = unStateHovered
        return this as T
    }

    fun setUnStateActivated(unStateActivated: M): T {
        this.unStateActivated = unStateActivated
        return this as T
    }
}
