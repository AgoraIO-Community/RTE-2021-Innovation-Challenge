package com.kangraoo.basektlib.tools.audio

import java.io.File

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/10
 * desc :
 * version: 1.0
 */
interface IAudio {
    fun stop(file: File?)
    fun pause()
    fun start()
    fun nextStart()
    fun onUpdate(db: Int, time: Long)
}
