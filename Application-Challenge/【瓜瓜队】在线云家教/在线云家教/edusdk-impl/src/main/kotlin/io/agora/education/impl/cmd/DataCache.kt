package io.agora.education.impl.cmd

import io.agora.education.api.stream.data.EduStreamEvent
import io.agora.education.api.user.data.EduUserEvent
import io.agora.education.api.user.data.EduUserInfo

internal class DataCache {

    /**数据同步的第二阶段期间，发生改变的有效数据(是否有效取决于每条数据的updateTime)*/
    private val validOnlineUsersBySyncing = mutableListOf<EduUserInfo>()
    private val validModifiedUsersBySyncing = mutableListOf<EduUserEvent>()
    private val validOfflineUsersBySyncing = mutableListOf<EduUserEvent>()
    private val validAddedStreamsBySyncing = mutableListOf<EduStreamEvent>()
    private val validModifiedStreamsBySyncing = mutableListOf<EduStreamEvent>()
    private val validRemovedStreamsBySyncing = mutableListOf<EduStreamEvent>()

    /**添加有效数据*/
    fun addValidDataBySyncing(validDatas: Array<MutableList<Any>>) {
        validOnlineUsersBySyncing.addAll(validDatas[0] as MutableList<EduUserInfo>)
        validModifiedUsersBySyncing.addAll(validDatas[1] as MutableList<EduUserEvent>)
        validOfflineUsersBySyncing.addAll(validDatas[2] as MutableList<EduUserEvent>)
        validAddedStreamsBySyncing.addAll(validDatas[3] as MutableList<EduStreamEvent>)
        validModifiedStreamsBySyncing.addAll(validDatas[4] as MutableList<EduStreamEvent>)
        validRemovedStreamsBySyncing.addAll(validDatas[5] as MutableList<EduStreamEvent>)
    }

    /***/
    fun getValidDataArray(): Array<MutableList<out Any>> {
        return arrayOf(validOnlineUsersBySyncing, validModifiedUsersBySyncing,
                validOfflineUsersBySyncing, validAddedStreamsBySyncing,
                validModifiedStreamsBySyncing, validRemovedStreamsBySyncing)
    }

    fun clearValidDataArray() {
        /**每次增量数据回调完成，都要清空集合*/
        validOnlineUsersBySyncing.clear()
        validModifiedUsersBySyncing.clear()
        validOfflineUsersBySyncing.clear()
        validAddedStreamsBySyncing.clear()
        validModifiedStreamsBySyncing.clear()
        validRemovedStreamsBySyncing.clear()
    }
}