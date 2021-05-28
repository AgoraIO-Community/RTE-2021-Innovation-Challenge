package com.kangraoo.basektlib.tools.audio

import android.media.MediaRecorder
import android.os.Build
import com.kangraoo.basektlib.exception.LibException
import com.kangraoo.basektlib.exception.LibPermissionException
import com.kangraoo.basektlib.exception.LibStorageException
import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import com.kangraoo.basektlib.tools.task.HIGH
import com.kangraoo.basektlib.tools.task.TaskManager
import java.io.File
import java.io.IOException
import kotlin.math.log10

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/10
 * desc :
 * version: 1.0
 */
private const val AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB
private const val AUDIO_OUTPUT_FORMAT = MediaRecorder.OutputFormat.AMR_NB

class AmrAudioRecorder private constructor() : BaseAudioRecorder() {

    companion object {
        val instance: AmrAudioRecorder by lazy {
            AmrAudioRecorder()
        }
    }

    var mMediaRecorder: MediaRecorder? = null
    var currentFileName: String? = null
    override fun initDefault() {
        init(
            AUDIO_INPUT, AUDIO_ENCODER,
            AUDIO_OUTPUT_FORMAT
        )
    }

    private var audioSource = 0
    private var audioEncoder = 0
    private var outputFormat = 0
    fun init(audioSource: Int, audioEncoder: Int, outputFormat: Int) {
        if (status == NO_READY) {
            this.audioSource = audioSource
            this.audioEncoder = audioEncoder
            this.outputFormat = outputFormat
            reInit(audioSource, audioEncoder, outputFormat)
            currentFileName = java.lang.String.valueOf(UTime.currentTimeMillis())
            time = 0
            status = READY
        }
    }

    private fun reInit(audioSource: Int, audioEncoder: Int, outputFormat: Int) {
        mMediaRecorder = MediaRecorder()
        mMediaRecorder!!.setAudioSource(audioSource) // 设置麦克风
        /*
         * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
         * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
         */mMediaRecorder!!.setOutputFormat(outputFormat)
        /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */mMediaRecorder!!.setAudioEncoder(
            audioEncoder
        )
    }

    @Throws(LibPermissionException::class, LibStorageException::class, LibException::class)
    override fun startRecord() {
        super.startRecord()
        val name: String?
        if (status == NEXT_RECORD) {
            reInit(audioSource, audioEncoder, outputFormat)
            currentFileName += files.size
            name = currentFileName
        } else {
            name = currentFileName
        }
        var file: File? = null
        var path = UStorage.getWritePath("$name.amr", StorageType.TYPE_AUDIO)
        if (path != null) {
            file = AttachmentStore.create(path)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mMediaRecorder!!.setOutputFile(file)
        } else {
            mMediaRecorder!!.setOutputFile(file!!.path)
        }
        try {
            mMediaRecorder!!.prepare()
        } catch (e: IOException) {
            ULog.e(e, e.message)
        }
        mMediaRecorder!!.start()
        files.add(file!!)
        TaskManager.taskExecutor.execute(Runnable {
            while (status == RECORD || status == NEXT_RECORD) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    ULog.e(e, e.message)
                }
                val ratio = mMediaRecorder!!.maxAmplitude / 150.toDouble()
                if (iAudio != null) {
                    var volume = 0 // 分贝
                    if (ratio > 1) {
                        volume = (20 * log10(ratio)).toInt()
                    }
                    iAudio!!.onUpdate(volume, audioTime)
                }
            }
        }, HIGH)
    }

    @Throws(LibException::class)
    override fun pauseRecord() {
        super.pauseRecord()
        if (mMediaRecorder != null) {
            mMediaRecorder = try {
                mMediaRecorder!!.stop()
                mMediaRecorder!!.release()
                null
            } catch (e: IllegalStateException) {
                ULog.e(e)
                mMediaRecorder!!.reset()
                mMediaRecorder!!.release()
                null
            }
        }
    }

    override fun cancle() {
        super.cancle()
        if (mMediaRecorder != null) {
            mMediaRecorder = try {
                mMediaRecorder!!.stop()
                mMediaRecorder!!.release()
                null
            } catch (e: IllegalStateException) {
                ULog.e(e)
                mMediaRecorder!!.reset()
                mMediaRecorder!!.release()
                null
            }
        }
    }

    override fun stopAction() {
        if (mMediaRecorder != null) {
            mMediaRecorder = try {
                mMediaRecorder!!.stop()
                mMediaRecorder!!.release()
                null
            } catch (e: IllegalStateException) {
                ULog.e(e)
                mMediaRecorder!!.reset()
                mMediaRecorder!!.release()
                null
            }
        }
    }
}
