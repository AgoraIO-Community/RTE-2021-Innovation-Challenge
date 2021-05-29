package com.hustunique.vlive.data

import java.nio.FloatBuffer
import kotlin.math.sqrt

class Quaternion(
    val n: Vector3 = Vector3(),
    var a: Float = 1f,
) {
    operator fun plus(q: Quaternion) = Quaternion(
        n + q.n,
        a + q.a,
    )

    operator fun plusAssign(q: Quaternion) {
        n += q.n
        a += q.a
    }

    operator fun minus(q: Quaternion) = Quaternion(
        n - q.n,
        a - q.a,
    )

    operator fun minusAssign(q: Quaternion) {
        n -= q.n
        a -= q.a
    }

    operator fun unaryMinus() = Quaternion(-n, -a)

    operator fun times(q: Quaternion) = Quaternion(
        a = a * q.a - n.dot(q.n),
        n = n * q.n + q.n * a + n * q.a,
    )

    fun inverse() = apply {
        n *= -1f
    }

    fun normalize() = apply {
        val factor = sqrt(a * a + n.x * n.x + n.y * n.y + n.z * n.z)
        if (factor != 0f) {
            a /= factor
            n *= 1 / factor
        }
    }

    fun writeToBuffer(data: FloatBuffer) = apply {
        n.writeToBuffer(data)
        data.put(a)
    }

    fun toRotation(R: FloatArray) {
        val q0 = a
        val q1 = n.x
        val q2 = n.y
        val q3 = n.z
        val sq_q1 = 2 * q1 * q1
        val sq_q2 = 2 * q2 * q2
        val sq_q3 = 2 * q3 * q3
        val q1_q2 = 2 * q1 * q2
        val q3_q0 = 2 * q3 * q0
        val q1_q3 = 2 * q1 * q3
        val q2_q0 = 2 * q2 * q0
        val q2_q3 = 2 * q2 * q3
        val q1_q0 = 2 * q1 * q0

        R[0] = 1 - sq_q2 - sq_q3
        R[1] = q1_q2 - q3_q0
        R[2] = q1_q3 + q2_q0

        R[3] = q1_q2 + q3_q0
        R[4] = 1 - sq_q1 - sq_q3
        R[5] = q2_q3 - q1_q0

        R[6] = q1_q3 - q2_q0
        R[7] = q2_q3 + q1_q0
        R[8] = 1 - sq_q1 - sq_q2
    }

    fun clone() = Quaternion(n.clone(), a)

    fun clone(q1: Quaternion) = apply {
        a = q1.a
        n.x = q1.n.x
        n.y = q1.n.y
        n.z = q1.n.z
    }

    override fun toString() = "[$a, $n]"

    companion object {
        fun readFromBuffer(data: FloatBuffer) = Quaternion(
            Vector3.readFromBuffer(data),
            data.get()
        )
    }
}