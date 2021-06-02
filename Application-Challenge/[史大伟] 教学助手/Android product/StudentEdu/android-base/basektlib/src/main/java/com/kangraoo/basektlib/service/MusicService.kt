package com.kangraoo.basektlib.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.danikula.videocache.HttpProxyCacheServer
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HString.isNet
import com.kangraoo.basektlib.tools.audio.AudioPlayer
import com.kangraoo.basektlib.tools.audio.IAudioPlayer
import com.kangraoo.basektlib.tools.listener.LibPhoneCallListener
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import java.io.File

class MusicService : Service() {
    var musicBinder: MusicBinder? = null
    companion object {
        val proxy: HttpProxyCacheServer by lazy {
            // 512MB
            HttpProxyCacheServer.Builder(SApplication.context()).maxCacheSize(UStorage.M * 512).cacheDirectory(
                File(UStorage.getDirectoryByDirType(StorageType.TYPE_AUDIO))
            ).build()
        }
    }

    private fun telephony() {
        // 获得相应的系统服务
        (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?)?.listen(libPhoneCallListener, PhoneStateListener.LISTEN_CALL_STATE)
    }
//    private val MEDIA_SESSION_ACTIONS = (PlaybackStateCompat.ACTION_PLAY
//            or PlaybackStateCompat.ACTION_PAUSE
//            or PlaybackStateCompat.ACTION_PLAY_PAUSE
//            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
//            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
//            or PlaybackStateCompat.ACTION_STOP
//            or PlaybackStateCompat.ACTION_SEEK_TO)
//    var mMediaSession: MediaSessionCompat? = null

    var audioPlayer: AudioPlayer = AudioPlayer.instance
    override fun onCreate() {
        super.onCreate()
        telephony()
        audioPlayer.setiAudioPlayer(object : IAudioPlayer {
            override fun play() {
                ULog.i("play")
                musicBinder?.iAudioPlayer?.play()
            }

            override fun pause() {
                ULog.i("pause")
//                mMediaSession!!.setPlaybackState(
//                    PlaybackStateCompat.Builder()
//                        .setActions(MEDIA_SESSION_ACTIONS)
//                        .setState(PlaybackStateCompat.STATE_PAUSED, musicBinder!!.getCurrenPostion().toLong(), 1f)
//                        .build()
//                )
                musicBinder?.iAudioPlayer?.pause()
            }

            override fun stop() {
                ULog.i("stop")
                musicBinder?.iAudioPlayer?.stop()
            }

            override fun finish() {
                ULog.i("finish")
                musicBinder?.iAudioPlayer?.finish()
            }

            override fun error() {
                ULog.i("error")
                musicBinder?.iAudioPlayer?.error()
            }

            override fun mediaLoadFinish(duration: Int) {
                ULog.i("duration", duration)
                musicBinder?.iAudioPlayer?.mediaLoadFinish(duration)
            }

            override fun playPosition(position: Int) {
                ULog.i("position", position)
//                mMediaSession!!.setPlaybackState(
//                    PlaybackStateCompat.Builder()
//                        .setActions(MEDIA_SESSION_ACTIONS)
//                        .setState(PlaybackStateCompat.STATE_PLAYING, position.toLong(), 1f)
//                        .build()
//                )
                musicBinder?.iAudioPlayer?.playPosition(position)
            }
        })

//        mMediaSession = MediaSessionCompat(this, "MusicService")
//        mMediaSession!!.setFlags(
//            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
//                    or MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
//        )
//        mMediaSession!!.setCallback(callback)
//        mMediaSession!!.isActive = true
    }

    private val callback: MediaSessionCompat.Callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
//            val metaData = MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "123")
//                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "123")
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "123")
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, "123")
//                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 10000L)
// //                .putBitmap(
// //                    MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
// //                    CoverLoader.getInstance().loadThumbnail(music)
// //                )
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
// //                metaData.putLong(
// //                    MediaMetadataCompat.METADATA_KEY_NUM_TRACKS,
// //                    AppCache.getMusicList().size()
// //                )
//            }
//            mMediaSession!!.setMetadata(metaData.build())
            musicBinder!!.play()
        }

        override fun onPause() {
            musicBinder!!.pause()
        }

        override fun onSkipToNext() {
//            mPlayService.next()
        }

        override fun onSkipToPrevious() {
//            mPlayService.prev()
        }

        override fun onStop() {
            musicBinder!!.cancle()
        }

        override fun onSeekTo(pos: Long) {
            musicBinder!!.seekTo(pos.toInt())
        }
    }

    private var libPhoneCallListener: LibPhoneCallListener = LibPhoneCallListener(object : LibPhoneCallListener.CallListener {
        override fun onCallFinish() {
        }

        override fun onCallRinging() {
        }

        override fun onCallStart() {
            audioPlayer.pause()
        }
    })

    override fun onBind(intent: Intent): IBinder {
        musicBinder = MusicBinder(audioPlayer)
        return musicBinder!!
    }

    override fun onDestroy() {
        audioPlayer.setiAudioPlayer(null)
        audioPlayer.cancle()
        val tm =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        tm?.listen(libPhoneCallListener, PhoneStateListener.LISTEN_NONE)
//        mMediaSession?.let {
//            it.setCallback(null)
//            it.setActive(false)
//            it.release()
//        }

        super.onDestroy()
    }

    class MusicBinder(val audioPlayer: AudioPlayer) : Binder() {
        var iAudioPlayer: IAudioPlayer? = null

        // 加载音乐并缓存
        fun loadingMusic(pathUrl: String) {
            val url = if (isNet(pathUrl)) proxy.getProxyUrl(pathUrl) else pathUrl
            audioPlayer.loadMedia(url)
        }

        /**
         * 暂停
         */
        fun pause() {
            audioPlayer.pause()
        }

        /**
         * 播放
         */
        fun play() {
            audioPlayer.play()
        }

        // 判断是否处于播放状态
        fun isPlaying() = audioPlayer.isPlaying
        // 返回歌曲的长度
        fun getDuration() = audioPlayer.getDuration()
        // 返回歌曲目前的进度
        fun getCurrenPostion() = audioPlayer.getCurrenPostion()
        // 设置歌曲播放的进度
        fun seekTo(mesc: Int) {
            audioPlayer.seekTo(mesc)
        }

        fun cancle() {
            audioPlayer.cancle()
        }

//        fun initMusic(title:String,artist:String,album:String){
//            val metaData = MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.getTitle())
//                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.getArtist())
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.getAlbum())
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, music.getArtist())
//                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, music.getDuration())
//                .putBitmap(
//                    MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
//                    CoverLoader.getInstance().loadThumbnail(music)
//                )
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                metaData.putLong(
//                    MediaMetadataCompat.METADATA_KEY_NUM_TRACKS,
//                    AppCache.getMusicList().size()
//                )
//            }
//
//            mMediaSession.setMetadata(metaData.build())
//        }
    }
}
