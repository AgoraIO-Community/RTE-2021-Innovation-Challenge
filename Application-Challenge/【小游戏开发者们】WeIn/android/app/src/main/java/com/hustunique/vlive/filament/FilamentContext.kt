package com.hustunique.vlive.filament

import android.opengl.EGLContext
import android.view.Surface
import android.view.SurfaceView
import com.google.android.filament.*
import com.google.android.filament.android.DisplayHelper
import com.google.android.filament.android.UiHelper
import com.google.android.filament.utils.KtxLoader
import com.google.android.filament.utils.Utils
import com.hustunique.vlive.filament.model_object.FilamentBaseModelObject
import java.nio.Buffer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/2/21
 */
class FilamentContext(private val surfaceView: SurfaceView, sharedContext: EGLContext? = null) {

    companion object {
        init {
            Utils.init()
        }

        private const val kNearPlane = 0.05     // 5 cm
        private const val kFarPlane = 1000.0    // 1 km
    }

    val engine = if (sharedContext == null) Engine.create() else Engine.create(sharedContext)

    private val displayHelper = DisplayHelper(surfaceView.context)

    private val uiHelper: UiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
        renderCallback = SurfaceCallback()
        attachTo(surfaceView)
    }

    var cameraFocalLength = 35f
        set(value) {
            field = value
            updateCameraProjection()
        }


    private val scene: Scene = engine.createScene()
    val camera: Camera = engine.createCamera()
    val view: View = engine.createView().apply {
        scene = this@FilamentContext.scene
        camera = this@FilamentContext.camera

        dynamicResolutionOptions = dynamicResolutionOptions.apply {
            enabled = true
        }
        ambientOcclusionOptions = ambientOcclusionOptions.apply {
            enabled = true
        }
        bloomOptions = bloomOptions.apply {
            enabled = true
        }
    }
    val renderer: Renderer = engine.createRenderer()

    private val resourceHolder =
        FilamentResourceHolder(engine, scene::addEntities, scene::removeEntities)

    val materialHolder = FilamentMaterialHolder(engine)

    val swapChainList: MutableList<SwapChain> = mutableListOf()
    private var displaySwapChain: SwapChain? = null

    @Entity
    val light: Int = EntityManager.get().create()

    init {
        val (r, g, b) = Colors.cct(6_500.0f)
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(r, g, b)
            .intensity(100_000.0f)
            .direction(0.0f, -1.0f, 0.0f)
            .castShadows(true)
            .build(engine, light)

        scene.addEntity(light)
    }

    fun loadGlb(buffer: Buffer) = resourceHolder.loadResource(buffer)

    fun render(frameTimeNanos: Long) {
        if (!uiHelper.isReadyToRender) {
            return
        }
        resourceHolder.update()

        swapChainList.forEach {
            if (renderer.beginFrame(it, frameTimeNanos)) {
                renderer.render(view)
                renderer.endFrame()
            }
        }
    }

    fun setIndirectLight(buffer: Buffer) {
//        scene.indirectLight = IndirectLight.Builder().build(engine)
        scene.indirectLight = KtxLoader.createIndirectLight(engine, buffer).apply {
            intensity = 10_000.0f
        }
    }

    fun setSkyBox(buffer: Buffer) {
        scene.skybox = KtxLoader.createSkybox(engine, buffer)
    }

    fun removeObj(obj: FilamentBaseModelObject) {
        obj.asset?.run(resourceHolder::removeAsset)
        obj.destroy()
    }

    fun getTransformManager() = engine.transformManager

    fun getRenderableManager() = engine.renderableManager

    private fun releaseResource() {
        resourceHolder.destroy()
        engine.run {
            destroyEntity(light)
            destroyView(view)
            scene.indirectLight?.let { destroyIndirectLight(it) }
            scene.skybox?.let { destroySkybox(it) }
            destroyScene(scene)
            destroyCamera(camera)
        }

        EntityManager.get().apply {
            destroy(light)
        }

        engine.destroy()
    }

    private fun updateCameraProjection() {
        val width = view.viewport.width
        val height = view.viewport.height
        val aspect = width.toDouble() / height.toDouble()
        camera.setLensProjection(cameraFocalLength.toDouble(), aspect, kNearPlane, kFarPlane)
    }


    fun release() {
        releaseResource()
    }


    inner class SurfaceCallback : UiHelper.RendererCallback {
        override fun onNativeWindowChanged(surface: Surface) {
            displaySwapChain?.let {
                swapChainList.remove(it)
                engine.destroySwapChain(it)
            }
            displaySwapChain = engine.createSwapChain(surface).apply {
                swapChainList.add(this)
            }
            surfaceView.let { displayHelper.attach(renderer, it.display) }
        }

        override fun onDetachedFromSurface() {
            displayHelper.detach()
            displaySwapChain?.let {
                swapChainList.remove(it)
                engine.destroySwapChain(it)
            }
            displaySwapChain = null
            engine.flushAndWait()
        }

        override fun onResized(width: Int, height: Int) {
            view.viewport = Viewport(0, 0, width, height)
            updateCameraProjection()
        }
    }
}