package com.hustunique.vlive.filament.model_object

import android.util.Log
import androidx.annotation.MainThread
import com.google.android.filament.gltfio.FilamentAsset
import com.google.android.filament.utils.*
import com.hustunique.vlive.data.Quaternion
import com.hustunique.vlive.data.Vector3
import com.hustunique.vlive.filament.FilamentContext
import com.hustunique.vlive.filament.FilamentLocalController
import com.hustunique.vlive.local.CharacterProperty

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/3/21
 */
abstract class FilamentBaseModelObject(val resourcePath: String) {

    companion object {
        private const val TAG = "FilamentBaseModelObject"
    }

    var asset: FilamentAsset? = null
        set(value) {
            field = value
            if (value != null) {
                onAssetSet()
            }
        }

    protected var filamentContext: FilamentContext? = null
    protected var property: CharacterProperty? = null

    abstract fun onAssetSet()

    abstract fun update(frameTimeNanos: Long)

    fun bindToContext(context: FilamentContext) {
        filamentContext = context
    }

    fun destroy() {
        asset = null
        filamentContext = null
    }

    fun onProperty(property: CharacterProperty) {
        this.property = property
    }

    private fun transformToCube(centerPoint: Float3 = FilamentLocalController.kDefaultObjectPosition) {
        if (filamentContext == null) {
            throw IllegalStateException("do not attached to a engine")
        }
        asset?.let { asset ->
            val tm = filamentContext!!.getTransformManager()
            var center = asset.boundingBox.center.let { v -> Float3(v[0], v[1], v[2]) }
            val halfExtent = asset.boundingBox.halfExtent.let { v -> Float3(v[0], v[1], v[2]) }
            Log.i(
                TAG,
                "transformToCube: ${halfExtent.x} ${halfExtent.y} ${halfExtent.z} $resourcePath"
            )
            Log.i(TAG, "transformToCube: ${center.x} ${center.y} ${center.z} $resourcePath")
            val maxExtent = 2.0f * max(halfExtent)
            val scaleFactor = 2.0f / maxExtent
            center -= centerPoint / scaleFactor
            val transform = scale(Float3(scaleFactor)) * translation(-center)
            tm.setTransform(tm.getInstance(asset.root), transpose(transform).toFloatArray())
        }
    }

    @MainThread
    open fun getTransform(): Pair<Vector3, Quaternion> {
        val buffer = property?.objectData
        return if (buffer != null) {
            buffer.rewind()
            val q = Quaternion.readFromBuffer(buffer)
            val v = Vector3.readFromBuffer(buffer)
            Pair(v, q)
        } else {
            Pair(Vector3(), Quaternion())
        }
    }
}