package com.hustunique.vlive.data

class Transformation {
    /**
     * /  R[ 0]   R[ 1]   R[ 2]   0  \
     * |  R[ 4]   R[ 5]   R[ 6]   0  |
     * |  R[ 8]   R[ 9]   R[10]   0  |
     * \  T[0]    T[1]    T[2]    1  /
     * */
    val data = FloatArray(16).apply {
        this[0] = 1f
        this[5] = 1f
        this[10] = 1f
        this[15] = 1f
    }

    fun setRotation(rotationMatrix: FloatArray) {
        data[0] = rotationMatrix[0]
        data[1] = rotationMatrix[1]
        data[2] = rotationMatrix[2]

        data[4] = rotationMatrix[3]
        data[5] = rotationMatrix[4]
        data[6] = rotationMatrix[5]

        data[8] = rotationMatrix[6]
        data[9] = rotationMatrix[7]
        data[10] = rotationMatrix[8]
    }

    fun setTransformation(v: Vector3) {
        data[12] = v.x
        data[13] = v.y
        data[14] = v.z
    }
}