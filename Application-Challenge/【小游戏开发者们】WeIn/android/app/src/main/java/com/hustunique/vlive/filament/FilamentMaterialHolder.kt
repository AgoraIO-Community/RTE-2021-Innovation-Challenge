package com.hustunique.vlive.filament

import com.google.android.filament.Colors
import com.google.android.filament.Engine
import com.google.android.filament.Material
import com.google.android.filament.MaterialInstance
import java.nio.Buffer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/3/21
 */
class FilamentMaterialHolder(private val engine: Engine) {

    var videoMaterial: Material? = null

    fun loadVideoMaterial(buffer: Buffer) {
        videoMaterial = Material.Builder().payload(buffer, buffer.remaining()).build(engine)
    }

}