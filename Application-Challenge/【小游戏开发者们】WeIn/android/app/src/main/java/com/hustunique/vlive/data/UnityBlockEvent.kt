package com.hustunique.vlive.data

import androidx.annotation.CallSuper
import java.nio.ByteBuffer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/27
 */

data class EventWrapper(val eventType: Byte, val dataList: List<BaseEvent>) {

    companion object {

        const val ADD_TYPE: Byte = 0
        const val DELETE_TYPE: Byte = 1


        fun unWrap(byteArray: ByteArray): EventWrapper = let {
            val buffer = ByteBuffer.wrap(byteArray)
            val type = buffer.get()
            val dataList: List<BaseEvent> = when (type) {
                ADD_TYPE -> mutableListOf<BaseEvent>().apply {
                    while (buffer.hasRemaining()) {
                        add(AddEvent.unWrap(buffer))
                    }
                }
                DELETE_TYPE -> mutableListOf<BaseEvent>().apply {
                    while (buffer.hasRemaining()) {
                        add(DeleteEvent.unWrap(buffer))
                    }
                }
                else -> listOf()
            }
            EventWrapper(type, dataList)
        }
    }

    fun toByteArray(): ByteArray = let {
        val listSize = dataList.sumBy { it.size }
        ByteBuffer.allocate(1 + listSize).also {
            it.put(eventType)
            dataList.forEach(it::put)
        }.array()
    }

}

abstract class BaseEvent(val size: Int) {


    abstract val id: Short

    @CallSuper
    open fun toByteArray(buffer: ByteBuffer) {
        buffer.putShort(id)
    }
}

data class AddEvent(
    override val id: Short,
    val position: Vector3,
    val rotation: Quaternion,
    val scale: Vector3,
    val type: Byte
) : BaseEvent(43) {

    companion object {
        fun unWrap(buffer: ByteBuffer): AddEvent =
            buffer.run { AddEvent(short, vector3, quaternion, vector3, get()) }
    }

    override fun toByteArray(buffer: ByteBuffer) {
        super.toByteArray(buffer)
        position.toByteArray(buffer)
        rotation.toByteArray(buffer)
        scale.toByteArray(buffer)
        buffer.put(type)
    }

}

data class DeleteEvent(override val id: Short) : BaseEvent(2) {

    companion object {
        fun unWrap(buffer: ByteBuffer): DeleteEvent = DeleteEvent(buffer.short)
    }

}

private fun Vector3.toByteArray(buffer: ByteBuffer) {
    buffer.run {
        putFloat(x)
        putFloat(y)
        putFloat(z)
    }
}

private fun Vector3.Companion.unWrap(buffer: ByteBuffer): Vector3 =
    Vector3(buffer.float, buffer.float, buffer.float)

private fun Quaternion.toByteArray(buffer: ByteBuffer) {
    n.toByteArray(buffer)
    buffer.putFloat(a)
}

private fun Quaternion.Companion.unWrap(buffer: ByteBuffer): Quaternion =
    Quaternion(Vector3.unWrap(buffer), buffer.float)

private fun ByteBuffer.put(event: BaseEvent) {
    event.toByteArray(this)
}

private val ByteBuffer.vector3: Vector3
    get() = Vector3.unWrap(this)

private val ByteBuffer.quaternion: Quaternion
    get() = Quaternion.unWrap(this)
