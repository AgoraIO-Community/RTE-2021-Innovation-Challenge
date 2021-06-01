package com.kangraoo.basektlib.tools.store.filestorage

import androidx.annotation.StringDef
import com.kangraoo.basektlib.tools.store.filestorage.UStorage.THRESHOLD_MIN_SPCAE
enum class StorageType constructor(@IDirectoryName val storageDirectoryName: String, val storageMinSize: Long = THRESHOLD_MIN_SPCAE) {
    TYPE_LOG(LOG_DIRECTORY_NAME),
    TYPE_CACHE(CACHE_DIRECTORY_NAME),
    TYPE_TEMP(TEMP_DIRECTORY_NAME),
    TYPE_FILE(FILE_DIRECTORY_NAME),
    TYPE_AUDIO(AUDIO_DIRECTORY_NAME),
    TYPE_IMAGE(IMAGE_DIRECTORY_NAME),
    TYPE_VIDEO(VIDEO_DIRECTORY_NAME),
    TYPE_THUMB_IMAGE(THUMB_DIRECTORY_NAME),
    TYPE_THUMB_VIDEO(THUMB_DIRECTORY_NAME)
}

const val AUDIO_DIRECTORY_NAME = "audio/"
const val DATA_DIRECTORY_NAME = "data/"
const val FILE_DIRECTORY_NAME = "file/"
const val LOG_DIRECTORY_NAME = "log/"
const val CACHE_DIRECTORY_NAME = "cache/"
const val TEMP_DIRECTORY_NAME = "temp/"
const val IMAGE_DIRECTORY_NAME = "image/"
const val THUMB_DIRECTORY_NAME = "thumb/"
const val VIDEO_DIRECTORY_NAME = "video/"

@StringDef(
    AUDIO_DIRECTORY_NAME,
    DATA_DIRECTORY_NAME,
    FILE_DIRECTORY_NAME,
    LOG_DIRECTORY_NAME,
    CACHE_DIRECTORY_NAME,
    TEMP_DIRECTORY_NAME,
    IMAGE_DIRECTORY_NAME,
    THUMB_DIRECTORY_NAME,
    VIDEO_DIRECTORY_NAME
)
@Retention(AnnotationRetention.SOURCE)
annotation class IDirectoryName
