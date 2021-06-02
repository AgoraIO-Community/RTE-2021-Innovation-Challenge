package com.dong.circlelive.gl

import android.opengl.GLES20
import com.dong.circlelive.appContext
import java.nio.FloatBuffer

class LookupFilter @JvmOverloads constructor(
    var table: String = FILTER_DEFAULT(),
    intensity: Float = 1f
) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADERX, LOOKUP_FRAGMENT_SHADER
) {

    var vertexMatrix = floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)

    var mLookupTextureUniform = 0
    var mLookupSourceTexture = OpenGlUtils.NO_TEXTURE
    private var mIntensityLocation = 0
    var intensity = 1f

    init {
        this.intensity = intensity
    }

    fun setIntensity(intensity: Float): Boolean {
        if (this.intensity == intensity) return false
        this.intensity = intensity
        return true
    }

    fun restore() {
        mTexBuffer!!.put(coord)
        mTexBuffer!!.position(0)
    }

    fun onDraw(textureId: Int) {
        onDraw(textureId, mVerBuffer, mTexBuffer)
    }

    override fun onDraw(textureId: Int, cubeBuffer: FloatBuffer?, textureBuffer: FloatBuffer?) {
        GLES20.glUseProgram(program)
        runPendingOnDrawTasks()
        if (!isInitialized) {
            return
        }
        drawLookupFilter(textureId, cubeBuffer, textureBuffer)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glUseProgram(0)
    }

    @JvmOverloads
    fun onDrawInFrameTexture(textureId: Int, cubeBuffer: FloatBuffer? = mVerBuffer, textureBuffer: FloatBuffer? = mTexBuffer) {
        GLES20.glUseProgram(program)
        runPendingOnDrawTasks()
        if (!isInitialized) {
            return
        }
        OpenGlUtils.bindFrameTexture(fFrame[0], fTexture[0])
        GLES20.glViewport(0, 0, outputWidth, outputHeight)
        drawLookupFilter(textureId, cubeBuffer, textureBuffer)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        OpenGlUtils.unBindFrameBuffer()
        GLES20.glUseProgram(0)
    }

    private fun drawLookupFilter(textureId: Int, cubeBuffer: FloatBuffer?, textureBuffer: FloatBuffer?) {
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, vertexMatrix, 0)
        cubeBuffer!!.position(0)
        GLES20.glVertexAttribPointer(attribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer)
        GLES20.glEnableVertexAttribArray(attribPosition)
        textureBuffer!!.position(0)
        GLES20.glVertexAttribPointer(attribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glEnableVertexAttribArray(attribTextureCoordinate)
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
            GLES20.glUniform1i(uniformTexture, 0)
        }
        onDrawArraysPre()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glDisableVertexAttribArray(attribTextureCoordinate)
        onDrawArraysAfter()
    }

    override fun onInit() {
        super.onInit()
        mLookupTextureUniform = GLES20.glGetUniformLocation(program, "inputImageTexture2")
        mIntensityLocation = GLES20.glGetUniformLocation(program, "intensity")
        mHMatrix = GLES20.glGetUniformLocation(program, "vMatrix")
        mLookupSourceTexture = OpenGlUtils.NO_TEXTURE
    }

    private val loadFilterAsset = Runnable {
        mLookupSourceTexture = OpenGlUtils.loadTexture(mLookupSourceTexture, appContext, table, true)
    }

    override fun onInitialized() {
        super.onInitialized()
        setFloat(mIntensityLocation, intensity)
        runOnDrawAfterRemoveExist(loadFilterAsset)
    }

    fun changeFilter(filter: String): Boolean {
        if (table == filter) return false
        table = filter
        runOnDrawAfterRemoveExist(loadFilterAsset)
        return true
    }

    fun changeFilter(lutTextureId: Int) {
        mLookupSourceTexture = lutTextureId
    }

    override fun onDestroy() {
        super.onDestroy()
        val texture = intArrayOf(mLookupSourceTexture)
        GLES20.glDeleteTextures(1, texture, 0)
        mLookupSourceTexture = -1
    }

    override fun onDrawArraysAfter() {
        if (mLookupSourceTexture != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE3)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        }
    }

    override fun onDrawArraysPre() {
        if (mLookupSourceTexture != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE3)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLookupSourceTexture)
            GLES20.glUniform1i(mLookupTextureUniform, 3)
            GLES20.glUniform1f(mIntensityLocation, intensity)
        }
    }

    companion object {

        fun FILTER_DEFAULT() = "lut/lut_day_for_night.jpg"

        const val NO_FILTER_VERTEX_SHADERX = "" +
                "attribute vec4 position;\n" +
                "attribute vec2 inputTextureCoordinate;\n" +
                "uniform mat4 vMatrix;\n" +
                "varying vec2 textureCoordinate;\n" +
                "void main(){\n" +
                "    gl_Position = vMatrix*position;\n" +
                "    textureCoordinate = inputTextureCoordinate.xy;\n" +
                "}"
        const val LOOKUP_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "uniform sampler2D inputImageTexture2;\n" +
                "uniform lowp float intensity;\n" +
                "void main(){\n" +
                "    highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "    highp float blueColor = textureColor.b * 63.0;\n" +
                "    highp vec2 quad1;\n" +
                "    quad1.y = floor(floor(blueColor) / 8.0);\n" +
                "    quad1.x = floor(blueColor) - (quad1.y * 8.0);\n" +
                "    highp vec2 quad2;\n" +
                "    quad2.y = floor(ceil(blueColor) / 8.0);\n" +
                "    quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n" +
                "    highp vec2 texPos1;\n" +
                "    texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n" +
                "    texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n" +
                "    highp vec2 texPos2;\n" +
                "    texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n" +
                "    texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n" +
                "    lowp vec4 newColor1 = texture2D(inputImageTexture2, texPos1);\n" +
                "    lowp vec4 newColor2 = texture2D(inputImageTexture2, texPos2);\n" +
                "    lowp vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n" +
                "    gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), intensity);\n" +
                "}\n"
    }

}