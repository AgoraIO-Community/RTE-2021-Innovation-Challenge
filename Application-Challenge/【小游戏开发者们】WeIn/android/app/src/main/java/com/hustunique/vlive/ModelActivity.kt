package com.hustunique.vlive

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.media.ImageReader
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.*
import com.google.android.filament.utils.KtxLoader
import com.google.android.filament.utils.Utils
import com.hustunique.vlive.agora.AgoraModule
import com.hustunique.vlive.agora.BufferSource
import com.hustunique.vlive.databinding.ActivityModelBinding
import com.hustunique.vlive.filament.ModelViewer
import com.hustunique.vlive.util.readCompressedAsset
import com.hustunique.vlive.util.readUncompressedAsset
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class ModelActivity : AppCompatActivity() {

    companion object {
        init {
            Utils.init()
        }

        private const val TAG = "MainActivity"
    }

    private lateinit var choreographer: Choreographer
    private val frameScheduler = FrameCallback()
    private val binding by lazy { ActivityModelBinding.inflate(layoutInflater) }

    private lateinit var modelViewer: ModelViewer

    //    private lateinit var videoSource: IVideoSource
    private lateinit var videoSource: BufferSource
    private lateinit var agoraModule: AgoraModule
    private var bm = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var reader: ImageReader? = null

//    private lateinit var arCoreHelper: ARCoreController

    private lateinit var material: Material
    private lateinit var materialInstance: MaterialInstance
    private lateinit var cameraBgHelper: CameraBgHelper

    private lateinit var vertexBuffer: VertexBuffer
    private lateinit var indexBuffer: IndexBuffer

    // Filament entity representing a renderable object
    @Entity
    private var renderable = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        choreographer = Choreographer.getInstance()

        modelViewer = ModelViewer(binding.mainSv)

        binding.mainSv.setOnTouchListener { _, event ->
            modelViewer.onTouchEvent(event)
            true
        }

        binding.mainSv.post {
            reader = ImageReader.newInstance(
                binding.mainSv.width,
                binding.mainSv.height,
                PixelFormat.RGBA_8888,
                2
            )
//            modelViewer.addRenderTarget(reader!!.surface)
//            val mask =  0xf.inv()
//            videoSource =
//                FilamentTextureSource((binding.mainSv.width and mask), (binding.mainSv.height and mask), modelViewer)
////            FilamentTextureSource(360, 640, modelViewer)
            videoSource = BufferSource()
//            agoraModule = AgoraModule(this).apply {
//                val view = initAgora(videoSource)
//                binding.frameContainer.addView(view)
//            }
            reader!!.setOnImageAvailableListener({
                val i = it.acquireNextImage()
                val pixelStride: Int = i.planes[0].pixelStride
                val rowStride: Int = i.planes[0].rowStride
                val rowPadding: Int = rowStride - pixelStride * i.width
                val buffer = i.planes[0].buffer
                val w = i.width + rowPadding / pixelStride
//                bm = Bitmap.createBitmap(
//                    i.width + rowPadding / pixelStride, i.height,
//                    Bitmap.Config.ARGB_8888
//                )
//                bm.copyPixelsFromBuffer(buffer)
                Log.i(TAG, "onFrame: $w, ${i.height}")
                videoSource.onBuffered(buffer, w, i.height)

                i.close()
            }, null)
        }

        createRenderables()
        createIndirectLight()
        setupScene()

        cameraBgHelper = CameraBgHelper(
            modelViewer.engine,
            materialInstance,
            windowManager.defaultDisplay
        ).apply {
            initHelper()
        }

        val dynamicResolutionOptions = modelViewer.view.dynamicResolutionOptions
        dynamicResolutionOptions.enabled = true
        modelViewer.view.dynamicResolutionOptions = dynamicResolutionOptions

        val ssaoOptions = modelViewer.view.ambientOcclusionOptions
        ssaoOptions.enabled = true
        modelViewer.view.ambientOcclusionOptions = ssaoOptions

        val bloomOptions = modelViewer.view.bloomOptions
        bloomOptions.enabled = true
        modelViewer.view.bloomOptions = bloomOptions

//        arCoreHelper = ARCoreController(this, Handler(), cameraBgHelper.surface)
    }

    private fun createRenderables() {
        val buffer = assets.open("models/room.glb").use { input ->
//            val buffer = assets.open("models/nfface.glb").use { input ->
            val bytes = ByteArray(input.available())
            input.read(bytes)
            Log.i(TAG, "createRenderables: ${bytes.size}")
            ByteBuffer.wrap(bytes)
        }

        modelViewer.loadModelGlb(buffer)
//        modelViewer.transformToUnitCube()
    }

    private fun createIndirectLight() {
        val engine = modelViewer.engine

        val scene = modelViewer.scene
        val ibl = "default_env"
        readCompressedAsset("envs/$ibl/${ibl}_ibl.ktx").let {
            scene.indirectLight = KtxLoader.createIndirectLight(engine, it)
            scene.indirectLight!!.intensity = 30_000.0f
        }
        readCompressedAsset("envs/$ibl/${ibl}_skybox.ktx").let {
            scene.skybox = KtxLoader.createSkybox(engine, it)
        }
    }

    private fun setupScene() {
        loadMaterial()
        setupMaterial()
        createMesh()

        // To create a renderable we first create a generic entity
        renderable = EntityManager.get().create()

        // We then create a renderable component on that entity
        // A renderable is made of several primitives; in this case we declare only 1
        // If we wanted each face of the cube to have a different material, we could
        // declare 6 primitives (1 per face) and give each of them a different material
        // instance, setup with different parameters
        RenderableManager.Builder(1)
            // Overall bounding box of the renderable
            .boundingBox(Box(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f))
            // Sets the mesh data of the first primitive, 6 faces of 6 indices each
            .geometry(
                0,
                RenderableManager.PrimitiveType.TRIANGLES,
                vertexBuffer,
                indexBuffer,
                0,
                6 * 6
            )
            // Sets the material of the first primitive
            .material(0, materialInstance)
            .build(modelViewer.engine, renderable)

        // Add the entity to the scene to render it
        modelViewer.scene.addEntity(renderable)
    }


    override fun onResume() {
        super.onResume()
//        arCoreHelper.resume()
        choreographer.postFrameCallback(frameScheduler)
    }

    override fun onPause() {
        super.onPause()
//        arCoreHelper.pause()
        choreographer.removeFrameCallback(frameScheduler)
    }

    override fun onDestroy() {
        super.onDestroy()
//        arCoreHelper.release()
        choreographer.removeFrameCallback(frameScheduler)
        modelViewer.destroyModel()
    }

    var lEyeEntity: Int = 0
    var rEyeEntity: Int = 0
    var mouthEntity: Int = 0

    inner class FrameCallback : Choreographer.FrameCallback {
        private val startTime = System.nanoTime()

        private fun checkEntity() {

            if (lEyeEntity == 0) {
                lEyeEntity =
                    modelViewer.asset?.getFirstEntityByName("HyperNURBS_6C4dObjectSymmetry_6Polygon")
                        ?: 0
            }
//            if (rEyeEntity == 0) {
//                rEyeEntity = modelViewer.asset?.getFirstEntityByName("reye") ?: 0
//            }
            if (mouthEntity == 0) {
                mouthEntity = modelViewer.asset?.getFirstEntityByName("HyperNURBS") ?: 0
            }
        }

        private fun setAnim(frameTimeNanos: Long) {
            val elapsedTimeSeconds = (frameTimeNanos - startTime).toDouble() / 1_000_000_000
            modelViewer.engine.renderableManager.setMorphWeights(
                modelViewer.engine.renderableManager.getInstance(lEyeEntity),
                floatArrayOf(
                    abs(sin(elapsedTimeSeconds * Math.PI)).toFloat(),
                    abs(cos(elapsedTimeSeconds * Math.PI)).toFloat(),
                    0f,
                    0f
                )
            )
//            modelViewer.engine.renderableManager.setMorphWeights(
//                modelViewer.engine.renderableManager.getInstance(rEyeEntity),
//                floatArrayOf(abs(cos(elapsedTimeSeconds * Math.PI)).toFloat(), 0f, 0f, 0f)
//            )
            modelViewer.engine.renderableManager.setMorphWeights(
                modelViewer.engine.renderableManager.getInstance(mouthEntity),
                floatArrayOf(abs(sin(elapsedTimeSeconds * Math.PI)).toFloat(), 0f, 0f, 0f)
            )
        }

        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)

            checkEntity()

            modelViewer.animator?.apply {
                val elapsedTimeSeconds = (frameTimeNanos - startTime).toDouble() / 1_000_000_000
                if (animationCount > 0) {
                    for (i in 0 until animationCount) {
                        applyAnimation(i, elapsedTimeSeconds.toFloat())
//                        Log.i(TAG, "doFrame: ${getAnimationName(i)}")
                    }
                }
                updateBoneMatrices()
            }
            setAnim(frameTimeNanos)

            val entity = modelViewer.asset?.root ?: 0
            if (entity != 0) {
//                val instance = modelViewer.engine.transformManager.getInstance(entity)
//                modelViewer.engine.transformManager.setTransform(
//                    instance,
//                    arCoreHelper.objectMatrix
//                )
            }

//            modelViewer.camera.lookAt(0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0)
            cameraBgHelper.pushExternalImageToFilament()
            modelViewer.render(frameTimeNanos)
        }
    }


    private fun createMesh() {
        val floatSize = 4
        val shortSize = 2
        // A vertex is a position + a tangent frame:
        // 3 floats for XYZ position, 4 floats for normal+tangents (quaternion)
        val vertexSize = 3 * floatSize + 4 * floatSize

        // Define a vertex and a function to put a vertex in a ByteBuffer
        @Suppress("ArrayInDataClass")
        data class Vertex(val x: Float, val y: Float, val z: Float, val tangents: FloatArray)

        fun ByteBuffer.put(v: Vertex): ByteBuffer {
            putFloat(v.x)
            putFloat(v.y)
            putFloat(v.z)
            v.tangents.forEach { putFloat(it) }
            return this
        }

        // 6 faces, 4 vertices per face
        val vertexCount = 6 * 4

        // Create tangent frames, one per face
        val tfPX = FloatArray(4)
        val tfNX = FloatArray(4)
        val tfPY = FloatArray(4)
        val tfNY = FloatArray(4)
        val tfPZ = FloatArray(4)
        val tfNZ = FloatArray(4)

        MathUtils.packTangentFrame(0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, tfPX)
        MathUtils.packTangentFrame(0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, -1.0f, 0.0f, 0.0f, tfNX)
        MathUtils.packTangentFrame(-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, tfPY)
        MathUtils.packTangentFrame(-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, tfNY)
        MathUtils.packTangentFrame(0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, tfPZ)
        MathUtils.packTangentFrame(0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, tfNZ)

        val vertexData = ByteBuffer.allocate(vertexCount * vertexSize)
            // It is important to respect the native byte order
            .order(ByteOrder.nativeOrder())
            // Face -Z
            .put(Vertex(-1.5f, -1.5f, -1.0f, tfNZ))
            .put(Vertex(-1.5f, 1.5f, -1.0f, tfNZ))
            .put(Vertex(1.5f, 1.5f, -1.0f, tfNZ))
            .put(Vertex(1.5f, -1.5f, -1.0f, tfNZ))
            // Face +X
            .put(Vertex(1.5f, -1.5f, -1.0f, tfPX))
            .put(Vertex(1.5f, 1.5f, -1.0f, tfPX))
            .put(Vertex(1.0f, 1.0f, 1.0f, tfPX))
            .put(Vertex(1.0f, -1.0f, 1.0f, tfPX))
            // Face +Z
            .put(Vertex(-1.0f, -1.0f, 1.0f, tfPZ))
            .put(Vertex(1.0f, -1.0f, 1.0f, tfPZ))
            .put(Vertex(1.0f, 1.0f, 1.0f, tfPZ))
            .put(Vertex(-1.0f, 1.0f, 1.0f, tfPZ))
            // Face -X
            .put(Vertex(-1.0f, -1.0f, 1.0f, tfNX))
            .put(Vertex(-1.0f, 1.0f, 1.0f, tfNX))
            .put(Vertex(-1.5f, 1.5f, -1.0f, tfNX))
            .put(Vertex(-1.5f, -1.5f, -1.0f, tfNX))
            // Face -Y
            .put(Vertex(-1.0f, -1.0f, 1.0f, tfNY))
            .put(Vertex(-1.5f, -1.5f, -1.0f, tfNY))
            .put(Vertex(1.5f, -1.5f, -1.0f, tfNY))
            .put(Vertex(1.0f, -1.0f, 1.0f, tfNY))
            // Face +Y
            .put(Vertex(-1.5f, 1.5f, -1.0f, tfPY))
            .put(Vertex(-1.0f, 1.0f, 1.0f, tfPY))
            .put(Vertex(1.0f, 1.0f, 1.0f, tfPY))
            .put(Vertex(1.5f, 1.5f, -1.0f, tfPY))
            // Make sure the cursor is pointing in the right place in the byte buffer
            .flip()

        // Declare the layout of our mesh
        vertexBuffer = VertexBuffer.Builder()
            .bufferCount(1)
            .vertexCount(vertexCount)
            // Because we interleave position and color data we must specify offset and stride
            // We could use de-interleaved data by declaring two buffers and giving each
            // attribute a different buffer index
            .attribute(
                VertexBuffer.VertexAttribute.POSITION,
                0,
                VertexBuffer.AttributeType.FLOAT3,
                0,
                vertexSize
            )
            .attribute(
                VertexBuffer.VertexAttribute.TANGENTS,
                0,
                VertexBuffer.AttributeType.FLOAT4,
                3 * floatSize,
                vertexSize
            )
            .build(modelViewer.engine)

        // Feed the vertex data to the mesh
        // We only set 1 buffer because the data is interleaved
        vertexBuffer.setBufferAt(modelViewer.engine, 0, vertexData)

        // Create the indices
        val indexData = ByteBuffer.allocate(6 * 2 * 3 * shortSize)
            .order(ByteOrder.nativeOrder())
        repeat(6) {
            val i = (it * 4).toShort()
            indexData
                .putShort(i).putShort((i + 1).toShort()).putShort((i + 2).toShort())
                .putShort(i).putShort((i + 2).toShort()).putShort((i + 3).toShort())
        }
        indexData.flip()

        // 6 faces, 2 triangles per face,
        indexBuffer = IndexBuffer.Builder()
            .indexCount(vertexCount * 2)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(modelViewer.engine)
        indexBuffer.setBuffer(modelViewer.engine, indexData)
    }

    private fun loadMaterial() {
//        readCompressedAsset("materials/lit.filamat").let {
        readUncompressedAsset("materials/lit.filamat").let {
            material = Material.Builder().payload(it, it.remaining()).build(modelViewer.engine)
        }
    }

    private fun setupMaterial() {
        materialInstance = material.createInstance()
        materialInstance.setParameter("baseColor", Colors.RgbType.SRGB, 1.0f, 0.85f, 0.57f)
        materialInstance.setParameter("roughness", 0.3f)
    }

}

fun FloatArray.toMString(): String = fold("Matrix: ") { R, d ->
    "$R $d, "
}