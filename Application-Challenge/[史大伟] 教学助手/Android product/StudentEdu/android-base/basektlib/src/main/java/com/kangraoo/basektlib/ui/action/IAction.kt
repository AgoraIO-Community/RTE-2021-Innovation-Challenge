package com.kangraoo.basektlib.ui.action

import com.kangraoo.basektlib.ui.IBaseView

interface IAction<in T : IBaseView> {
    fun setBaseView(iBaseView: T)
    fun initAction()
    fun detach()
    fun resume()
    fun pause()
    fun getName(): String
}
