package com.dong.circlelive.rtc

import android.opengl.GLES11Ext
import android.opengl.GLES20
import io.agora.rtc.gl.GlShader
import io.agora.rtc.gl.GlUtil
import io.agora.rtc.gl.RendererCommon
import java.nio.FloatBuffer
import java.util.*

class GlRectDrawer : RendererCommon.GlDrawer {
    private var mTexCoordinate = GlUtil.createFloatBuffer(floatArrayOf(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f))
    private var mPosCoordinate = GlUtil.createFloatBuffer(floatArrayOf(-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f))
    private val shaders: MutableMap<String, Shader?> = IdentityHashMap<String, Shader?>()

    override fun drawOes(
        oesTextureId: Int,
        transformMatrix: FloatArray,
        renderWidth: Int,
        renderHeight: Int,
        startX: Int,
        startY: Int,
        width: Int,
        height: Int
    ) {
        this.prepareShader(
            OES_FRAGMENT_SHADER_STRING,
            transformMatrix
        )
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, oesTextureId)
        drawRectangle(startX, startY, width, height)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    }

    override fun drawRgb(
        textureId: Int,
        transformMatrix: FloatArray,
        renderWidth: Int,
        renderHeight: Int,
        startX: Int,
        startY: Int,
        width: Int,
        height: Int
    ) {
        this.prepareShader(
            RGB_FRAGMENT_SHADER_STRING,
            transformMatrix
        )
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        drawRectangle(startX, startY, width, height)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
    }

    override fun drawYuv(
        yuvTextures: IntArray,
        transformMatrix: FloatArray,
        renderWidth: Int,
        renderHeight: Int,
        startX: Int,
        startY: Int,
        width: Int,
        height: Int
    ) {
        this.prepareShader(
            YUV_FRAGMENT_SHADER_STRING,
            transformMatrix
        )
        var textureIndex = 0
        while (textureIndex < 3) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureIndex)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, yuvTextures[textureIndex])
            ++textureIndex
        }
        drawRectangle(startX, startY, width, height)
        textureIndex = 0
        while (textureIndex < 3) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureIndex)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            ++textureIndex
        }
    }

    private fun drawRectangle(startX: Int, startY: Int, width: Int, height: Int) {
        GLES20.glViewport(startX, startY, width, height)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun prepareShader(shaderStr: String, matrix: FloatArray) {
        val shader: Shader?
        if (shaders.containsKey(shaderStr)) {
            shader = shaders[shaderStr]
        } else {
            shader = Shader(shaderStr)
            shaders[shaderStr] = shader
            shader.glShader.useProgram()
            when (shaderStr) {
                YUV_FRAGMENT_SHADER_STRING -> {
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("y_tex"), 0)
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("u_tex"), 1)
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("v_tex"), 2)
                }
                RGB_FRAGMENT_SHADER_STRING -> {
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("rgb_tex"), 0)
                }
                else -> {
                    check(OES_FRAGMENT_SHADER_STRING == shaderStr) { "Unknown fragment shader: $shaderStr" }
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("oes_tex"), 0)
                }
            }
            GlUtil.checkNoGLES2Error("Initialize fragment shader uniform values.")
            shader.glShader.setVertexAttribArray("in_pos", 2, FULL_RECTANGLE_BUF)
            shader.glShader.setVertexAttribArray("in_tc", 2, FULL_RECTANGLE_TEX_BUF)
        }
        shader!!.glShader.useProgram()
        GLES20.glUniformMatrix4fv(shader.texMatrixLocation, 1, false, matrix, 0)
    }

    private fun prepareShader(shaderStr: String, matrix: FloatArray, texBuffer: FloatBuffer, vexBuffer: FloatBuffer) {
        val shader: Shader?
        if (shaders.containsKey(shaderStr)) {
            shader = shaders[shaderStr]
        } else {
            shader = Shader(shaderStr)
            shaders[shaderStr] = shader
            shader.glShader.useProgram()
            when {
                shaderStr === YUV_FRAGMENT_SHADER_STRING -> {
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("y_tex"), 0)
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("u_tex"), 1)
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("v_tex"), 2)
                }
                shaderStr === RGB_FRAGMENT_SHADER_STRING -> {
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("rgb_tex"), 0)
                }
                else -> {
                    check(!(shaderStr !== OES_FRAGMENT_SHADER_STRING)) { "Unknown fragment shader: $shaderStr" }
                    GLES20.glUniform1i(shader.glShader.getUniformLocation("oes_tex"), 0)
                }
            }
            GlUtil.checkNoGLES2Error("Initialize fragment shader uniform values.")
        }
        shader!!.glShader.setVertexAttribArray("in_pos", 2, vexBuffer)
        shader.glShader.setVertexAttribArray("in_tc", 2, texBuffer)
        shader.glShader.useProgram()
        GLES20.glUniformMatrix4fv(shader.texMatrixLocation, 1, false, matrix, 0)
    }

//    fun drawOes(
//        var1: Int,
//        var2: FloatArray,
//        var3: Int,
//        var4: Int,
//        var5: Int,
//        var6: Int,
//        var7: Int,
//        var8: Int,
//        var9: Int,
//        var10: Int
//    ) {
//        val var11 = ComputeVertexAttribArray(var3, var4, var9, var10)
//        mTexCoordinate = GlUtil.createFloatBuffer(var11)
//        if (var9 == var7 && var10 == var8) {
//            mPosCoordinate = FULL_RECTANGLE_BUF
//        } else {
//            val var12 = ComputePosVertexAttribArray(var7, var8, var9, var10)
//            mPosCoordinate = GlUtil.createFloatBuffer(var12)
//        }
//        this.prepareShader(
//            OES_FRAGMENT_SHADER_STRING,
//            var2,
//            mTexCoordinate,
//            mPosCoordinate
//        )
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
//        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, var1)
//        drawRectangle(var5, var6, var7, var8)
//        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
//    }

//    fun drawRgb(
//        var1: Int,
//        var2: FloatArray,
//        var3: Int,
//        var4: Int,
//        var5: Int,
//        var6: Int,
//        var7: Int,
//        var8: Int,
//        var9: Int,
//        var10: Int
//    ) {
//        val var11 = ComputeVertexAttribArray(var3, var4, var9, var10)
//        if (var9 == var7 && var10 == var8) {
//            mPosCoordinate = FULL_RECTANGLE_BUF
//        } else {
//            val var12 = ComputePosVertexAttribArray(var7, var8, var9, var10)
//            mPosCoordinate = GlUtil.createFloatBuffer(var12)
//        }
//        mTexCoordinate = GlUtil.createFloatBuffer(var11)
//        this.prepareShader(
//            RGB_FRAGMENT_SHADER_STRING,
//            var2,
//            mTexCoordinate,
//            mPosCoordinate
//        )
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, var1)
//        drawRectangle(var5, var6, var7, var8)
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
//    }
//
//    private fun ComputeVertexAttribArray(var1: Int, var2: Int, var3: Int, var4: Int): FloatArray {
//        val var5 = var3.toFloat() / var4.toFloat()
//        val var6 = var1.toFloat() / var2.toFloat()
//        val var7: Float
//        val var8: Float
//        return if (var6 >= var5) {
//            var7 = var2.toFloat() * var5
//            var8 = (var1.toFloat() - var7) / 2.0f / var1.toFloat()
//            floatArrayOf(var8, 0.0f, 1.0f - var8, 0.0f, var8, 1.0f, 1.0f - var8, 1.0f)
//        } else {
//            var7 = var1.toFloat() / var5
//            var8 = 0.0f
//            val var9 = (var2.toFloat() - var7) / 2.0f / var2.toFloat()
//            floatArrayOf(0.0f, var9, 1.0f, var9, 0.0f, 1.0f - var9, 1.0f, 1.0f - var9)
//        }
//    }
//
//    private fun ComputePosVertexAttribArray(var1: Int, var2: Int, var3: Int, var4: Int): FloatArray {
//        val var5 = if (var2 == var4) -1.0f else 2.0f * (var2 - var4).toFloat() / var2.toFloat() - 1.0f
//        val var6 = if (var1 == var3) 1.0f else 2.0f * var3.toFloat() / var1.toFloat() - 1.0f
//        return floatArrayOf(-1.0f, var5, var6, var5, -1.0f, 1.0f, var6, 1.0f)
//    }

    override fun release() {
        val var1: Iterator<*> = shaders.values.iterator()
        while (var1.hasNext()) {
            val var2 = var1.next() as Shader
            var2.glShader.release()
        }
        shaders.clear()
    }

    private class Shader(shader: String) {
        val glShader: GlShader = GlShader(
            VERTEX_SHADER_STRING,
            shader
        )
        val texMatrixLocation: Int

        init {
            texMatrixLocation = glShader.getUniformLocation("texMatrix")
        }
    }

    companion object {
        private const val VERTEX_SHADER_STRING = """
            varying vec2 interp_tc;
            attribute vec4 in_pos;
            attribute vec4 in_tc;
            uniform mat4 texMatrix;
            void main() {
                gl_Position = in_pos;
                interp_tc = (texMatrix * in_tc).xy;
            }
        """

        private const val YUV_FRAGMENT_SHADER_STRING =  """
                precision mediump float;
                varying vec2 interp_tc;
                uniform sampler2D y_tex;
                uniform sampler2D u_tex;
                uniform sampler2D v_tex;
                void main() {
                    float y = texture2D(y_tex, interp_tc).r;
                    float u = texture2D(u_tex, interp_tc).r - 0.5;
                    float v = texture2D(v_tex, interp_tc).r - 0.5;
                    gl_FragColor = vec4(y + 1.403 * v, y - 0.344 * u - 0.714 * v, y + 1.77 * u, 1);
                }
        """
        private const val RGB_FRAGMENT_SHADER_STRING = """
                precision mediump float;
                varying vec2 interp_tc;
                uniform sampler2D rgb_tex;
                void main() { 
                    gl_FragColor = texture2D(rgb_tex, interp_tc);
                }
        """
        private const val OES_FRAGMENT_SHADER_STRING = """
                #extension GL_OES_EGL_image_external : require
                precision mediump float;
                varying vec2 interp_tc;
                uniform samplerExternalOES oes_tex;
                void main() {
                    gl_FragColor = texture2D(oes_tex, interp_tc);
                }
        """
        private val FULL_RECTANGLE_BUF = GlUtil.createFloatBuffer(floatArrayOf(-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f))
        private val FULL_RECTANGLE_TEX_BUF = GlUtil.createFloatBuffer(floatArrayOf(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f))
    }
}