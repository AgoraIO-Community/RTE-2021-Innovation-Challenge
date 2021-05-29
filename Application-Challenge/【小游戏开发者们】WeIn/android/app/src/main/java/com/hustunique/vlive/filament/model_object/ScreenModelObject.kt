package com.hustunique.vlive.filament.model_object

import com.google.android.filament.Colors
import com.google.android.filament.MaterialInstance
import com.hustunique.vlive.agora.AgoraRawVideoConsumer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/3/21
 */
class ScreenModelObject : ActorModelObject("models/screen_actor.glb") {

    companion object {
        private const val TAG = "ScreenModelObject"
    }

    val videoConsumer = AgoraRawVideoConsumer()

    private var screenEntity: Int = 0

    private lateinit var materialInstance: MaterialInstance

    override fun onAssetSet() {
        super.onAssetSet()
        screenEntity = asset?.getFirstEntityByName("screen") ?: 0
        materialInstance = filamentContext?.materialHolder?.videoMaterial?.createInstance()!!
        materialInstance.setParameter("baseColor", Colors.RgbType.SRGB, 1.0f, 0.85f, 0.57f)
        materialInstance.setParameter("roughness", 0.3f)
        videoConsumer.filamentEngine = filamentContext?.engine
        videoConsumer.filamentMaterial = materialInstance
        setMaterial()
    }

    private fun setMaterial() {
        filamentContext?.run {
            getRenderableManager().run {
                setMaterialInstanceAt(getInstance(screenEntity), 0, materialInstance)
            }
        }

    }

    override fun update(frameTimeNanos: Long) {
        super.update(frameTimeNanos)
        videoConsumer.updateStream()
    }
}