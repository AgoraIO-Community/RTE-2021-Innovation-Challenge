package com.hustunique.vlive.filament.model_object

import com.google.android.filament.RenderableManager
import com.google.android.filament.TransformManager
import com.hustunique.vlive.data.MathUtil
import com.hustunique.vlive.data.Quaternion
import com.hustunique.vlive.data.Vector3

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/19
 */
open class ActorModelObject(path: String = "models/actor.glb") : FilamentBaseModelObject(path) {
    private var rootInstance: Int = 0
    private var transformManager: TransformManager? = null
    private var renderManager: RenderableManager? = null
    private val transformMatrix = FloatArray(16)

    private var leyeEntity: Int = 0
    private var reyeEntity: Int = 0
    private var mouthEntity: Int = 0

    override fun onAssetSet() {
        asset?.let {
            renderManager = filamentContext!!.getRenderableManager()
            val tm = filamentContext!!.getTransformManager()
            rootInstance = tm.getInstance(it.root)
            transformManager = tm
            leyeEntity = it.getFirstEntityByName("leye")
            reyeEntity = it.getFirstEntityByName("reye")
            mouthEntity = it.getFirstEntityByName("mouth")
        }
    }

    override fun update(frameTimeNanos: Long) {
        transformManager?.let { tm ->
            property?.let {
                val buffer = it.objectData
                buffer.rewind()
                val q = Quaternion.readFromBuffer(buffer)
                val pos = Vector3.readFromBuffer(buffer)
                MathUtil.packRotationAndPosT(q, pos, transformMatrix)
                tm.setTransform(rootInstance, transformMatrix)
                renderManager?.run {
                    setMorphWeights(
                        getInstance(mouthEntity),
                        floatArrayOf(1f - it.mouthOpenWeight, 0f, 0f, 0f)
                    )
                    setMorphWeights(
                        getInstance(leyeEntity),
                        floatArrayOf(1f - it.lEyeOpenProbability, 0f, 0f, 0f)
                    )
                    setMorphWeights(
                        getInstance(reyeEntity),
                        floatArrayOf(1f - it.rEyeOpenProbability, 0f, 0f, 0f)
                    )
                }
            }
        }
    }
}