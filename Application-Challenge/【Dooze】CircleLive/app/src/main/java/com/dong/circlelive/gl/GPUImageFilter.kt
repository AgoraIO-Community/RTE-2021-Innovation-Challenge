/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dong.circlelive.gl

import android.content.Context
import android.graphics.PointF
import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.dong.circlelive.base.Timber.Companion.e
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*

open class GPUImageFilter @JvmOverloads constructor(
    vertexShader: String = NO_FILTER_VERTEX_SHADER,
    fragmentShader: String = YUV_TO_RGB
) {
    private val mRunOnDraw: LinkedList<Runnable> = LinkedList()

    private val mVertexShader: String = vertexShader
    private val mFragmentShader: String = fragmentShader

    var program = 0
        protected set
    var attribPosition = 0
        protected set
    var uniformTexture = 0
        protected set
    var attribTextureCoordinate = 0
        protected set
    var outputWidth = 0
        protected set
    var outputHeight = 0
        protected set
    var isInitialized = false
        protected set
    protected var mHCoordMatrix = 0
    protected var mHMatrix = 0

    /**
     * 顶点坐标Buffer
     */
    protected open var mVerBuffer: FloatBuffer? = null

    /**
     * 纹理坐标Buffer
     */
    protected open var mTexBuffer: FloatBuffer? = null
    protected open var fFrame = IntArray(1)
    protected var fTexture = IntArray(1)
    var matrix = floatArrayOf(
        1f, 0f, 0f, 0f,
        0f, 1.0f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    )
    var mCoordMatrix = floatArrayOf(0f, -1f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 1f)
    private val flipCoord = floatArrayOf(
        0.0f, 1.0f,
        0.0f, 0.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    )

    //顶点坐标
    protected var pos = floatArrayOf(
        -1.0f, 1.0f,
        -1.0f, -1.0f,
        1.0f, 1.0f,
        1.0f, -1.0f
    )
    private var tempPos: FloatArray? = null

    //纹理坐标
    var coord = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        1.0f, 1.0f
    )


    init {
        initBufferX()
    }

    fun setCoordMatrix(matrix: FloatArray) {
        mCoordMatrix = matrix
    }

    fun flip() {
        mTexBuffer!!.put(flipCoord)
        mTexBuffer!!.position(0)
    }

    fun updateTextureCoor(matrix: FloatArray) {
        if (matrix.size != 8) throw RuntimeException("invalid texture matrix")
        mTexBuffer!!.clear()
        mTexBuffer!!.put(matrix)
        mTexBuffer!!.position(0)
    }

    fun init() {
        onInit()
        this.isInitialized = true
        onInitialized()
    }

    fun initIfNeed() {
        if (!this.isInitialized) init()
    }

    open fun onInit() {
        program = OpenGlUtils.loadProgram(mVertexShader, mFragmentShader)
        attribPosition = GLES20.glGetAttribLocation(program, "position")
        uniformTexture = GLES20.glGetUniformLocation(program, "inputImageTexture")
        attribTextureCoordinate = GLES20.glGetAttribLocation(
            program,
            "inputTextureCoordinate"
        )
        mHCoordMatrix = GLES20.glGetUniformLocation(program, "vCoordMatrix")
        mHMatrix = GLES20.glGetUniformLocation(program, "vMatrix")
        this.isInitialized = true
    }

    open fun onInitialized() {
        GLES20.glGenFramebuffers(1, fFrame, 0)
    }

    fun destroy() {
        this.isInitialized = false
        GLES20.glDeleteProgram(program)
        onDestroy()
    }

    open fun onDestroy() {
        mRunOnDraw.clear()
    }

    fun onOutputSizeChanged(width: Int, height: Int) {
        outputWidth = width
        outputHeight = height
        //创建FrameBuffer和Texture
        deleteFrameBuffer()
        GLES20.glGenFramebuffers(1, fFrame, 0)
        OpenGlUtils.genTexturesWithParameter(1, fTexture, 0, GLES20.GL_RGBA, width, height)
    }

    private fun deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0)
        GLES20.glDeleteTextures(1, fTexture, 0)
    }

    val outputTexture: Int
        get() = fTexture[0]

    open fun onDraw(
        textureId: Int, cubeBuffer: FloatBuffer?,
        textureBuffer: FloatBuffer?
    ) {
        GLES20.glUseProgram(program)
        runPendingOnDrawTasks()
        if (!this.isInitialized) {
            return
        }
        OpenGlUtils.bindFrameTexture(fFrame[0], fTexture[0])
        GLES20.glViewport(0, 0, outputWidth, outputHeight)
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0)
        GLES20.glUniformMatrix4fv(mHCoordMatrix, 1, false, mCoordMatrix, 0)
        mVerBuffer!!.position(0)
        GLES20.glVertexAttribPointer(attribPosition, 2, GLES20.GL_FLOAT, false, 0, mVerBuffer)
        GLES20.glEnableVertexAttribArray(attribPosition)
        mTexBuffer!!.position(0)
        GLES20.glVertexAttribPointer(
            attribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
            mTexBuffer
        )
        GLES20.glEnableVertexAttribArray(attribTextureCoordinate)
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
            GLES20.glUniform1i(uniformTexture, 0)
        }
        onDrawArraysPre()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glDisableVertexAttribArray(attribTextureCoordinate)
        onDrawArraysAfter()
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        OpenGlUtils.unBindFrameBuffer()
    }

    fun onDrawWithBuffer(textureId: Int, cubeBuffer: FloatBuffer, textureBuffer: FloatBuffer?) {
        GLES20.glUseProgram(program)
        runPendingOnDrawTasks()
        if (!this.isInitialized) {
            return
        }
        OpenGlUtils.bindFrameTexture(fFrame[0], fTexture[0])
        GLES20.glViewport(0, 0, outputWidth, outputHeight)
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0)
        GLES20.glUniformMatrix4fv(mHCoordMatrix, 1, false, mCoordMatrix, 0)
        cubeBuffer.position(0)
        GLES20.glVertexAttribPointer(attribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer)
        GLES20.glEnableVertexAttribArray(attribPosition)
        GLES20.glVertexAttribPointer(attribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glEnableVertexAttribArray(attribTextureCoordinate)
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
            GLES20.glUniform1i(uniformTexture, 0)
        }
        onDrawArraysPre()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glDisableVertexAttribArray(attribTextureCoordinate)
        onDrawArraysAfter()
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        OpenGlUtils.unBindFrameBuffer()
    }

    fun onDrawWithoutBindFrame(
        textureId: Int, cubeBuffer: FloatBuffer,
        textureBuffer: FloatBuffer
    ) {
        GLES20.glUseProgram(program)
        runPendingOnDrawTasks()
        if (!this.isInitialized) {
            return
        }
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0)
        GLES20.glUniformMatrix4fv(mHCoordMatrix, 1, false, mCoordMatrix, 0)
        cubeBuffer.position(0)
        GLES20.glVertexAttribPointer(attribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer)
        GLES20.glEnableVertexAttribArray(attribPosition)
        textureBuffer.position(0)
        GLES20.glVertexAttribPointer(
            attribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
            textureBuffer
        )
        GLES20.glEnableVertexAttribArray(attribTextureCoordinate)
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
            GLES20.glUniform1i(uniformTexture, 0)
        }
        onDrawArraysPre()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glDisableVertexAttribArray(attribTextureCoordinate)
        onDrawArraysAfter()
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        GLES20.glUseProgram(0)
    }

    protected fun initBufferX() {
        val a = ByteBuffer.allocateDirect(32)
        a.order(ByteOrder.nativeOrder())
        val mVerBuffer = a.asFloatBuffer()
        mVerBuffer.put(pos)
        mVerBuffer.position(0)
        val b = ByteBuffer.allocateDirect(32)
        b.order(ByteOrder.nativeOrder())
        val mTexBuffer = b.asFloatBuffer()
        mTexBuffer.put(coord)
        mTexBuffer.position(0)
        this.mVerBuffer = mVerBuffer
        this.mTexBuffer = mTexBuffer
    }

    protected open fun onDrawArraysPre() {}
    protected open fun onDrawArraysAfter() {}
    protected fun runPendingOnDrawTasks() {
        synchronized(mRunOnDraw) {
            while (!mRunOnDraw.isEmpty()) {
                try {
                    mRunOnDraw.removeFirst().run()
                } catch (e: NoSuchElementException) {
                    e(e)
                    e.printStackTrace()
                }
            }
        }
    }

    protected fun setInteger(location: Int, intValue: Int) {
        runOnDraw { GLES20.glUniform1i(location, intValue) }
    }

    protected open fun setFloat(location: Int, floatValue: Float) {
        runOnDraw { GLES20.glUniform1f(location, floatValue) }
    }

    protected fun setFloatVec2(location: Int, arrayValue: FloatArray?) {
        runOnDraw { GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue)) }
    }

    protected fun setFloatVec3(location: Int, arrayValue: FloatArray?) {
        runOnDraw { GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue)) }
    }

    protected fun setFloatVec4(location: Int, arrayValue: FloatArray?) {
        runOnDraw { GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue)) }
    }

    protected fun setFloatArray(location: Int, arrayValue: FloatArray) {
        runOnDraw { GLES20.glUniform1fv(location, arrayValue.size, FloatBuffer.wrap(arrayValue)) }
    }

    protected fun setPoint(location: Int, point: PointF) {
        runOnDraw {
            val vec2 = FloatArray(2)
            vec2[0] = point.x
            vec2[1] = point.y
            GLES20.glUniform2fv(location, 1, vec2, 0)
        }
    }

    protected fun setUniformMatrix3f(location: Int, matrix: FloatArray?) {
        runOnDraw { GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0) }
    }

    protected fun setUniformMatrix4f(location: Int, matrix: FloatArray?) {
        runOnDraw { GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0) }
    }

    protected fun runOnDraw(runnable: Runnable) {
        synchronized(mRunOnDraw) { mRunOnDraw.addLast(runnable) }
    }

    protected fun runOnDrawAfterRemoveExist(runnable: Runnable) {
        synchronized(mRunOnDraw) {
            mRunOnDraw.remove(runnable)
            mRunOnDraw.addLast(runnable)
        }
    }

    /**
     * 用来计算贴纸渲染的纹理最终需要的顶点坐标,处理纹理宽高与显示的surface宽高不一致导致的画面拉伸
     */
    fun centerCropVertexBuffer(displayW: Int, displayH: Int, imageW: Int, imageH: Int) {
        val ratio1 = displayW.toFloat() / imageW
        val ratio2 = displayH.toFloat() / imageH
        val ratioMin = Math.min(ratio1, ratio2)
        val imageWidthNew = Math.round(imageW * ratioMin)
        val imageHeightNew = Math.round(imageH * ratioMin)
        val ratioWidth = imageWidthNew * 1.0f / displayW
        val ratioHeight = imageHeightNew * 1.0f / displayH
        if (tempPos == null) {
            tempPos = floatArrayOf(
                -1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, 1.0f,
                1.0f, -1.0f
            )
        }
        pos[0] = tempPos!![0] / ratioHeight
        pos[1] = tempPos!![1] / ratioWidth
        pos[2] = tempPos!![2] / ratioHeight
        pos[3] = tempPos!![3] / ratioWidth
        pos[4] = tempPos!![4] / ratioHeight
        pos[5] = tempPos!![5] / ratioWidth
        pos[6] = tempPos!![6] / ratioHeight
        pos[7] = tempPos!![7] / ratioWidth
        if (mTexBuffer == null) {
            mTexBuffer = ByteBuffer.allocateDirect(pos.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        }
        mVerBuffer!!.clear()
        mVerBuffer!!.put(pos).position(0)
    }

    companion object {
        const val NO_FILTER_VERTEX_SHADER = "" +
                "attribute vec4 position;\n" +
                "attribute vec2 inputTextureCoordinate;\n" +
                "uniform mat4 vMatrix;\n" +
                "uniform mat4 vCoordMatrix;\n" +
                "varying vec2 textureCoordinate;\n" +
                " \n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = vMatrix*position;\n" +
                "    textureCoordinate = (vCoordMatrix*vec4(inputTextureCoordinate,0,1)).xy;\n" +
                "}"
        const val NO_FILTER_FRAGMENT_SHADER = "" +
                "varying highp vec2 textureCoordinate;\n" +
                " \n" +
                "uniform sampler2D inputImageTexture;\n" +
                " \n" +
                "void main()\n" +
                "{\n" +
                "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "}"
        const val YUV_TO_RGB_X = "attribute vec4 position;\n" +
                "attribute vec4 inputTextureCoordinate;\n" +
                "\n" +
                "uniform mat4 textureTransform;\n" +
                "varying vec2 textureCoordinate;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "\ttextureCoordinate = (textureTransform * inputTextureCoordinate).xy;\n" +
                "\tgl_Position = position;\n" +
                "}\n"
        const val YUV_TO_RGB = "#extension GL_OES_EGL_image_external : require\n" +
                "varying highp vec2 textureCoordinate;\n" +
                "uniform samplerExternalOES inputImageTexture;\n" +
                "void main(){\n" +
                "  gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "}"

        fun loadShader(file: String?, context: Context): String {
            try {
                val assetManager = context.assets
                val ims = assetManager.open(file!!)
                val re = convertStreamToString(ims)
                ims.close()
                return re
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun convertStreamToString(`is`: InputStream?): String {
            val s = Scanner(`is`).useDelimiter("\\A")
            return if (s.hasNext()) s.next() else ""
        }
    }

}