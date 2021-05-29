package com.hustunique.vlive.filament

import android.util.Log
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.FilamentAsset
import com.google.android.filament.gltfio.MaterialProvider
import com.google.android.filament.gltfio.ResourceLoader
import java.nio.Buffer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/2/21
 */
class FilamentResourceHolder(
    private val engine: Engine,
    private val onEntityReady: (IntArray) -> Unit,
    private val onEntityRemoved: (IntArray) -> Unit
) {

    companion object {
        private const val TAG = "FilamentResourceHolder"
    }

    var normalizeSkinningWeights = true
    var recomputeBoundingBoxes = false

    private val readyRenderables = IntArray(128) // add up to 128 entities at a time

    private val assetLoader = AssetLoader(engine, MaterialProvider(engine), EntityManager.get())
    private val resourceLoader =
        ResourceLoader(engine, normalizeSkinningWeights, recomputeBoundingBoxes)

    private val assetList = mutableListOf<FilamentAsset>()

    fun update() {
        resourceLoader.asyncUpdateLoad()
        populateScene()
    }

    fun loadResource(buffer: Buffer): FilamentAsset? =
        assetLoader.createAssetFromBinary(buffer)?.apply {
            resourceLoader.asyncBeginLoad(this)
            assetList.add(this)
            releaseSourceData()
        }


    private fun populateScene() {
        var count = 0
        val popRenderables = {
            count = 0
            assetList.forEach {
                if (count == 0) {
                    count = it.popRenderables(readyRenderables)
                }
            }
            count != 0
        }
        while (popRenderables()) {
            onEntityReady(readyRenderables.take(count).toIntArray())
        }
    }

    fun removeAsset(asset: FilamentAsset) {
        Log.i(TAG, "removeAsset: ")
        assetList.remove(asset)
        onEntityRemoved(asset.entities)
        assetLoader.destroyAsset(asset)
    }

    fun destroy() {
        resourceLoader.run {
            asyncCancelLoad()
            evictResourceData()
            destroy()
        }
        assetLoader.run {
            assetList.forEach {
                onEntityRemoved(it.entities)
                destroyAsset(it)
            }
            destroy()
        }
    }

}