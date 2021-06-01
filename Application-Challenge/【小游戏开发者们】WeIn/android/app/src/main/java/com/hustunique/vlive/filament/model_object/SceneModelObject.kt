package com.hustunique.vlive.filament.model_object

import com.google.android.filament.TransformManager

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/3/21
 */
class SceneModelObject : FilamentBaseModelObject("models/room.glb") {
    private var rootInstance: Int = 0
    private var transformManager: TransformManager? = null

    private val baseOffsetMatrix = FloatArray(16).apply {
        this[0] = 1f
        this[5] = 1f
        this[10] = 1f
        this[15] = 1f
        this[13] = -0.3f
    }

    override fun onAssetSet() {
        asset?.let {
            val tm = filamentContext!!.getTransformManager()
            rootInstance = tm.getInstance(it.root)
            transformManager = tm
        }
    }

    override fun update(frameTimeNanos: Long) {
        transformManager?.let { tm ->
            tm.setTransform(rootInstance, baseOffsetMatrix)
        }

    }
}