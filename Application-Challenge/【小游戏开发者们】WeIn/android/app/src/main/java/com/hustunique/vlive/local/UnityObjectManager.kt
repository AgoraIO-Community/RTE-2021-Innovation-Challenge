package com.hustunique.vlive.local

import android.util.SparseArray
import com.hustunique.vlive.data.AddEvent
import com.hustunique.vlive.data.BaseEvent
import com.hustunique.vlive.data.EventWrapper
import com.hustunique.vlive.filament.model_object.*
import com.hustunique.vlive.util.ThreadUtil
import com.hustunique.vlive.util.putIfAbsent

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/27
 */
class UnityObjectManager(
    private val addObj: (FilamentBaseModelObject) -> Unit,
    private val removeObj: (FilamentBaseModelObject) -> Unit
) {

    private val objList = SparseArray<FilamentBaseModelObject>()

    fun onEvent(event: EventWrapper) {
        if (event.eventType == EventWrapper.ADD_TYPE) {
            addAction(event.dataList)
        } else if (event.eventType == EventWrapper.DELETE_TYPE) {
            deleteAction(event.dataList)
        }
    }

    private fun addAction(list: List<BaseEvent>) {
        ThreadUtil.execUi {
            list.forEach {
                it as AddEvent
                (objList.get(it.id.toInt()) ?: objList.putIfAbsent(
                    it.id.toInt(),
                    when (it.type.toInt()) {
                        0 -> UnityCubeModelObject()
                        1 -> UnitySphereModelObject()
                        2 -> UnityCylinderModelObject()
                        3 -> UnityPyramidModelObject()
                        else -> UnityCubeModelObject()
                    }
                ) {
                    addObj(it)
                }).run {
                    (this as? UnityModelObject)?.setEventData(it)
                }
            }
        }
    }

    private fun deleteAction(list: List<BaseEvent>) {
        ThreadUtil.execUi {
            list.forEach {
                objList.get(it.id.toInt())?.let {
                    removeObj(it)
                }
                objList.remove(it.id.toInt())
            }
        }
    }
}