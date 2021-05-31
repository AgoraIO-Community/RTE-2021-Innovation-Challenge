package com.dong.circlelive.rtc

import android.graphics.Matrix
import android.graphics.Point
import android.opengl.GLES20
import io.agora.rtc.gl.GlUtil
import io.agora.rtc.gl.RendererCommon
import io.agora.rtc.gl.RgbaBuffer
import io.agora.rtc.gl.VideoFrame
import io.agora.rtc.gl.VideoFrame.I420Buffer
import io.agora.rtc.gl.VideoFrame.TextureBuffer
import java.nio.ByteBuffer
import kotlin.math.hypot
import kotlin.math.roundToInt

class VideoFrameDrawer {
    private val dstPoints = FloatArray(6)
    private val renderSize = Point()
    private var renderWidth = 0
    private var renderHeight = 0
    private val yuvUploader = YuvUploader()
    private var lastI420Frame: VideoFrame? = null
    private var lastRgbaFrame: VideoFrame? = null
    private val renderMatrix = Matrix()
    private val rgbaUploader = RGBAUploader()

    private fun calculateTransformedRenderSize(width: Int, height: Int, matrix: Matrix?) {
        if (matrix == null) {
            renderWidth = width
            renderHeight = height
        } else {
            matrix.mapPoints(dstPoints, srcPoints)
            for (var4 in 0..2) {
                var points = dstPoints
                points[var4 * 2 + 0] *= width.toFloat()
                points = dstPoints
                points[var4 * 2 + 1] *= height.toFloat()
            }
            renderWidth = distance(
                dstPoints[0],
                dstPoints[1],
                dstPoints[2],
                dstPoints[3]
            )
            renderHeight = distance(
                dstPoints[0],
                dstPoints[1],
                dstPoints[4],
                dstPoints[5]
            )
        }
    }

    @JvmOverloads
    fun drawFrame(
        videoFrame: VideoFrame,
        glDrawer: RendererCommon.GlDrawer,
        matrix: Matrix? = null,
        startX: Int = 0,
        startY: Int = 0,
        width: Int = videoFrame.rotatedWidth,
        height: Int = videoFrame.rotatedHeight
    ) {
        val frameWidth = videoFrame.rotatedWidth
        val frameHeight = videoFrame.rotatedHeight
        calculateTransformedRenderSize(frameWidth, frameHeight, matrix)
        val isTexture = videoFrame.buffer is TextureBuffer
        val isRgb = videoFrame.buffer is RgbaBuffer
        renderMatrix.reset()
        renderMatrix.preTranslate(0.5f, 0.5f)
        if (!isTexture) {
            renderMatrix.preScale(1.0f, -1.0f)
        }
        renderMatrix.preRotate(videoFrame.rotation.toFloat())
        renderMatrix.preTranslate(-0.5f, -0.5f)
        if (matrix != null) {
            renderMatrix.preConcat(matrix)
        }
        when {
            isTexture -> {
                lastI420Frame = null
                lastRgbaFrame = null
                drawTexture(
                    glDrawer,
                    videoFrame.buffer as TextureBuffer,
                    renderMatrix,
                    renderWidth,
                    renderHeight,
                    startX,
                    startY,
                    width,
                    height
                )
            }
            isRgb -> {
                if (videoFrame !== lastRgbaFrame) {
                    lastRgbaFrame = videoFrame
                    val rgbaBuffer = videoFrame.buffer as RgbaBuffer
                    rgbaUploader.uploadData(rgbaBuffer.buffer, rgbaBuffer.width, rgbaBuffer.height)
                    rgbaBuffer.release()
                }
                glDrawer.drawRgb(
                    rgbaUploader.textureId,
                    RendererCommon.convertMatrixFromAndroidGraphicsMatrix(renderMatrix),
                    renderWidth,
                    renderHeight,
                    startX,
                    startY,
                    width,
                    height
                )
            }
            else -> {
                if (videoFrame !== lastI420Frame) {
                    lastI420Frame = videoFrame
                    val i420Buffer = videoFrame.buffer.toI420()
                    yuvUploader.uploadFromBuffer(i420Buffer)
                    i420Buffer.release()
                }
                glDrawer.drawYuv(
                    yuvUploader.yuvTextures,
                    RendererCommon.convertMatrixFromAndroidGraphicsMatrix(renderMatrix),
                    renderWidth,
                    renderHeight,
                    startX,
                    startY,
                    width,
                    height
                )
            }
        }
    }

    fun release() {
        yuvUploader.release()
        lastI420Frame = null
        rgbaUploader.release()
        lastRgbaFrame = null
    }

    private class RGBAUploader {
        var textureId = 0
            private set
        private var mData: ByteBuffer? = null
        fun uploadData(byteBuffer: ByteBuffer?, width: Int, height: Int): Int {
            mData = byteBuffer
            if (textureId == 0) {
                textureId = GlUtil.generateTexture(GLES20.GL_TEXTURE_2D)
            }
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, mData)
            GlUtil.checkNoGLES2Error("glTexImage2D")
            return textureId
        }

        fun release() {
            mData = null
            if (textureId != 0) {
                GLES20.glDeleteTextures(1, intArrayOf(textureId), 0)
            }
        }

    }

    private class YuvUploader {
        private var copyBuffer: ByteBuffer? = null
        var yuvTextures: IntArray? = null
            private set

        fun uploadYuvData(width: Int, height: Int, strideArray: IntArray, dataArray: Array<ByteBuffer?>): IntArray? {
            val widthArray = intArrayOf(width, width / 2, width / 2)
            val heightArray = intArrayOf(height, height / 2, height / 2)
            var pixelCount = 0
            var textureIndex: Int
            textureIndex = 0
            while (textureIndex < 3) {
                if (strideArray[textureIndex] > widthArray[textureIndex]) {
                    pixelCount = pixelCount.coerceAtLeast(widthArray[textureIndex] * heightArray[textureIndex])
                }
                ++textureIndex
            }
            if (pixelCount > 0 && (copyBuffer == null || copyBuffer!!.capacity() < pixelCount)) {
                copyBuffer = ByteBuffer.allocateDirect(pixelCount)
            }
            if (yuvTextures == null) {
                yuvTextures = IntArray(3)
                textureIndex = 0
                while (textureIndex < 3) {
                    yuvTextures!![textureIndex] = GlUtil.generateTexture(GLES20.GL_TEXTURE_2D)
                    ++textureIndex
                }
            }
            textureIndex = 0
            while (textureIndex < 3) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureIndex)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, yuvTextures!![textureIndex])
                val byteBuffer: ByteBuffer? = if (strideArray[textureIndex] == widthArray[textureIndex]) {
                    dataArray[textureIndex]
                } else {
                    copyBuffer
                }
                GLES20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_LUMINANCE,
                    widthArray[textureIndex],
                    heightArray[textureIndex],
                    0,
                    GLES20.GL_LUMINANCE,
                    GLES20.GL_UNSIGNED_BYTE,
                    byteBuffer
                )
                ++textureIndex
            }
            return yuvTextures
        }

        fun uploadFromBuffer(i420Buffer: I420Buffer): IntArray? {
            val strideArr = intArrayOf(i420Buffer.strideY, i420Buffer.strideU, i420Buffer.strideV)
            val dataArray = arrayOf(i420Buffer.dataY, i420Buffer.dataU, i420Buffer.dataV)
            return uploadYuvData(i420Buffer.width, i420Buffer.height, strideArr, dataArray)
        }

        fun release() {
            copyBuffer = null
            if (yuvTextures != null) {
                GLES20.glDeleteTextures(3, yuvTextures, 0)
                yuvTextures = null
            }
        }
    }

    companion object {
        val srcPoints = floatArrayOf(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f)

        /**
         * glDrawer,
        videoFrame.buffer as TextureBuffer,
        renderMatrix,
        renderWidth,
        renderHeight,
        startX,
        startY,
        width,
        height
         */
        fun drawTexture(
            glDrawer: RendererCommon.GlDrawer,
            textureBuffer: TextureBuffer,
            matrix: Matrix?,
            renderWidth: Int,
            renderHeight: Int,
            startX: Int,
            startY: Int,
            width: Int,
            height: Int
        ) {
            val finalMatrix = Matrix(textureBuffer.transformMatrix)
            finalMatrix.preConcat(matrix)
            val renderMatrix = RendererCommon.convertMatrixFromAndroidGraphicsMatrix(finalMatrix)
            when (textureBuffer.type) {
                TextureBuffer.Type.OES -> glDrawer.drawOes(
                    textureBuffer.textureId,
                    renderMatrix,
                    renderWidth,
                    renderHeight,
                    startX,
                    startY,
                    width,
                    height
                )
                TextureBuffer.Type.RGB -> glDrawer.drawRgb(
                    textureBuffer.textureId,
                    renderMatrix,
                    renderWidth,
                    renderHeight,
                    startX,
                    startY,
                    width,
                    height
                )
                else -> throw RuntimeException("Unknown texture type.")
            }
        }

        private fun distance(x0: Float, y0: Float, x1: Float, y1: Float): Int {
            return hypot((x1 - x0).toDouble(), (y1 - y0).toDouble()).roundToInt()
        }
    }
}