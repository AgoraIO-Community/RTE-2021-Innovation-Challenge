package com.vmloft.develop.app.match.ui.main.home

import androidx.lifecycle.viewModelScope
import com.vmloft.develop.library.common.base.BViewModel
import com.vmloft.develop.library.common.request.RResult
import com.vmloft.develop.app.match.request.repository.MatchRepository
import com.vmloft.develop.library.common.common.CConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Create by lzan13 on 2020/4/20 17:28
 * 描述：匹配 ViewModel
 */
class MatchViewModel(private val repository: MatchRepository) : BViewModel() {

    /**
     * 提交匹配信息
     */
    fun submitMatch(content: String, emotion: Int, type: Int = 0) {
        viewModelScope.launch(Dispatchers.Main) {
            emitUIState(true)
            val result = repository.submitMatch(content, emotion, type)
            if (result is RResult.Success) {
                emitUIState(isSuccess = true, data = result.data, type = "submitMatch")
                return@launch
            } else if (result is RResult.Error) {
                emitUIState(code = result.code, error = result.error)
            }
        }
    }

    /**
     * 删除一条匹配
     */
    fun removeMatch(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            emitUIState(true)
            val result = repository.removeMatch(id)
            if (result is RResult.Success && result.data != null) {
                emitUIState(isSuccess = true, data = result.data, type = "removeMatch")
                return@launch
            } else if (result is RResult.Error) {
                emitUIState(code = result.code, error = result.error)
            }
        }
    }

    /**
     * 获取匹配列表
     */
    fun getMatchList(page: Int = CConstants.defaultPage, limit: Int = CConstants.defaultLimit, type: Int = 0) {
        viewModelScope.launch(Dispatchers.Main) {
            emitUIState(true)
            val result = repository.getMatchList(page, limit)
            if (result is RResult.Success) {
                emitUIState(isSuccess = true, data = result.data, type = "matchList")
                return@launch
            } else if (result is RResult.Error) {
                emitUIState(code = result.code, error = result.error)
            }
        }
    }

    /**
     * 随机获取一条匹配
     */
    fun getMatchOne(type: Int = 0) {
        viewModelScope.launch(Dispatchers.Main) {
            emitUIState(true)
            val result = withContext(Dispatchers.IO) {
                Thread.sleep(CConstants.timeSecond)
                repository.getMatchOne(type)
            }
            if (result is RResult.Success && result.data != null) {
                emitUIState(isSuccess = true, data = result.data, type = "matchOne")
                return@launch
            } else if (result is RResult.Error) {
                emitUIState(code = result.code, error = result.error)
            }
        }
    }

}