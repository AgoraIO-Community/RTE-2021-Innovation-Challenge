package com.kangraoo.basektlib.tools

import com.kangraoo.basektlib.app.*
import com.kangraoo.basektlib.app.SYS_DEBUG_CONSOLE
import com.kangraoo.basektlib.app.SYS_DEBUG_DATA_SELECT
import com.kangraoo.basektlib.app.SYS_DEBUG_STATUS
import com.kangraoo.basektlib.data.model.SelectModel
import com.kangraoo.basektlib.tools.store.MMKVStore
import com.kangraoo.basektlib.tools.store.MemoryStore
import com.tencent.mmkv.MMKV.MULTI_PROCESS_MODE

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/08/10
 * desc :
 */
class SSysStore private constructor() {

    val safeMmkvStore: MMKVStore = MMKVStore.instanceSafe(
        SApplication.instance().sConfiger.sysSharedpreferences, MULTI_PROCESS_MODE
    )
    val memoryStore: MemoryStore = MemoryStore.instance

    companion object {
        val instance: SSysStore by lazy {
            SSysStore()
        }
    }

    fun putSysDebugDataSelect(selectModel: SelectModel<*>) {
        safeMmkvStore.put(SYS_DEBUG_DATA_SELECT, selectModel)
    }

    val sysDebugDataSelect: SelectModel<*>?
        get() {
            if (safeMmkvStore.contains(SYS_DEBUG_DATA_SELECT)) {
                return safeMmkvStore.get<SelectModel<*>>(
                    SYS_DEBUG_DATA_SELECT,
                    null,
                    SelectModel::class.java
                )
            }
            return null
        }

    val sysDebugConsole: Boolean
        get() {
            return safeMmkvStore.get(SYS_DEBUG_CONSOLE, false, Boolean::class.java)!!
        }

    fun putSysDebugConsole(isChecked: Boolean) {
        safeMmkvStore.put(SYS_DEBUG_CONSOLE, isChecked)
    }

    fun putSysDebugStatus() {
        safeMmkvStore.put(SYS_DEBUG_STATUS, true)
    }

    fun putSysDebugDataList(appModels: List<SelectModel<*>>) {
        memoryStore.put(SYS_DEBUG_DATA_LIST, appModels)
    }
    fun sysDebugDataList(): List<SelectModel<*>>? {
        return memoryStore.get(SYS_DEBUG_DATA_LIST, null)
    }

    val sysDebugStatus: Boolean
        get() {
            return safeMmkvStore.get(
                SYS_DEBUG_STATUS,
                false,
                Boolean::class.java
            )!!
        }
}
