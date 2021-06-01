package com.hustunique.vlive.data

import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Vector3(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
) {
    operator fun plus(v: Vector3) = Vector3(x + v.x, y + v.y, z + v.z)

    operator fun plusAssign(v: Vector3) {
        x += v.x
        y += v.y
        z += v.z
    }

    operator fun plus(f: Float) = Vector3(x + f, y + f, z + f)

    operator fun plusAssign(f: Float) {
        x += f
        y += f
        z += f
    }

    operator fun minus(v: Vector3) = Vector3(x - v.x, y - v.y, z - v.z)

    operator fun minusAssign(v: Vector3) {
        x -= v.x
        y -= v.y
        z -= v.z
    }

    operator fun minus(f: Float) = Vector3(x - f, y - f, z - f)

    operator fun minusAssign(f: Float) {
        x -= f
        y -= f
        z -= f
    }

    operator fun unaryMinus() = Vector3(-x, -y, -z)

    operator fun times(f: Float) = Vector3(x * f, y * f, z * f)

    operator fun timesAssign(f: Float) {
        x *= f
        y *= f
        z *= f
    }

    operator fun times(v: Vector3) = Vector3(
        y * v.z - z * v.y,
        - x * v.z + z * v.x,
        x * v.y - y * v.x,
    )

    operator fun timesAssign(v: Vector3) {
        val x0 = y * v.z - z * v.y
        val y0 = - x * v.z + z * v.x
        val z0 = x * v.y - y * v.x
        x = x0
        y = y0
        z = z0
    }

    operator fun div(f: Float) = Vector3(
        x / f,
        y / f,
        z / f,
    )

    operator fun divAssign(f: Float) {
        x /= f
        y /= f
        z /= f
    }

    fun dot(other: Vector3) = x * other.x + y * other.y + z * other.z

    fun norm() = sqrt(x * x + y * y + z * z)

    fun normalized() = apply {
        val factor = norm()
        x /= factor
        y /= factor
        z /= factor
    }

    fun applyL(matrix: FloatArray) = apply {
        val tempX = x
        val tempY = y
        val tempZ = z
        x = matrix[0] * tempX + matrix[1] * tempY + matrix[2] * tempZ
        y = matrix[3] * tempX + matrix[4] * tempY + matrix[5] * tempZ
        z = matrix[6] * tempX + matrix[7] * tempY + matrix[8] * tempZ
    }

    fun clone() = Vector3(x, y, z)

    fun clone(v: Vector3) = apply {
        x = v.x
        y = v.y
        z = v.z
    }

    fun toRotationMatrix(data: FloatArray) = apply {
        val c1 = cos(x)
        val s1 = sin(x)
        val c2 = cos(y)
        val s2 = sin(y)
        val c3 = cos(z)
        val s3 = sin(z)
        data[0] = (c1 * c3 + s1 * s2 * s3)
        data[1] = (c3 * s1 * s2 - c1 * s3)
        data[2] = (c2 * s1)
        data[3] = (c2 * s3)
        data[4] = (c2 * c3)
        data[5] = (-s2)
        data[6] = (c1 * s2 * s3 - s1 * c3)
        data[7] = (s1 * s3 + c1 * c3 * s2)
        data[8] = (c1 * c2)
    }

    fun writeToBuffer(buffer: FloatBuffer) {
        buffer.put(x)
        buffer.put(y)
        buffer.put(z)
    }

    override fun toString() = "($x, $y, $z)"

    companion object {
        fun readFromBuffer(buffer: FloatBuffer) = Vector3(buffer.get(), buffer.get(), buffer.get())
    }
}