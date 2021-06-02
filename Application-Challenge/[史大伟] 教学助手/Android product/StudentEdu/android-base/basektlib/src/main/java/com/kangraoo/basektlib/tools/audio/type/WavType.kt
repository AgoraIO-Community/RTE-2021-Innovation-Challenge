package com.kangraoo.basektlib.tools.audio.type

import com.kangraoo.basektlib.tools.UTime.currentTimeMillis
import com.kangraoo.basektlib.tools.audio.IAudioType
import com.kangraoo.basektlib.tools.log.ULog.e
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import java.io.*

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/10
 * desc :
 * version: 1.0
 */
class WavType : IAudioType {

    override fun toType(fileList: List<File>): File? {
        var totalSize = 0
        for (i in fileList.indices) {
            totalSize += fileList[i].length().toInt()
        }
        // 填入参数，比特率等等。这里用的是16位单声道 8000 hz
        val header = WaveHeader()
        // 长度字段 = 内容的大小（TOTAL_SIZE) +
        // 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = totalSize + (44 - 8)
        header.fmtHdrLeth = 16
        header.bitsPerSample = 16
        header.channels = 2
        header.formatTag = 0x0001
        header.samplesPerSec = 8000
        header.blockAlign = (header.channels * header.bitsPerSample / 8).toShort()
        header.avgBytesPerSec = header.blockAlign * header.samplesPerSec
        header.dataHdrLeth = totalSize
        var h: ByteArray? = null
        h = try {
            header.header
        } catch (e: IOException) {
            e(e, e.message)
            return null
        }
        if (h?.size != 44) { // WAV标准，头部应该是44字节,如果不是44个字节则不进行转换文件
            return null
        }

        val path = UStorage.getWritePath(currentTimeMillis().toString() + ".wav", StorageType.TYPE_AUDIO)
        return if (path != null) {
            // 合成所有的pcm文件的数据，写到目标文件
            val wavfile = AttachmentStore.create(path)
            FileOutputStream(wavfile).use {
                var buffer = ByteArray(1024 * 4)
                BufferedOutputStream(it).write(h, 0, h.size)

                for (j in fileList.indices) {

                    FileInputStream(fileList[j]).use { ins ->
                        var size = ins.read(buffer)
                        while (size != -1) {
                            it.write(buffer)
                            size = ins.read(buffer)
                        }
                    }
                }

                for (i in fileList.indices) {
                    val file = fileList[i]
                    if (file.exists()) {
                        file.delete()
                    }
                }
                wavfile
            }
        } else {
            null
        }
    }
}
