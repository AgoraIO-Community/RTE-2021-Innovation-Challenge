package com.kangraoo.basektlib.ui.impl

import java.lang.Exception

sealed class PageStateActionEvent {
    object RefreshCompletedState : PageStateActionEvent()
    object LoadMoreCompletedState : PageStateActionEvent()
    object EmptyPageState : PageStateActionEvent()
    object LastDataState : PageStateActionEvent()
    class MoreLoadFailState(val e: Exception) : PageStateActionEvent()
    class LoadFailState(val e: Exception) : PageStateActionEvent()
    class DataState<T>(val data: List<T>, val isRefreshLast: Boolean) : PageStateActionEvent()
}

interface IBasePageView<T> {
    fun refreshCompleted()
    fun loadMoreCompleted()
    fun emptyPage()
    fun setData(data: List<T>, isRefreshLast: Boolean)
    fun lastData()
    fun moreLoadFail(e: Exception)
    fun onLoadFail(e: Exception)
}
