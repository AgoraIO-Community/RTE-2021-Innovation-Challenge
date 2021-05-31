package com.kangraoo.basektlib.tools.audio

import com.kangraoo.basektlib.exception.LibException
import com.kangraoo.basektlib.exception.LibPermissionException
import com.kangraoo.basektlib.exception.LibStorageException
import kotlin.jvm.Throws

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/10
 * desc :
 * version: 1.0
 */
interface IAudioRecorder {
    fun initDefault()

    @Throws(LibPermissionException::class, LibStorageException::class, LibException::class)
    fun startRecord()

    @Throws(LibException::class)
    fun stopRecord()

    @Throws(LibException::class)
    fun pauseRecord()
    fun cancle()
}
