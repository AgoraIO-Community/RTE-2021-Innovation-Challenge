package androidx.lifecycle

import com.dong.circlelive.base.commonCoroutineExceptionHandler
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Create by dooze on 2021/5/26  10:52 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

private const val JOB_KEY = "androidx.lifecycle.ViewModelCoroutineScope.JOB_KEY"


val ViewModel.scope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(
            JOB_KEY,
            CloseableCoroutineScopeEx(SupervisorJob() + Dispatchers.Main.immediate + commonCoroutineExceptionHandler)
        )
    }

fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return scope.launch(context, start, block)
}


internal class CloseableCoroutineScopeEx(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}