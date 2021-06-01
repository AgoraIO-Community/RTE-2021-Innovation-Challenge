package com.kangraoo.basektlib.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

sealed class Animation {

    abstract fun getAnimators(view: View): Array<Animator>

    class AlphaInAnimation(private val form: Float) : Animation() {
        constructor() : this(0f)
        override fun getAnimators(view: View): Array<Animator> {
            return arrayOf<Animator>(ObjectAnimator.ofFloat(view, "alpha", form, 1f))
        }
    }

    class ScaleInAnimation(private val form: Float) : Animation() {
        constructor() : this(.5f)
        override fun getAnimators(view: View): Array<Animator> {
            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", form, 1f)
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", form, 1f)
            return arrayOf<Animator>(scaleX, scaleY)
        }
    }

    class SlideInBottomAnimation : Animation() {
        override fun getAnimators(view: View): Array<Animator> {
            return arrayOf(
                    ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f)
            )
        }
    }

    class SlideInLeftAnimation : Animation() {
        override fun getAnimators(view: View): Array<Animator> {
            return arrayOf(
                    ObjectAnimator.ofFloat(view, "translationX", -view.rootView.width.toFloat(), 0f)
            )
        }
    }

    class SlideInRightAnimation : Animation() {
        override fun getAnimators(view: View): Array<Animator> {
            return arrayOf(
                    ObjectAnimator.ofFloat(view, "translationX", view.rootView.width.toFloat(), 0f)
            )
        }
    }
}
