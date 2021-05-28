package com.hustunique.vlive.filament.model_object

import com.google.android.filament.TransformManager
import com.hustunique.vlive.data.AddEvent
import com.hustunique.vlive.data.MathUtil

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/27
 */
abstract class UnityModelObject(path: String) : FilamentBaseModelObject(path) {
    private var rootInstance: Int = 0
    private var transformManager: TransformManager? = null
    private val transformMatrix = FloatArray(16)


    override fun onAssetSet() {
        asset?.let {
            val tm = filamentContext!!.getTransformManager()
            rootInstance = tm.getInstance(it.root)
            transformManager = tm
        }
    }

    fun setEventData(event: AddEvent) {
        MathUtil.packPRST(event.position, event.rotation, event.scale, transformMatrix)
    }

    override fun update(frameTimeNanos: Long) {
        transformManager?.setTransform(rootInstance, transformMatrix)
    }
}