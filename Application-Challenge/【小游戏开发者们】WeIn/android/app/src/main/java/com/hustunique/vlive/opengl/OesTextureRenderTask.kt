package com.hustunique.vlive.opengl

import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.hustunique.vlive.util.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/5/21
 */
class OesTextureRenderTask(
    private val inTexture: Int,
) : RenderTask {

    companion object {
        private const val TAG = "OesTextureRenderTask"

        private const val COORDS_PER_VERTEX = 2
        private const val TEXCOORDS_PER_VERTEX = 2
        private const val FLOAT_SIZE = 4
        private val QUAD_COORDS = ByteBuffer
            .allocateDirect(8 * FLOAT_SIZE)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(
                floatArrayOf(
                    -1.0f, -1.0f,
                    +1.0f, -1.0f,
                    -1.0f, +1.0f,
                    +1.0f, +1.0f
                )
            )
        private val QUAD_TEX_COORDS = ByteBuffer
            .allocateDirect(8 * FLOAT_SIZE)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(
                floatArrayOf(
                    0f, 1f,
                    1f, 1f,
                    0f, 0f,
                    1f, 0f
                )
            )
    }

    private var renderProgram: Int = -1
    private var positionAttrib: Int = -1
    private var texCoordAttrib: Int = -1
    private var textureUniform: Int = -1

    override fun init() {
        val vertexShader = ShaderUtil.loadShader(
            Shaders.vertexShader,
            GLES20.GL_VERTEX_SHADER
        )
        val fragmentShader = ShaderUtil.loadShader(
            Shaders.fragShader,
            GLES20.GL_FRAGMENT_SHADER
        )

        renderProgram = GLES20.glCreateProgram()
        GLES20.glAttachShader(renderProgram, vertexShader)
        GLES20.glAttachShader(renderProgram, fragmentShader)
        GLES20.glLinkProgram(renderProgram)
        GLES20.glUseProgram(renderProgram)
        ShaderUtil.checkGLError(TAG, "Program creation")

        positionAttrib = GLES20.glGetAttribLocation(renderProgram, "a_Position")
        texCoordAttrib = GLES20.glGetAttribLocation(renderProgram, "a_TexCoord")
        ShaderUtil.checkGLError(TAG, "Program creation")

        textureUniform = GLES20.glGetUniformLocation(renderProgram, "sTexture")
        ShaderUtil.checkGLError(TAG, "Program parameters")
    }

    override fun render() {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthMask(false)
        GLES20.glViewport(0, 0, 120, 160)

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, inTexture)
        GLES20.glUseProgram(renderProgram)
        GLES20.glUniform1i(textureUniform, 0)

        QUAD_COORDS.position(0)
        QUAD_TEX_COORDS.position(0)
        // Set the vertex positions and texture coordinates.
        GLES20.glVertexAttribPointer(
            positionAttrib,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            0,
            QUAD_COORDS
        )
        GLES20.glVertexAttribPointer(
            texCoordAttrib,
            TEXCOORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            0,
            QUAD_TEX_COORDS
        )
        GLES20.glEnableVertexAttribArray(positionAttrib)
        GLES20.glEnableVertexAttribArray(texCoordAttrib)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // Restore the depth state for further drawing.
        GLES20.glDepthMask(true)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        ShaderUtil.checkGLError(TAG, "OES renderer")
    }

    override fun release() {
    }
}