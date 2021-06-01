package com.kangraoo.basektlib.tools.audio

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/11
 * desc :
 * version: 1.0
 */
interface IAudioPlayer {
    fun play() // 开始播放
    fun pause()
    fun stop()
    fun finish() // 完成
    fun error() // 错误
    fun mediaLoadFinish(duration: Int) // 完成媒体流的装载
    fun playPosition(position: Int)
}
