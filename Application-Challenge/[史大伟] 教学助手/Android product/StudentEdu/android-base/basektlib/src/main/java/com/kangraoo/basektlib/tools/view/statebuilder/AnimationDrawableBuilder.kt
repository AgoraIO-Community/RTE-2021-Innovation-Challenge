package com.kangraoo.basektlib.tools.view.statebuilder

import android.graphics.drawable.AnimationDrawable
import androidx.core.content.ContextCompat
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HAction

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/04
 * desc :
 * version: 1.0
 */
class AnimationDrawableBuilder : BaseBuilder<AnimationDrawableBuilder>() {
    private var autoStart = false
    private var drawableList: List<Int>? = null
    private var durationList: List<Int>? = null
    private var duration = 0
    private var oneShot = false
    fun setAutoStart(autoStart: Boolean): AnimationDrawableBuilder {
        this.autoStart = autoStart
        return this
    }

    fun setDrawableList(
        drawableList: List<Int>?,
        durationList: List<Int>?
    ): AnimationDrawableBuilder {
        this.drawableList = drawableList
        this.durationList = durationList
        return this
    }

    fun setDuration(duration: Int): AnimationDrawableBuilder {
        this.duration = duration
        return this
    }

    fun setOneShot(oneShot: Boolean): AnimationDrawableBuilder {
        this.oneShot = oneShot
        return this
    }

    fun build(): AnimationDrawable {
        val drawable = AnimationDrawable()
        drawable.isOneShot = oneShot
        for (i in drawableList!!.indices) {
            if (durationList != null) {
                duration = durationList!![i]
            }
            val itemDrawable =
                ContextCompat.getDrawable(SApplication.context(), drawableList!![i])
            if (itemDrawable != null) {
                drawable.addFrame(itemDrawable, duration)
            }
        }
        if (autoStart) {
            HAction.mainHandler.post(Runnable { drawable.start() })
        }
        return drawable
    }
}
