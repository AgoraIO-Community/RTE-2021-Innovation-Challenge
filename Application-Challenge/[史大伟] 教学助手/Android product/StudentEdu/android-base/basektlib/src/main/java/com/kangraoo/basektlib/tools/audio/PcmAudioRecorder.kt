package com.kangraoo.basektlib.tools.audio

import android.media.AudioFormat
import android.media.AudioRecord
import com.kangraoo.basektlib.exception.LibException
import com.kangraoo.basektlib.exception.LibPermissionException
import com.kangraoo.basektlib.exception.LibStorageException
import com.kangraoo.basektlib.tools.UTime.currentTimeMillis
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import com.kangraoo.basektlib.tools.task.HIGH
import com.kangraoo.basektlib.tools.task.TaskManager
import java.io.File
import java.io.FileOutputStream
import kotlin.jvm.Throws

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/09
 * desc :
 * version: 1.0
 */
/**
 * 基本配置
 */
// 采样率
// 44100是目前的标准，但是某些设备仍然支持22050，16000，11025
// 采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
private const val AUDIO_SAMPLE_RATE = 16000

// 音频通道 单声道
private const val AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO

// 音频格式：PCM编码
private const val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT
private const val MAX_VOLUME = 2000
class PcmAudioRecorder private constructor() : BaseAudioRecorder() {

    companion object {
        val instance: PcmAudioRecorder by lazy {
            PcmAudioRecorder()
        }
    }

    // 录音对象
    private var mAudioRecord: AudioRecord? = null

    // 缓冲区大小：缓冲区字节大小
    private var bufferSizeInBytes = 0
    override fun initDefault() {
        init(
            AUDIO_INPUT, AUDIO_SAMPLE_RATE,
            AUDIO_CHANNEL, AUDIO_ENCODING
        )
    }

    var currentFileName: String? = null
    fun init(
        audioSource: Int,
        sampleRateInHz: Int,
        channelConfig: Int,
        audioFormat: Int
    ) {
        if (status == NO_RECORD_READY) {
            bufferSizeInBytes = AudioRecord.getMinBufferSize(
                sampleRateInHz,
                channelConfig, audioFormat
            )
            mAudioRecord = AudioRecord(
                audioSource,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                bufferSizeInBytes
            )
            currentFileName = currentTimeMillis()
                .toString() + "_" + AUDIO_SAMPLE_RATE
            time = 0
            status = READY
        }
    }

    @Volatile
    var audiodata: ByteArray? = null

    @Volatile
    var readsize = 0

    @Throws(LibPermissionException::class, LibStorageException::class, LibException::class)
    override fun startRecord() {
        super.startRecord()
        mAudioRecord!!.startRecording()
        TaskManager.taskExecutor.execute(Runnable {
            // new一个byte数组用来存一些字节数据，大小为缓冲区大小
            audiodata = ByteArray(bufferSizeInBytes)
            if (status == NEXT_RECORD) {
                currentFileName += files.size
            }
            val path = UStorage.getWritePath("$currentFileName.pcm", StorageType.TYPE_AUDIO)
            if (path != null) {
                val pcmfile: File = AttachmentStore.create(path)
                FileOutputStream(pcmfile).use {
                    while (status == RECORD || status == NEXT_RECORD) {
                        readsize = mAudioRecord!!.read(audiodata!!, 0, bufferSizeInBytes)
                        it.write(audiodata)
                    }
                    audiodata = null
                    readsize = 0
                    files.add(pcmfile)
                }
            }
        }, HIGH)
        TaskManager.taskExecutor.execute(Runnable {
            while (status == RECORD || status == NEXT_RECORD) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    ULog.e(e, e.message)
                }
                if (audiodata != null) {
                    val volume = calculateRealVolume(audiodata!!, readsize)
                    if (iAudio != null) {
                        iAudio!!.onUpdate(volume, audioTime)
                    }
                }
            }
        }, HIGH)
    }

    override fun stopAction() {
        mAudioRecord!!.stop()
    }

    @Throws(LibException::class)
    override fun pauseRecord() {
        super.pauseRecord()
        mAudioRecord!!.stop()
    }

    override fun cancle() {
        super.cancle()
        if (mAudioRecord != null) {
            mAudioRecord!!.release()
            mAudioRecord = null
        }
    }

    // 计算音量大小
    private fun calculateRealVolume(buffer: ByteArray, readSize: Int): Int {
        var sum = 0.0
        var volume = 0
        for (i in 0 until readSize) {
            sum += buffer[i] * buffer[i].toDouble()
        }
        if (readSize > 0) {
            val amplitude = sum / readSize
            volume = Math.sqrt(amplitude).toInt()
        }
        return if (volume >= MAX_VOLUME) {
            MAX_VOLUME
        } else {
            volume
        }
    }
}
