package com.kangraoo.basektlib.tools.audio

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.annotation.IntDef
import androidx.core.app.ActivityCompat
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.exception.LibException
import com.kangraoo.basektlib.exception.LibPermissionException
import com.kangraoo.basektlib.exception.LibStorageException
import com.kangraoo.basektlib.tools.UTime.currentTimeMillis
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage.storageCheck
import java.io.File
import java.util.*
import kotlin.jvm.Throws

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/10
 * desc :
 * version: 1.0
 */
abstract class BaseAudioRecorder : IAudioRecorder {

    @get:RecordStatusType
    @RecordStatusType
    @Volatile
    public var status = NO_RECORD_READY
        protected set
    var cha: Long = 0
    @JvmField
    var iAudio: IAudio? = null
    fun setiAudio(iAudio: IAudio?) {
        this.iAudio = iAudio
    }

    var iAudioType: IAudioType? = null
    fun setiAudioType(iAudioType: IAudioType?) {
        this.iAudioType = iAudioType
    }

    @Throws(LibPermissionException::class, LibStorageException::class)
    protected fun checkPremission() {
        storageCheck(StorageType.TYPE_AUDIO)
        if (ActivityCompat.checkSelfPermission(
                SApplication.instance(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw LibPermissionException(
                SApplication.instance().getString(R.string.libPleaseCheckRecordPermissions)
            )
        }
    }

    @Throws(LibPermissionException::class, LibStorageException::class, LibException::class)
    override fun startRecord() {
        checkPremission()
        if (status == NO_RECORD_READY) {
            throw LibException(
                SApplication.instance().getString(R.string.libRecordingNotInitialized)
            )
        }
        if (status == RECORD || status == NEXT_RECORD) {
            throw LibException(SApplication.instance().getString(R.string.libRecording))
        }
        if (status == PAUSE) {
            status = NEXT_RECORD
            if (iAudio != null) {
                iAudio!!.nextStart()
            }
            time = currentTimeMillis() - cha
            cha = 0
        } else {
            status = RECORD
            if (iAudio != null) {
                iAudio!!.start()
            }
            time = currentTimeMillis()
        }
    }

    @JvmField
    protected var files: MutableList<File> =
        ArrayList()

    @Throws(LibException::class)
    override fun stopRecord() {
        if (status == NO_RECORD_READY || status == READY) {
            throw LibException(
                SApplication.instance().getString(R.string.libRecordingNotStarted)
            )
        }
        if (status == STOP) {
            throw LibException(SApplication.instance().getString(R.string.libRecordingStopped))
        }
        status = STOP
        stopAction()
        if (files.size > 0) {
            if (iAudioType != null) {
                if (iAudio != null) {
                    iAudio!!.stop(iAudioType!!.toType(files))
                }
            } else {
                if (iAudio != null) {
                    iAudio!!.stop(null)
                }
            }
            files.clear()
        } else {
            if (iAudio != null) {
                iAudio!!.stop(null)
            }
        }
        cancle()
    }

    protected abstract fun stopAction()
    @JvmField
    protected var time: Long = 0

    @Throws(LibException::class)
    override fun pauseRecord() {
        if (status == NO_RECORD_READY || status == READY) {
            throw LibException(
                SApplication.instance().getString(R.string.libRecordingNotStarted)
            )
        }
        if (status != RECORD && status != NEXT_RECORD) {
            throw LibException(SApplication.instance().getString(R.string.libNotRecorded))
        }
        status = PAUSE
        cha = currentTimeMillis() - time
        if (iAudio != null) {
            iAudio!!.pause()
        }
    }

    override fun cancle() {
        status = NO_RECORD_READY
    }

    protected val audioTime: Long
        protected get() = currentTimeMillis() - time
}
const val AUDIO_INPUT = MediaRecorder.AudioSource.MIC
const val NO_RECORD_READY = 0
const val READY = 1
const val RECORD = 2
const val PAUSE = 3
const val STOP = 4
const val NEXT_RECORD = 5

@IntDef(
    NO_RECORD_READY,
    READY,
    RECORD,
    PAUSE,
    STOP,
    NEXT_RECORD
)
@Retention(AnnotationRetention.SOURCE)
annotation class RecordStatusType
