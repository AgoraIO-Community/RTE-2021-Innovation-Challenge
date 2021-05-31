package com.hustunique.vlive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.hustunique.vlive.databinding.ActivityCameraXBinding
import com.hustunique.vlive.processor.FaceDetectorProcessor
import com.hustunique.vlive.processor.GraphicOverlay
import com.hustunique.vlive.processor.VisionImageProcessor
import com.hustunique.vlive.util.HyperParam

class CameraXActivity : AppCompatActivity() {
    private lateinit var cameraXBinding: ActivityCameraXBinding

    private var cameraProvider: ProcessCameraProvider? = null
    private var imageProcessor: VisionImageProcessor? = null

    private lateinit var previewView: PreviewView
    private lateinit var graphicOverlay: GraphicOverlay

    private lateinit var previewUseCase: Preview
    private lateinit var analysisUseCase: ImageAnalysis
    private lateinit var cameraSelector: CameraSelector
    private var lensFacing = CameraSelector.LENS_FACING_FRONT

    private var needUpdateGraphicOverlayImageSourceInfo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraXBinding = ActivityCameraXBinding.inflate(layoutInflater)
        setContentView(cameraXBinding.root)

        initCamera()
    }

    override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()
        imageProcessor?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.stop()
        cameraProvider?.unbindAll()
    }

    private fun initCamera() {
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        previewView = cameraXBinding.cameraPreview
        graphicOverlay = cameraXBinding.cameraOverlay
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(CameraXViewModel::class.java)
            .cameraProviderLiveData
            .observe(this) { provider ->
                cameraProvider = provider
                bindAllCameraUseCases()
            }
    }

    private fun bindAllCameraUseCases() {
        Log.i(TAG, "bindAllCameraUseCases: $cameraProvider")
        if (cameraProvider != null) {
            cameraProvider!!.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        previewUseCase = Preview.Builder()
            .setTargetResolution(HyperParam.targetResolution)
            .build()
        previewUseCase.setSurfaceProvider(previewView.createSurfaceProvider())
        cameraProvider?.bindToLifecycle(this, cameraSelector, previewUseCase)
    }

    private fun bindAnalysisUseCase() {
        imageProcessor?.stop()

        val options = FaceDetectorOptions.Builder()
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setMinFaceSize(10f)
            .build()
        imageProcessor = FaceDetectorProcessor(this, options)

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetResolution(HyperParam.targetResolution)
            .build()

        needUpdateGraphicOverlayImageSourceInfo = true

        analysisUseCase.setAnalyzer(
            ContextCompat.getMainExecutor(this)
        ) { imageProxy ->
            if (needUpdateGraphicOverlayImageSourceInfo) {
                val isImageFlipped = CameraSelector.LENS_FACING_FRONT == lensFacing
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                if (rotationDegrees == 0 || rotationDegrees == 180) {
                    graphicOverlay.setImageSourceInfo(
                        imageProxy.width,
                        imageProxy.height,
                        isImageFlipped
                    )
                } else {
                    graphicOverlay.setImageSourceInfo(
                        imageProxy.height,
                        imageProxy.width,
                        isImageFlipped
                    )
                }
                needUpdateGraphicOverlayImageSourceInfo = false
            }
            imageProcessor?.processImageProxy(imageProxy, graphicOverlay)
        }
        cameraProvider?.bindToLifecycle(this, cameraSelector, analysisUseCase)
    }

    companion object {
        private const val TAG = "CameraXActivity"
    }
}