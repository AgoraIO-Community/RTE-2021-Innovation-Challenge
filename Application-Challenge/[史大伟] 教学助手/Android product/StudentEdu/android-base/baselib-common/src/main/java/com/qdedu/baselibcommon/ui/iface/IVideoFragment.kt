package com.qdedu.baselibcommon.ui.iface

import android.view.View
import android.view.animation.Animation
import android.widget.SeekBar
import androidx.fragment.app.Fragment

interface IVideoListener{
    fun onTopVisibilityChanged(
        isVisible: Boolean
    )
    fun onProgress(progress:Int, position:Long, duration:Long)

    fun onStopTrackingTouch(seekBar: SeekBar?):SeekBar?
}

interface IVideoFragment {

    fun setUp(url: String?,title:String?)

    fun loadPoster(poster:String?)

    fun startVideo()

    fun getFragment():Fragment

    fun addViewTopRight(view:View)

    fun resumeVideo()

    fun pauseVideo()

    fun totalTime(): String

    fun currTime(): String

    fun setVideoTopVisableListener(videoListener: IVideoListener)
}