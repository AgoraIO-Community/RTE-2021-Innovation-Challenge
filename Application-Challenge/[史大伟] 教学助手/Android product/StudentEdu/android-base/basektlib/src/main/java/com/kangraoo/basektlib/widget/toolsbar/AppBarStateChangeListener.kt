package com.kangraoo.basektlib.widget.toolsbar

import androidx.annotation.IntDef
import com.google.android.material.appbar.AppBarLayout
import com.kangraoo.basektlib.tools.audio.MUSIC_LODING
import com.kangraoo.basektlib.tools.audio.MUSIC_LODING_ERROR
import com.kangraoo.basektlib.tools.audio.MUSIC_LODING_FINISH
import com.kangraoo.basektlib.tools.audio.NO_READY
import kotlin.math.abs

/**
 * @author shidawei
 * 创建日期：2021/4/1
 * 描述：
 */

const val EXPANDED = 1
const val COLLAPSED = 2
const val IDLE = 3

@IntDef(
    EXPANDED,
    COLLAPSED,
    IDLE
)
@Retention(AnnotationRetention.SOURCE)
annotation class State

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    private var mCurrentState = IDLE
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        when {
            verticalOffset == 0 -> {
                if (mCurrentState != EXPANDED) {
                    onStateChanged(appBarLayout, EXPANDED,verticalOffset)
                }
                mCurrentState = EXPANDED
            }
            abs(verticalOffset) >= appBarLayout!!.totalScrollRange -> {
                if (mCurrentState != COLLAPSED) {
                    onStateChanged(appBarLayout, COLLAPSED,verticalOffset)
                }
                mCurrentState = COLLAPSED
            }
            else -> {
                if (mCurrentState != IDLE) {
                    onStateChanged(appBarLayout, IDLE,verticalOffset)
                }
                mCurrentState = IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, @State state: Int, verticalOffset: Int)
}