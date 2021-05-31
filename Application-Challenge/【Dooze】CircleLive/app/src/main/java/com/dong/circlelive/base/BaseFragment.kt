package com.dong.circlelive.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Create by dooze on 2021/5/9  3:48 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes), BaseView {

    override val mainScope: CoroutineScope by lazyFast {
        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob() + commonCoroutineExceptionHandler)
    }
}