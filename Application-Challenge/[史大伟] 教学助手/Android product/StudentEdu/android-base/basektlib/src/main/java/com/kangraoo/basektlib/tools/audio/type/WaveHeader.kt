package com.kangraoo.basektlib.tools.audio.type

import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.jvm.Throws

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/09
 * desc :
 * version: 1.0
 */
class WaveHeader {
    val fileID = charArrayOf('R', 'I', 'F', 'F')
    @JvmField
    var fileLength = 0
    var wavTag = charArrayOf('W', 'A', 'V', 'E')
    var fmtHdrID = charArrayOf('f', 'm', 't', ' ')
    @JvmField
    var fmtHdrLeth = 0
    @JvmField
    var formatTag: Short = 0
    @JvmField
    var channels: Short = 0
    @JvmField
    var samplesPerSec = 0
    @JvmField
    var avgBytesPerSec = 0
    @JvmField
    var blockAlign: Short = 0
    @JvmField
    var bitsPerSample: Short = 0
    var dataHdrID = charArrayOf('d', 'a', 't', 'a')
    @JvmField
    var dataHdrLeth = 0

    @get:Throws(IOException::class)
    val header: ByteArray
        get() {
            val bos = ByteArrayOutputStream()
            writeChar(bos, fileID)
            writeInt(bos, fileLength)
            writeChar(bos, wavTag)
            writeChar(bos, fmtHdrID)
            writeInt(bos, fmtHdrLeth)
            writeShort(bos, formatTag.toInt())
            writeShort(bos, channels.toInt())
            writeInt(bos, samplesPerSec)
            writeInt(bos, avgBytesPerSec)
            writeShort(bos, blockAlign.toInt())
            writeShort(bos, bitsPerSample.toInt())
            writeChar(bos, dataHdrID)
            writeInt(bos, dataHdrLeth)
            bos.flush()
            val r = bos.toByteArray()
            bos.close()
            return r
        }

    @Throws(IOException::class)
    private fun writeShort(bos: ByteArrayOutputStream, s: Int) {
        val mybyte = ByteArray(2)
        mybyte[1] = (s shl 16 shr 24).toByte()
        mybyte[0] = (s shl 24 shr 24).toByte()
        bos.write(mybyte)
    }

    @Throws(IOException::class)
    private fun writeInt(bos: ByteArrayOutputStream, n: Int) {
        val buf = ByteArray(4)
        buf[3] = (n shr 24).toByte()
        buf[2] = (n shl 8 shr 24).toByte()
        buf[1] = (n shl 16 shr 24).toByte()
        buf[0] = (n shl 24 shr 24).toByte()
        bos.write(buf)
    }

    private fun writeChar(bos: ByteArrayOutputStream, id: CharArray) {
        for (i in id.indices) {
            val c = id[i]
            bos.write(c.toInt())
        }
    }
}
