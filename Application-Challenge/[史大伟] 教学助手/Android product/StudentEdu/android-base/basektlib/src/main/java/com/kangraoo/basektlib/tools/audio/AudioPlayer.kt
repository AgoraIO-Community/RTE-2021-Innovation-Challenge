package com.kangraoo.basektlib.tools.audio

import android.media.MediaPlayer
import android.text.TextUtils
import androidx.annotation.IntDef
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.exception.LibException
import com.kangraoo.basektlib.tools.HString.isNet
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.task.HIGH
import com.kangraoo.basektlib.tools.task.TaskManager
import java.io.IOException
import kotlin.jvm.Throws

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/10
 * desc :
 * version: 1.0
 */
class AudioPlayer private constructor() {

    companion object {

        private const val TAG = "AudioPlayer"
        val instance: AudioPlayer by lazy {
            AudioPlayer()
        }
    }

    @StatusType
    @Volatile
    private var status = NO_READY

    private var mMediaPlayer: MediaPlayer? = null

    var iAudioPlayer: IAudioPlayer? = null
    fun setiAudioPlayer(iAudioPlayer: IAudioPlayer?) {
        this.iAudioPlayer = iAudioPlayer
    }

    private fun init() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            // 注册，播放完成后的监听
            mMediaPlayer!!.setOnCompletionListener {
                if (iAudioPlayer != null) {
                    iAudioPlayer!!.finish()
                }
            }
            // 监听媒体流是否装载完成
            mMediaPlayer!!.setOnPreparedListener {
                status =
                    MUSIC_LODING_FINISH
                val duration = mMediaPlayer!!.duration // 获取总时长
                if (iAudioPlayer != null) {
                    iAudioPlayer!!.mediaLoadFinish(duration)
                }
            }
            /**
             * @date: 2019/6/21 0021
             * @author: gaoxiaoxiong
             * @description:监听媒体错误信息
             */
            mMediaPlayer!!.setOnErrorListener { mp, what, extra ->
                status =
                    MUSIC_LODING_ERROR
                if (iAudioPlayer != null) {
                    iAudioPlayer!!.error()
                }
                ULog.d(
                    TAG,
                    "OnError - Error code: ",
                    what,
                    " Extra code: ",
                    extra
                )
                when (what) {
                    -1004 -> ULog.d(
                        TAG,
                        "MEDIA_ERROR_IO"
                    )
                    -1007 -> ULog.d(
                        TAG,
                        "MEDIA_ERROR_MALFORMED"
                    )
                    200 -> ULog.d(
                        TAG,
                        "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK"
                    )
                    100 -> ULog.d(
                        TAG,
                        "MEDIA_ERROR_SERVER_DIED"
                    )
                    -110 -> ULog.d(
                        TAG,
                        "MEDIA_ERROR_TIMED_OUT"
                    )
                    1 -> ULog.d(
                        TAG,
                        "MEDIA_ERROR_UNKNOWN"
                    )
                    -1010 -> ULog.d(
                        TAG,
                        "MEDIA_ERROR_UNSUPPORTED"
                    )
                }
                when (extra) {
                    800 -> ULog.d(
                        TAG,
                        "MEDIA_INFO_BAD_INTERLEAVING"
                    )
                    702 -> ULog.d(
                        TAG,
                        "MEDIA_INFO_BUFFERING_END"
                    )
                    701 -> ULog.d(
                        TAG,
                        "MEDIA_INFO_METADATA_UPDATE"
                    )
                    802 -> ULog.d(
                        TAG,
                        "MEDIA_INFO_METADATA_UPDATE"
                    )
                    801 -> ULog.d(
                        TAG,
                        "MEDIA_INFO_NOT_SEEKABLE"
                    )
                    1 -> ULog.d(
                        TAG,
                        "MEDIA_INFO_UNKNOWN"
                    )
                    3 -> ULog.d(
                        TAG,
                        "MEDIA_INFO_VIDEO_RENDERING_START"
                    )
                    700 -> ULog.d(
                        TAG,
                        "MEDIA_INFO_VIDEO_TRACK_LAGGING"
                    )
                }
                false
            }
        }
    }

    private var musiUrl: String? = null // 音乐地址，可以是本地的音乐，可以是网络的音乐

    /**
     * @date: 2019/6/24 0024
     * @author: gaoxiaoxiong
     * @description:播放开始
     */
    @Throws(LibException::class)
    fun play() {
        if (mMediaPlayer == null) {
            throw LibException(
                SApplication.instance().getString(R.string.libUninitializedPlaybackTool)
            )
        }
        if (mMediaPlayer!!.isPlaying) {
            return
        }
        when (status) {
            MUSIC_LODING_FINISH -> {
                mMediaPlayer!!.start()
                iAudioPlayer?.play()
                TaskManager.taskExecutor.execute(Runnable { // 开启线程，获取当前播放的进度
                    while (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
                        try {
                            Thread.sleep(100)
                        } catch (e: InterruptedException) {
                            ULog.e(e, e.message)
                        }
                        synchronized(this) {
                            if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
                                val currentPosition = mMediaPlayer!!.currentPosition
                                if (iAudioPlayer != null) {
                                    iAudioPlayer!!.playPosition(currentPosition)
                                }
                            }
                        }
                    }
                }, HIGH)
            }
            MUSIC_LODING -> throw LibException(
                SApplication.instance().getString(R.string.libMediaStreamLoading)
            )
            MUSIC_LODING_ERROR -> throw LibException(
                SApplication.instance().getString(R.string.libMediaStreamLoadingFailure)
            )
        }
    }

    /**
     * @date: 2019/6/24 0024
     * @author: gaoxiaoxiong
     * @description: 加载媒体资源
     */
    @Throws(LibException::class)
    fun loadMedia(musiUrl: String?) {
        if (TextUtils.isEmpty(musiUrl)) {
            ULog.i(TAG, "url is null")
            throw NullPointerException("url is null")
        }
//        if (status == MUSIC_LODING) {
//            throw LibException(
//                SApplication.instance().getString(R.string.libMediaStreamLoading)
//            )
//        }
        status = MUSIC_LODING
        this.musiUrl = musiUrl
        init()
        try {
            mMediaPlayer!!.reset() // 防止再次添加进来出现崩溃信息
            mMediaPlayer!!.setDataSource(musiUrl)
            if (isNet(musiUrl!!)) {
                mMediaPlayer!!.prepareAsync()
            } else {
                mMediaPlayer!!.prepare()
            }
        } catch (e: IOException) {
            ULog.e(e, e.message)
        }
    }

    /**
     * @date: 2019/6/24 0024
     * @author: gaoxiaoxiong
     * @description:判断是否正在播放
     */
    val isPlaying: Boolean
        get() = if (mMediaPlayer != null) {
            mMediaPlayer!!.isPlaying
        } else false


    @Synchronized
    @Throws(LibException::class)
    fun reset() {
        loadMedia(musiUrl)
    }

    @Synchronized
    @Throws(LibException::class)
    fun pause() {
        if (mMediaPlayer == null) {
            throw LibException(
                SApplication.instance().getString(R.string.libUninitializedPlaybackTool)
            )
        }
        if (!mMediaPlayer!!.isPlaying) {
            throw LibException(SApplication.instance().getString(R.string.libNoMusicPlayed))
        }
        mMediaPlayer!!.pause()
        iAudioPlayer?.pause()
    }

    @Synchronized
    fun cancle() {
        iAudioPlayer?.stop()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        if (iAudioPlayer != null) {
            iAudioPlayer!!.playPosition(0)
        }
    }

    @Throws(LibException::class)
    fun seekTo(position: Int) {
        if (mMediaPlayer == null) {
            if (iAudioPlayer != null) {
                iAudioPlayer!!.playPosition(0)
            }
            throw LibException(
                SApplication.instance().getString(R.string.libUninitializedPlaybackTool)
            )
        }
        if (mMediaPlayer != null) {
            mMediaPlayer!!.seekTo(position)
        }
    }

    fun getDuration() = if (mMediaPlayer != null) {
        mMediaPlayer!!.duration
    } else {
        0
    }

    fun getCurrenPostion() = if (mMediaPlayer != null) {
        mMediaPlayer!!.currentPosition
    } else {
        0
    }
}

const val MUSIC_LODING = 4 // 媒体流加载中
const val MUSIC_LODING_FINISH = 5 // 媒体流装载完成
const val MUSIC_LODING_ERROR = 8 // 媒体流装载失败
const val NO_READY = 0
@IntDef(
    MUSIC_LODING,
    MUSIC_LODING_FINISH,
    MUSIC_LODING_ERROR,
    NO_READY
)

@Retention(AnnotationRetention.SOURCE)
annotation class StatusType
