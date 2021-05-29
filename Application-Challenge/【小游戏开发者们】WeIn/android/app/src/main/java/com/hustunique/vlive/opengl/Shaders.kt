package com.hustunique.vlive.opengl

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/5/21
 */
object Shaders {


    const val vertexShader = """
#version 300 es
in vec4 a_Position;
in vec2 a_TexCoord;

out vec2 v_TexCoord;

void main() {
    gl_Position = a_Position;
    v_TexCoord = a_TexCoord;
}
    """

    const val fragShader = """
#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
#extension GL_EXT_YUV_target : require

precision mediump float;
in vec2 v_TexCoord;
out vec4 outColor;
uniform samplerExternalOES sTexture;


void main() {
    outColor = texture(sTexture, v_TexCoord);
}
    """

    const val texFragShader = """
#version 300 es

precision mediump float;
in vec2 v_TexCoord;
out vec4 outColor;
uniform sampler2D sTexture;


void main() {
    outColor = texture(sTexture, v_TexCoord);
}
    """


}