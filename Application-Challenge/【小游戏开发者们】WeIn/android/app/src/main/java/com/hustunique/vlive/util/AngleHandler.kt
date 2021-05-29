package com.hustunique.vlive.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventCallback
import android.hardware.SensorManager
import android.util.Log
import com.hustunique.vlive.data.Quaternion
import com.hustunique.vlive.data.Vector3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 3/26/21
 */
class AngleHandler(
    context: Context,
    private val firstCallback: (Quaternion) -> Unit,
) : SensorEventCallback() {

    companion object {
        private const val TAG = "AngelHandler"
    }

    private val rotationMode = DeviceRotationMode.LANDSCAPE

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    private val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val rotationVector = FloatArray(4)

    fun start() {
        sensorManager?.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        Log.i(TAG, "Angle handler start")
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
        Log.i(TAG, "Angle handler stop")
    }

    fun getRotationMatrix(matrix: FloatArray) {
        SensorManager.getRotationMatrixFromVector(matrix, rotationVector)
    }

    fun getRotation(): Quaternion {
        val x = rotationVector[0]
        val y = rotationVector[1]
        val z = rotationVector[2]
        val a = rotationVector[3]
        return Quaternion(Vector3(x, y, z), a).normalize() * getRotationByMode()
    }

    private var started = false

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ROTATION_VECTOR -> {
                event.values.copyInto(rotationVector, 0, 0, 4)
                if (!started) {
                    started = true
                    firstCallback(getRotation())
                }
            }
        }
    }

    enum class DeviceRotationMode {
        PORTRAIT,
        LANDSCAPE,
    }

    private fun getRotationByMode() = when (rotationMode) {
        DeviceRotationMode.PORTRAIT -> Quaternion()
        DeviceRotationMode.LANDSCAPE -> Quaternion(
            Vector3(0f, 0f, -sin(PI / 4).toFloat()),
            cos(-PI / 4).toFloat()
        )
    }
}