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
import com.hustunique.vlive.util.ThreadUtil
import java.nio.FloatBuffer

class VCPropertyProviderWithMLKit(
    private val activity: AppCompatActivity,
    onPropertyReady: (CharacterProperty) -> Unit,
) {
    private val emptyFaceBuffer = FloatBuffer.allocate(4)
    private val emptyObjectBuffer = FloatBuffer.allocate(7)

    private val mlKit = MLKitController { f1, f2, f3 ->
        ThreadUtil.execUi {
            Log.i(TAG, "$f1 $f2 $f3")
            onPropertyReady(CharacterProperty(f1, f2, f3, emptyFaceBuffer, emptyObjectBuffer))
        }
    }

    private val lensFacing = CameraSelector.LENS_FACING_FRONT
    private val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var analysisUseCase: ImageAnalysis

    init {
        ViewModelProvider(activity, ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application))
            .get(CameraXViewModel::class.java)
            .cameraProviderLiveData
            .observe(activity) { provider ->
                cameraProvider = provider
                bindUseCase()
            }
    }

    private fun bindUseCase() {
        cameraProvider?.let {
            it.unbindAll()

            analysisUseCase = ImageAnalysis.Builder()
                .setTargetResolution(HyperParam.targetResolution)
                .build()

            analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(activity)) { imageProxy ->
                mlKit.onImageAvailable(imageProxy)
            }

            it.bindToLifecycle(activity, cameraSelector, analysisUseCase)
        }
    }

    fun resume() {
        bindUseCase()
    }

    fun pause() {

    }

    fun destroy() {
        mlKit.stop()
        cameraProvider?.unbindAll()
    }

    companion object {
        private const val TAG = "VCPropertyProviderWithMLKit"
    }
}