package com.hustunique.vlive.local

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.hustunique.vlive.CameraXViewModel
import com.hustunique.vlive.util.HyperParam

class CameraFrameProvider(
    private val context: AppCompatActivity,
    private val localVideoSink: LocalVideoSink,
) {
    private val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
        .build()
    private var analysisUseCase: ImageAnalysis? = null

    private var cameraProvider: ProcessCameraProvider? = null

    init {
        require(localVideoSink.getConsumeType() == LocalVideoType.REALITY)

        ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory.getInstance(context.application)
        )
            .get(CameraXViewModel::class.java)
            .cameraProviderLiveData
            .observe(context) { provider ->
                provider.unbindAll()
                cameraProvider = provider
                bindAnalysisUseCase()
            }
    }

    private fun bindAnalysisUseCase() {
        analysisUseCase = ImageAnalysis.Builder()
            .setTargetResolution(HyperParam.targetResolution)
            .build()
            .apply {
                setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    Log.i(TAG, "bindAnalysisUseCase: Analyze callback")
                    imageProxy.image?.let {
                        localVideoSink.onFrame(it)
                    }
                    imageProxy.close()
                }
            }

        cameraProvider?.bindToLifecycle(context, cameraSelector, analysisUseCase)
    }

    fun destroy() {
        cameraProvider?.unbindAll()
    }

    companion object {
        private const val TAG = "CameraFrameProvider"
    }
}