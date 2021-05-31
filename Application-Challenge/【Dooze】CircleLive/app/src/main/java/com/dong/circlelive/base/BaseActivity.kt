package com.dong.circlelive.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Create by dooze on 5/6/21  8:56 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {
    override val mainScope: CoroutineScope by lazyFast {
        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob() + commonCoroutineExceptionHandler)
    }
}