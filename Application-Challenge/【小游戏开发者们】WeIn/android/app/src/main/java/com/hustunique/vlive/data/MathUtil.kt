package com.hustunique.vlive.data

object MathUtil {
    fun packRotationAndPosT(q: Quaternion, p: Vector3, R: FloatArray) {
        // 0 1 2
        // 3 4 5
        // 6 7 8
        val q0 = q.a
        val q1 = q.n.x
        val q2 = q.n.y
        val q3 = q.n.z

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
        R[4] = q1_q2 - q3_q0
        R[8] = q1_q3 + q2_q0

        R[1] = q1_q2 + q3_q0
        R[5] = 1 - sq_q1 - sq_q3
        R[9] = q2_q3 - q1_q0

        R[2] = q1_q3 - q2_q0
        R[6] = q2_q3 + q1_q0
        R[10] = 1 - sq_q1 - sq_q2

        R[3] = 0.0f
        R[7] = 0.0f
        R[11] = 0.0f

        R[12] = p.x
        R[13] = p.y
        R[14] = p.z
        R[15] = 1.0f
    }

    fun packPRST(p: Vector3, q: Quaternion, s: Vector3, R: FloatArray) {
        packRotationAndPosT(q, p, R)
        R[0] *= s.x
        R[5] *= s.y
        R[10] *= s.z
    }
}